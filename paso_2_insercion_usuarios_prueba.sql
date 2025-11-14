USE tpi_prog_2;	

/* ===========================================================
 INSERCIÓN DE USUARIOS
=========================================================== */

INSERT INTO usuario 
(id_usuario, eliminado, username, nombre, apellido, email, fecha_registro, activo, estado)
VALUES
(1, 0, 'matias.carro', 'Matias', 'Carro', 'matias.carro@mail.com', '2025-11-14 00:22:53', 1, 'ACTIVO'),
(2, 0, 'hugo.catalan', 'Hugo', 'Catalan', 'hugo.catalan@mail.com', '2025-11-14 00:23:53', 1, 'ACTIVO'),
(3, 0, 'ignacio.carne', 'Ignacio', 'Carne', 'ignacio.carne@mail.com', '2025-11-14 00:24:40', 1, 'ACTIVO'),
(4, 0, 'gabriel.carbajal', 'Gabriel', 'Carbajal', 'gabriel.carbajal@mail.com', '2025-11-14 00:25:15', 1, 'ACTIVO');

INSERT INTO credencial_acceso 
(id_credencial, eliminado, usuario_id, estado, ultima_sesion, hash_password, salt, ultimo_cambio, requiere_reset)
VALUES
(1, 0, 1, 'ACTIVO', NULL, '9c54c37d7d49fc1f6bee5de14313915ba2aa7252550c8a63dbccdae791aed45f', '1f05a244324fbc1c2845c47911c81416', '2025-11-14 00:22:59', 0),
(2, 0, 2, 'ACTIVO', NULL, '83de667011fdd37c72b91dfa80754634309ce7590b28e5b8103134bb9d4ce9f2', 'a88c6229e545981effb8516c821f0cc2', '2025-11-14 00:23:59', 0),
(3, 0, 3, 'ACTIVO', NULL, 'b2b9d791185f19cf6df32ce9b8426583cd124b8272a9064f7c596515aeb34246', 'b2fe845062eae5cbcb78e726e5d66524', '2025-11-14 00:24:46', 0),
(4, 0, 4, 'ACTIVO', NULL, 'a5fed4461e10a0bd3f283be1658fd1eb7712f15fa2ec792d89c3068345801e84', '125b28f83d02162dbd9fc432a6c69fc0', '2025-11-14 00:25:21', 0);
