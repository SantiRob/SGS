# SGS Visits - Sistema de Gestión de Visitas

## Descripción del Proyecto
Sistema de gestión de visitas para SGS Colombia S.A.S., enfocado en el control y agendamiento de visitas a estaciones de gas natural.

## Tecnologías
- **Lenguaje**: Java
- **Framework UI**: JavaFX
- **Base de Datos**: MySQL
- **Arquitectura**: Modelo-Vista-Controlador (MVC)

## Objetivos
1. Optimizar el agendamiento de visitas técnicas
2. Mejorar el control de mantenimientos
3. Reducir visitas fallidas
4. Centralizar la información de estaciones

## Funcionalidades Principales
- Gestión de usuarios (CRUD)
- Control de estaciones de gas
- Programación y seguimiento de visitas
- Registro de tipos de mantenimiento
- Control de equipos de trabajo

## Estructura del Proyecto
```
sgs/
├── src/
│   ├── main/java/com/sgs/
│   │   ├── model/       # Clases de entidad
│   │   ├── controller/  # Controladores MVC
│   │   ├── repository/  # Acceso a datos
│   │   ├── service/     # Lógica de negocio
│   │   └── view/        # Interfaces de usuario
│   └── resources/       # Configuraciones
└── pom.xml
```

## Modelo de Datos
### Tablas Principales
- `users`: Gestión de usuarios
- `stations`: Información de estaciones
- `visits`: Registro de visitas
- `maintenance_types`: Tipos de mantenimiento
- `teams`: Equipos de trabajo
- `issues`: Registro de problemas encontrados

## Requisitos
- Java 11+
- Maven
- MySQL 8.0+

## Configuración
1. Clonar repositorio
2. Configurar `database.properties`
3. Ejecutar scripts SQL de inicialización
4. Compilar con Maven: `mvn clean install`

## Contribución
1. Fork del repositorio
2. Crear branch de feature
3. Commit de cambios
4. Push y crear Pull Request

```