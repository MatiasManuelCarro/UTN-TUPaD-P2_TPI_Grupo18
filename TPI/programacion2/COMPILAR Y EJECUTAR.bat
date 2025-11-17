@echo off
echo Se necesita Maven instalado para que funcione la compilacion
echo. 
echo Para compilar y que conecte a la base de datos
echo se debe modificar el db.resources en TPI\programacion2\src\main\resources
echo se debe modificar el db.resources en TPI\programacion2\target\classes
echo. 
echo Si aparece un error de UTF-8 revisar en la carpeta Extras como configurar.
echo.
echo Para mas informacion, leer el README 
echo. 
pause

REM Cambiar la página de códigos a UTF-8
chcp 65001 >nul

REM Cambiar al directorio del proyecto
cd /d "%~dp0"

REM Ejecutar Maven: limpiar y empaquetar
call mvn clean
call mvn package

REM Ejecutar el JAR 
java -Dfile.encoding=UTF-8 -jar target/tfi-prog2-1.0-shaded.jar

pause
