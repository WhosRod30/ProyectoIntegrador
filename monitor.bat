@echo off
setlocal enabledelayedexpansion
title Monitor Sistema Avicola
color 0A

:inicio
cls
echo ================================
echo   MONITOR SISTEMA AVICOLA
echo ================================
echo Fecha: %date% %time%
echo.

:: Verificar si Java está instalado
java -version >nul 2>&1
if %ERRORLEVEL%==0 (
    echo ✅ Java: Instalado
) else (
    echo ❌ Java: No encontrado
)

:: Verificar si la aplicación está corriendo
:: Buscar proceso Java que ejecute nuestro JAR
tasklist /FI "IMAGENAME eq java.exe" /V | find /i "integrador-avicola" >nul 2>&1
if %ERRORLEVEL%==0 (
    echo ✅ Aplicación: EJECUTANDO
) else (
    :: Método alternativo: buscar por ventana del título
    tasklist /FI "IMAGENAME eq java.exe" /V | find /i "Sistema Avícola" >nul 2>&1
    if !ERRORLEVEL!==0 (
        echo ✅ Aplicación: EJECUTANDO ^(Login^)
    ) else (
        :: Método básico: verificar si hay algún java.exe corriendo
        tasklist /FI "IMAGENAME eq java.exe" >nul 2>&1
        if !ERRORLEVEL!==0 (
            echo ⚠️  Aplicación: Java detectado ^(verificar manualmente^)
        ) else (
            echo ❌ Aplicación: DETENIDA
        )
    )
)

:: Verificar directorio de logs
if exist "logs\" (
    echo ✅ Directorio logs: Disponible
    
    :: Contar archivos de log del día
    for /f %%i in ('dir logs\*.log /b 2^>nul ^| find /c /v ""') do set log_count=%%i
    echo 📁 Archivos log: !log_count!
    
    :: Verificar errores recientes (si existe el archivo)
    if exist "logs\errors.log" (
        for /f %%i in ('find /c /v "" ^< "logs\errors.log" 2^>nul') do set error_count=%%i
        echo ⚠️  Total errores: !error_count!
    )
) else (
    echo ❌ Directorio logs: No encontrado
)

:: Verificar conexión a base de datos (muy básico)
ping -n 1 localhost >nul
if %ERRORLEVEL%==0 (
    echo ✅ Localhost: Accesible
) else (
    echo ❌ Localhost: No accesible
)

echo.
echo ================================
echo Opciones:
echo [1] Ver logs recientes
echo [2] Iniciar aplicación
echo [3] Salir
echo [R] Refrescar (automático en 30s)
echo ================================

:: Auto-refresh después de 30 segundos
choice /c 123R /t 30 /d R /m "Seleccione opción:"

if %ERRORLEVEL%==1 goto ver_logs
if %ERRORLEVEL%==2 goto iniciar_app
if %ERRORLEVEL%==3 goto salir
if %ERRORLEVEL%==4 goto inicio

:ver_logs
cls
echo ================================
echo      LOGS RECIENTES
echo ================================
if exist "logs\avicola.log" (
    echo Últimas 10 líneas del log:
    echo.
    powershell -command "Get-Content logs\avicola.log -Tail 10"
) else (
    echo No se encontró el archivo de log.
)
echo.
pause
goto inicio

:iniciar_app
echo.
echo Iniciando aplicación...
start /min java -jar target\integrador-avicola-1.0-SNAPSHOT.jar
echo Aplicación iniciada en segundo plano.
timeout /t 3 >nul
goto inicio

:salir
echo.
echo Cerrando monitor...
exit
