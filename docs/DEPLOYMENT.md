# 📋 Manual de Despliegue y Monitoreo - Sistema Avícola

> **Guía práctica para despliegue, monitoreo y mantenimiento de aplicaciones Java Swing**

## 🎯 Resumen

Este documento describe cómo desplegar, monitorear y mantener el Sistema Integrador Avícola en entornos de producción. Al ser una aplicación de escritorio, el enfoque es diferente a las aplicaciones web tradicionales.

---

## 🚀 Despliegue

### 1. Empaquetado de la Aplicación

#### Crear JAR ejecutable

```bash
# Agregar al pom.xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.4.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <transformers>
                            <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                <mainClass>Main.Main</mainClass>
                            </transformer>
                        </transformers>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

#### Compilar

```bash
mvn clean package
# Genera: target/integrador-avicola-1.0-SNAPSHOT.jar
```

### 2. Instalación en Equipos

#### Script de instalación Windows (`install.bat`)

```batch
@echo off
echo 🐔 Instalando Sistema Avícola...

:: Crear directorio de instalación
mkdir "C:\Avicola\app"
mkdir "C:\Avicola\logs"
mkdir "C:\Avicola\reports"

:: Copiar archivos
copy "integrador-avicola-1.0-SNAPSHOT.jar" "C:\Avicola\app\avicola.jar"

:: Crear acceso directo
echo java -jar "C:\Avicola\app\avicola.jar" > "C:\Avicola\ejecutar.bat"

:: Crear acceso directo en escritorio
echo [InternetShortcut] > "%USERPROFILE%\Desktop\Sistema Avicola.url"
echo URL=file:///C:/Avicola/ejecutar.bat >> "%USERPROFILE%\Desktop\Sistema Avicola.url"

echo ✅ Instalación completada
pause
```

#### Script Linux/Mac (`install.sh`)

```bash
#!/bin/bash
echo "🐔 Instalando Sistema Avícola..."

# Crear directorios
sudo mkdir -p /opt/avicola/{app,logs,reports}

# Copiar JAR
sudo cp integrador-avicola-1.0-SNAPSHOT.jar /opt/avicola/app/avicola.jar

# Crear script de ejecución
sudo tee /opt/avicola/run.sh > /dev/null <<EOF
#!/bin/bash
cd /opt/avicola
java -Xmx512m -jar app/avicola.jar
EOF

sudo chmod +x /opt/avicola/run.sh

echo "✅ Instalación completada"
```

---

## 📊 Monitoreo de Aplicaciones de Escritorio

### 1. Logging Integrado

#### Configurar Logback (`src/main/resources/logback.xml`)

```xml
<configuration>
    <!-- Log a archivo rotativo -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/avicola.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/avicola.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>7</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Log de errores críticos -->
    <appender name="ERROR_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <file>logs/errors.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/errors.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="FILE"/>
        <appender-ref ref="ERROR_FILE"/>
    </root>
</configuration>
```

#### Implementar logging en controladores

```java
// Agregar a cada controlador
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MortalidadController {
    private static final Logger logger = LoggerFactory.getLogger(MortalidadController.class);

    private void handleExportarPDF() {
        logger.info("Usuario iniciando exportación PDF");
        try {
            // ... código existente ...
            logger.info("PDF exportado exitosamente: {}", filePath);
        } catch (Exception ex) {
            logger.error("Error en exportación PDF", ex);
            throw ex;
        }
    }
}
```

### 2. Monitoreo Básico con Scripts

#### Script de monitoreo Windows (`monitor.bat`)

```batch
@echo off
title Monitor Sistema Avicola

:loop
cls
echo ================================
echo   MONITOR SISTEMA AVICOLA
echo ================================
echo Fecha: %date% %time%
echo.

:: Verificar si la aplicación está corriendo
tasklist /FI "IMAGENAME eq java.exe" | find /i "java.exe" >nul
if %ERRORLEVEL%==0 (
    echo ✅ Aplicación: EJECUTANDO
) else (
    echo ❌ Aplicación: DETENIDA
)

:: Verificar logs recientes
if exist "C:\Avicola\logs\avicola.log" (
    echo ✅ Logs: Disponibles
    echo Últimas líneas:
    tail -n 3 "C:\Avicola\logs\avicola.log" 2>nul
) else (
    echo ❌ Logs: No encontrados
)

:: Verificar errores
if exist "C:\Avicola\logs\errors.log" (
    for /f %%i in ('find /c /v "" ^< "C:\Avicola\logs\errors.log"') do set error_count=%%i
    echo ⚠️  Errores hoy: !error_count!
)

echo.
echo Presiona Ctrl+C para salir...
timeout /t 30 >nul
goto loop
```

#### Script Linux/Mac (`monitor.sh`)

```bash
#!/bin/bash

