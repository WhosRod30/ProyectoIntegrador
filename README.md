# 🐔 Sistema Integrador Avícola

Sistema de gestión integral para granjas avícolas desarrollado en Java Swing con interfaz moderna y funcionalidades completas de administración.

## 📋 Características Principales

- **Gestión de Granjas**: Administración completa de granjas y galpones
- **Control de Mortalidad**: Registro y seguimiento de mortalidad por lotes
- **Gestión de Consumo**: Monitoreo de consumo de alimento y agua
- **Control de Ingresos**: Registro de ingresos y ventas
- **Reportes PDF**: Generación de reportes profesionales
- **Sistema de Usuarios**: Autenticación segura con encriptación BCrypt
- **Interfaz Moderna**: UI responsiva con FlatLaf

## 🛠️ Tecnologías

- **Java 21** - Lenguaje principal
- **Swing + FlatLaf** - Interfaz gráfica moderna
- **MySQL** - Base de datos
- **Maven** - Gestión de dependencias
- **iText PDF** - Generación de reportes
- **Spring Security Crypto** - Encriptación de contraseñas

## 🚀 Instalación Rápida

### Prerrequisitos

- Java 21 JDK
- MySQL 8.0+
- Maven 3.8+

### Pasos

1. **Clonar el repositorio**

   ```bash
   git clone https://github.com/FrankoFPM/integrador-avicola.git
   cd integrador-avicola
   ```

2. **Configurar base de datos**

   ```sql
   CREATE DATABASE integrador_avicola;
   -- Ejecutar scripts SQL incluidos en /database
   ```

3. **Compilar y ejecutar**
   ```bash
   mvn clean compile
   mvn exec:java -Dexec.mainClass="Main.Main"
   ```

## 📁 Estructura del Proyecto

```
src/main/java/
├── Conexion/          # Configuración de base de datos
├── Controlador/       # Lógica de negocio (MVC)
├── DAO/              # Acceso a datos
├── Modelo/           # Entidades del dominio
├── Vista/            # Interfaces gráficas
├── Util/             # Utilidades (PDF, seguridad)
└── Main/             # Punto de entrada
```

## 👥 Módulos del Sistema

| Módulo         | Descripción                   | Controlador            |
| -------------- | ----------------------------- | ---------------------- |
| **Granjas**    | Gestión de granjas y galpones | `GranjaController`     |
| **Mortalidad** | Control de mortalidad avícola | `MortalidadController` |
| **Consumo**    | Monitoreo de consumo          | `ConsumoController`    |
| **Ingresos**   | Gestión financiera            | `IngresoController`    |
| **Usuarios**   | Autenticación y perfiles      | `LoginController`      |

## 🔒 Características de Seguridad

- Autenticación con contraseñas encriptadas (BCrypt)
- Validación de entrada en formularios
- Conexiones seguras a base de datos
- Logs de auditoría

## 📊 Reportes Disponibles

- **Reporte de Mortalidad**: Análisis detallado con estadísticas
- Exportación a PDF con diseño profesional
- Filtros por fecha, granja y lote

## 🔧 Configuración

Editar `src/main/java/Conexion/Conexion.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/integrador_avicola";
private static final String USER = "tu_usuario";
private static final String PASSWORD = "tu_password";
```

## 📖 Documentación Adicional

### Para Desarrollo y Despliegue

- 📋 **[Manual de Despliegue y Monitoreo](docs/DEPLOYMENT.md)** - Guía completa para producción
- 🔒 **[Documentación de Seguridad](SECURITY.md)** - Medidas de seguridad y pruebas implementadas

## 🤝 Contribución

1. Fork el proyecto
2. Crear rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abrir Pull Request

---

_Desarrollado con ❤️ para la gestión avícola moderna_
