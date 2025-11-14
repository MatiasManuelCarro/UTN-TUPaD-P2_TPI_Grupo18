@echo off
REM Cambiar al directorio del proyecto
cd /d "%~dp0"

REM Ejecutar Maven: limpiar y empaquetar
call mvn clean
call mvn package

REM Ejecutar el JAR 
java -jar "%~dp0target\tfi-prog2-1.0-shaded.jar"

pause