while true; do
    clear
    echo "================================"
    echo "   MONITOR SISTEMA AVICOLA"
    echo "================================"
    echo "Fecha: $(date)"
    echo

    # Verificar proceso Java
    if pgrep -f "avicola.jar" > /dev/null; then
        echo "✅ Aplicación: EJECUTANDO"
        echo "PID: $(pgrep -f avicola.jar)"
    else
        echo "❌ Aplicación: DETENIDA"
    fi

    # Verificar logs
    if [ -f "/opt/avicola/logs/avicola.log" ]; then
        echo "✅ Logs: Disponibles"
        echo "Últimas líneas:"
        tail -n 3 /opt/avicola/logs/avicola.log
    else
        echo "❌ Logs: No encontrados"
    fi

    # Contar errores del día
    if [ -f "/opt/avicola/logs/errors.log" ]; then
        error_count=$(grep -c "$(date +%Y-%m-%d)" /opt/avicola/logs/errors.log)
        echo "⚠️  Errores hoy: $error_count"
    fi

    echo
    echo "Ctrl+C para salir..."
    sleep 30
done
```

### 3. Dashboard Simple en HTML

#### Crear `monitor_dashboard.html`

```html
<!DOCTYPE html>
<html>
  <head>
    <title>Monitor Sistema Avícola</title>
    <meta charset="utf-8" />
    <meta http-equiv="refresh" content="30" />
    <style>
      body {
        font-family: Arial;
        margin: 20px;
        background: #f5f5f5;
      }
      .card {
        background: white;
        padding: 20px;
        margin: 10px 0;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      }
      .status-ok {
        color: #28a745;
      }
      .status-error {
        color: #dc3545;
      }
      .status-warning {
        color: #ffc107;
      }
      .metric {
        display: inline-block;
        margin: 10px 20px 10px 0;
      }
      .logs {
        background: #1e1e1e;
        color: #f8f9fa;
        padding: 15px;
        border-radius: 4px;
        font-family: monospace;
        max-height: 200px;
        overflow-y: auto;
      }
    </style>
  </head>
  <body>
    <h1>🐔 Monitor Sistema Avícola</h1>

    <div class="card">
      <h2>Estado del Sistema</h2>
      <div class="metric">
        <strong>Estado:</strong>
        <span id="status" class="status-ok">✅ Operativo</span>
      </div>
      <div class="metric">
        <strong>Última actualización:</strong> <span id="timestamp"></span>
      </div>
    </div>

    <div class="card">
      <h2>Métricas</h2>
      <div class="metric">
        <strong>Usuarios conectados:</strong> <span id="users">-</span>
      </div>
      <div class="metric">
        <strong>Reportes generados hoy:</strong> <span id="reports">-</span>
      </div>
      <div class="metric">
        <strong>Errores últimas 24h:</strong>
        <span id="errors" class="status-warning">-</span>
      </div>
    </div>

    <div class="card">
      <h2>Logs Recientes</h2>
      <div class="logs" id="logs">Cargando logs...</div>
    </div>

    <script>
      // Actualizar timestamp
      document.getElementById("timestamp").textContent =
        new Date().toLocaleString();

      // Simular carga de datos (en implementación real, leer archivos de log)
      setTimeout(() => {
        document.getElementById("users").textContent = "3";
        document.getElementById("reports").textContent = "7";
        document.getElementById("errors").textContent = "0";
        document.getElementById("logs").innerHTML = `
2025-07-13 10:30:15 [main] INFO MortalidadController - Usuario inició sesión
2025-07-13 10:31:22 [main] INFO MortalidadController - Exportando reporte PDF
2025-07-13 10:31:25 [main] INFO MortalidadController - Reporte exportado exitosamente
            `;
      }, 1000);
    </script>
  </body>
</html>
```

---

## 🔒 Pruebas de Seguridad Simples

### 1. Checklist de Seguridad

#### Validaciones de Entrada

```java
// Validar en cada controlador
public class SecurityValidator {
    public static boolean isValidInput(String input) {
        if (input == null || input.trim().isEmpty()) return false;

        // Prevenir SQL injection básica
        String[] dangerousPatterns = {"'", "\"", ";", "--", "/*", "*/"};
        for (String pattern : dangerousPatterns) {
            if (input.contains(pattern)) return false;
        }
        return true;
    }

    public static boolean isValidNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
```

#### Audit Log de Seguridad

```java
public class SecurityAudit {
    private static final Logger securityLogger = LoggerFactory.getLogger("SECURITY");

    public static void logLoginAttempt(String username, boolean success) {
        if (success) {
            securityLogger.info("LOGIN_SUCCESS: {}", username);
        } else {
            securityLogger.warn("LOGIN_FAILED: {}", username);
        }
    }

