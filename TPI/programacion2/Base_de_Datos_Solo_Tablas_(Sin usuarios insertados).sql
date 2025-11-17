DROP DATABASE IF EXISTS tpi_prog_2;
CREATE DATABASE IF NOT EXISTS tpi_prog_2
CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE tpi_prog_2;	

CREATE TABLE usuario (
id_usuario INT NOT NULL AUTO_INCREMENT,
eliminado BOOLEAN NOT NULL DEFAULT FALSE, 
username VARCHAR(60) NOT NULL,
nombre VARCHAR(100) NOT NULL,
apellido VARCHAR(100) NOT NULL,
email VARCHAR(120) NOT NULL,
fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
activo BOOLEAN NOT NULL DEFAULT TRUE,
estado VARCHAR(15) NOT NULL DEFAULT 'ACTIVO', 
CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
CONSTRAINT uq_usuario_username UNIQUE (username),
CONSTRAINT uq_usuario_email UNIQUE (email),
CONSTRAINT ck_usuario_estado CHECK (estado IN ('ACTIVO','INACTIVO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE credencial_acceso (
id_credencial INT NOT NULL AUTO_INCREMENT,
eliminado BOOLEAN NOT NULL DEFAULT FALSE,
usuario_id INT NOT NULL,
estado VARCHAR(15) NOT NULL,
ultima_sesion TIMESTAMP NULL,
hash_password VARCHAR(255) NOT NULL,
salt VARCHAR(64) NULL,
ultimo_cambio DATETIME NULL,
requiere_reset BOOLEAN NOT NULL DEFAULT FALSE,
CONSTRAINT pk_credencial PRIMARY KEY (id_credencial),
CONSTRAINT uq_credencial_usuario UNIQUE (usuario_id),
CONSTRAINT fk_credencial_usuario FOREIGN KEY (usuario_id)
REFERENCES usuario(id_usuario)
ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT ck_cred_estado CHECK (estado IN ('ACTIVO','INACTIVO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX ix_usuario_activo ON usuario(activo);
CREATE INDEX ix_usuario_estado ON usuario(estado);
CREATE INDEX ix_cred_estado ON credencial_acceso(estado);

DELIMITER $$
-- 1) Si el usuario está INACTIVO, su credencial no puede quedar ACTIVA.
CREATE TRIGGER bi_credencial_no_activa_si_usuario_inactivo
BEFORE INSERT ON credencial_acceso
FOR EACH ROW
BEGIN
DECLARE v_estado_usuario VARCHAR(15);
SELECT estado INTO v_estado_usuario
FROM usuario
WHERE id_usuario = NEW.usuario_id;
IF v_estado_usuario = 'INACTIVO' AND NEW.estado = 'ACTIVO' THEN
-- Forzamos a INACTIVO para mantener coherencia
SET NEW.estado = 'INACTIVO';
END IF;
END$$
CREATE TRIGGER bu_credencial_no_activa_si_usuario_inactivo
BEFORE UPDATE ON credencial_acceso
FOR EACH ROW
BEGIN
DECLARE v_estado_usuario VARCHAR(15);
SELECT estado INTO v_estado_usuario
FROM usuario
WHERE id_usuario = NEW.usuario_id;
IF v_estado_usuario = 'INACTIVO' AND NEW.estado = 'ACTIVO' THEN
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'No se puede activar la credencial de un usuario
INACTIVO.';
END IF;
END$$

-- 2) Si cambia el estado del usuario, sincronizamos la credencial.
CREATE TRIGGER au_usuario_sync_estado_credencial
AFTER UPDATE ON usuario
FOR EACH ROW
BEGIN
IF NEW.estado <> OLD.estado THEN
UPDATE credencial_acceso
SET estado = CASE
WHEN NEW.estado = 'INACTIVO' THEN 'INACTIVO'
ELSE estado -- si el usuario pasa a ACTIVO, no forzamos; respetamos la cred actual
END
WHERE usuario_id = NEW.id_usuario;
END IF;
END$$
DELIMITER ;

-- catalogo

CREATE TABLE estado (
    id_estado INT NOT NULL AUTO_INCREMENT,
    nombre_estado VARCHAR(20) NOT NULL,
    CONSTRAINT pk_estado PRIMARY KEY (id_estado),
    CONSTRAINT uq_estado UNIQUE (nombre_estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Carga inicial de valores del catálogo
INSERT INTO estado (nombre_estado)
VALUES ('ACTIVO'), ('INACTIVO');

-- Indices

-- elimina los indices si existian anteriormente
DROP INDEX ix_usuario_activo ON usuario;
DROP INDEX ix_usuario_estado ON usuario;
DROP INDEX ix_cred_estado ON credencial_acceso;

-- se crean los indices para la tabla.
CREATE INDEX ix_usuario_activo ON usuario(activo);
CREATE INDEX ix_usuario_estado ON usuario(estado);
CREATE INDEX ix_cred_estado ON credencial_acceso(estado);


/* ===========================================================
 CREDENCIALES 1:1 (coherentes con usuario.estado)
=========================================================== */
INSERT INTO credencial_acceso
(usuario_id, estado, ultima_sesion, eliminado, hash_password, salt,
ultimo_cambio, requiere_reset)
SELECT
u.id_usuario,
-- Si el usuario está INACTIVO, la credencial queda INACTIVA; si no, patrón de estados
CASE
WHEN u.estado = 'INACTIVO' THEN 'INACTIVO'
WHEN u.id_usuario % 5 = 0 THEN 'INACTIVO'
ELSE 'ACTIVO'
END AS estado,
TIMESTAMP(
DATE_SUB(CURDATE(), INTERVAL (u.id_usuario % 365) DAY),
SEC_TO_TIME((u.id_usuario % 86400))
),
FALSE,
UPPER(SHA2(CONCAT('pw:', u.id_usuario), 256)),
SUBSTRING(UPPER(SHA2(CONCAT('salt:', u.id_usuario), 256)), 1, 32),
DATE_SUB(NOW(), INTERVAL (u.id_usuario % 180) DAY),
(u.id_usuario % 20 = 0)
FROM usuario u
LEFT JOIN credencial_acceso c ON c.usuario_id = u.id_usuario
WHERE c.usuario_id IS NULL;


/* ===========================================================
 STORED PROCEDURE para actualizar password
=========================================================== */
DELIMITER //
 
CREATE PROCEDURE sp_actualizar_password_seguro (
    IN p_usuario_id INT,
    IN p_nuevo_hash VARCHAR(255),
    IN p_nuevo_salt VARCHAR(64)
)
BEGIN
    -- Utiliza parámetros de entrada que son tratados como datos.
    -- La consulta está predefinida y no se construye con concatenación de strings (NO ES SQL DINÁMICO).
    UPDATE credencial_acceso
    SET
        hash_password = p_nuevo_hash,
        salt = p_nuevo_salt,
        ultimo_cambio = NOW(),
        requiere_reset = FALSE
    WHERE
        usuario_id = p_usuario_id;
        
END //
 
DELIMITER ;
-- Limpiamos el delimitador
