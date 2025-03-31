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
│   │   └── service/     # Lógica de negocio
│   └── resources/       # Configuraciones y vistas
│       └── view/        # Interfaces de usuario
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

## Database

```
-- Crear base de datos
CREATE DATABASE sgs_visits_db;

-- Usar la base de datos
USE sgs_visits_db;

-- Tabla de usuarios
CREATE TABLE users (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Tabla de estaciones
CREATE TABLE stations (
    id_station INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    contact_email VARCHAR(255),
    status VARCHAR(50) NOT NULL
);

-- Tabla de tipos de mantenimiento
CREATE TABLE maintenance_types (
    id_type INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- Tabla de visitas
CREATE TABLE visits (
    id_visit INT AUTO_INCREMENT PRIMARY KEY,
    id_station INT NOT NULL,
    id_user INT NOT NULL,
    id_maintenance_type INT NOT NULL,
    date DATE NOT NULL,
    result VARCHAR(100),
    observations TEXT,
    FOREIGN KEY (id_station) REFERENCES stations(id_station),
    FOREIGN KEY (id_user) REFERENCES users(id_user),
    FOREIGN KEY (id_maintenance_type) REFERENCES maintenance_types(id_type)
);

-- Tabla de problemas/issues
CREATE TABLE issues (
    id_issue INT AUTO_INCREMENT PRIMARY KEY,
    id_visit INT NOT NULL,
    description TEXT NOT NULL,
    requires_revisit BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_visit) REFERENCES visits(id_visit)
);

-- Índices adicionales para mejorar el rendimiento
CREATE INDEX idx_visits_station ON visits(id_station);
CREATE INDEX idx_visits_user ON visits(id_user);
CREATE INDEX idx_visits_maintenance_type ON visits(id_maintenance_type);
CREATE INDEX idx_issues_visit ON issues(id_visit);

ALTER TABLE users MODIFY role VARCHAR(50) NOT NULL DEFAULT 'technician';

-- Ejemplo de datos iniciales (opcional)
INSERT INTO maintenance_types (name, description) VALUES 
('Preventivo', 'Mantenimiento programado para prevenir fallos'),
('Correctivo', 'Mantenimiento para corregir una falla existente'),
('Predictivo', 'Mantenimiento basado en el análisis de condición');

INSERT INTO users (name, email, role, password) VALUES 
('Administrador', 'admin@example.com', 'admin', 'password123'),
('Técnico Principal', 'tecnico@example.com', 'technician', 'password456');

```

```