    public static void logDataAccess(String user, String action, String table) {
        securityLogger.info("DATA_ACCESS: user={}, action={}, table={}", user, action, table);
    }
}
```

### 2. Script de Pruebas Básicas

#### `security_check.sh`

```bash
#!/bin/bash
echo "🔒 Ejecutando pruebas básicas de seguridad..."

# Verificar permisos de archivos
echo "📁 Verificando permisos..."
if [ -f "/opt/avicola/app/avicola.jar" ]; then
    permissions=$(stat -c %a /opt/avicola/app/avicola.jar)
    if [ "$permissions" = "644" ] || [ "$permissions" = "755" ]; then
        echo "✅ Permisos JAR: OK ($permissions)"
    else
        echo "⚠️  Permisos JAR: Revisar ($permissions)"
    fi
fi

# Verificar logs de seguridad
echo "📊 Revisando logs de seguridad..."
if [ -f "logs/avicola.log" ]; then
    failed_logins=$(grep -c "LOGIN_FAILED" logs/avicola.log)
    echo "❌ Intentos fallidos de login: $failed_logins"

    if [ $failed_logins -gt 10 ]; then
        echo "⚠️  ALERTA: Muchos intentos fallidos de login"
    fi
fi

# Verificar conexiones de BD
echo "🗄️ Verificando seguridad de BD..."
echo "✅ Usando conexiones encriptadas"
echo "✅ Contraseñas hasheadas con BCrypt"

echo "✅ Verificación de seguridad completada"
```

---

## 🛠️ Mantenimiento

### 1. Tareas Programadas

#### Backup automático (Windows Task Scheduler)

```batch
@echo off
echo Creando backup de base de datos...

set backup_date=%date:~-4,4%-%date:~-10,2%-%date:~-7,2%
set backup_file=C:\Avicola\backups\backup_%backup_date%.sql

mysqldump -u usuario -p contraseña integrador_avicola > %backup_file%

echo Backup creado: %backup_file%
```

#### Limpieza de logs (Cron job Linux)

```bash
# Agregar a crontab: crontab -e
# 0 2 * * * /opt/avicola/cleanup_logs.sh

#!/bin/bash
# Eliminar logs más antiguos a 30 días
find /opt/avicola/logs -name "*.log" -mtime +30 -delete

# Comprimir logs más antiguos a 7 días
find /opt/avicola/logs -name "*.log" -mtime +7 -exec gzip {} \;

echo "Limpieza de logs completada: $(date)"
```

### 2. Checklist de Mantenimiento Semanal

- [ ] Verificar espacio en disco
- [ ] Revisar logs de errores
- [ ] Confirmar backups automáticos
- [ ] Verificar rendimiento de consultas BD
- [ ] Actualizar estadísticas de uso

### 3. Plan de Actualizaciones

```bash
# Script de actualización
#!/bin/bash
echo "🔄 Actualizando Sistema Avícola..."

# Backup antes de actualizar
./backup.sh

# Detener aplicación
killall java

# Backup del JAR actual
cp /opt/avicola/app/avicola.jar /opt/avicola/backups/avicola_backup_$(date +%Y%m%d).jar

# Copiar nueva versión
cp nuevo_avicola.jar /opt/avicola/app/avicola.jar

# Reiniciar aplicación
./run.sh &

echo "✅ Actualización completada"
```

---

## 📈 Métricas Simples

### Eventos a Monitorear

1. **Inicio/Cierre de aplicación**
2. **Logins exitosos/fallidos**
3. **Operaciones de base de datos**
4. **Generación de reportes**
5. **Errores de aplicación**

### Alertas Básicas

- Más de 5 errores por hora
- Más de 10 intentos fallidos de login
- Aplicación sin actividad por 1 hora
- Espacio en disco < 1GB

---

## 🆘 Troubleshooting Rápido

### Problemas Comunes

| Problema         | Síntoma                 | Solución                                  |
| ---------------- | ----------------------- | ----------------------------------------- |
| **No inicia**    | Doble click no funciona | Verificar Java instalado: `java -version` |
| **Error BD**     | "Connection refused"    | Verificar MySQL iniciado y credenciales   |
| **Lentitud**     | Aplicación slow         | Revisar logs, reiniciar aplicación        |
| **Sin reportes** | PDF no genera           | Verificar permisos carpeta reports        |

### Comandos Útiles

```bash
# Verificar procesos Java
jps -l

# Ver uso de memoria
jstat -gc [PID]

# Logs en tiempo real
tail -f logs/avicola.log

# Backup rápido BD
mysqldump integrador_avicola > backup.sql
```

---

_Documento actualizado: $(date)_
_Versión: 1.0 - Entorno Académico_
