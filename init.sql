-- Crear base de datos
CREATE DATABASE IF NOT EXISTS sgs_visits_db;

-- Usar la base de datos
USE sgs_visits_db;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
                                     id_user INT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'technician',
    password VARCHAR(255) NOT NULL
    );

-- Tabla de estaciones
CREATE TABLE IF NOT EXISTS stations (
                                        id_station INT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    contact_email VARCHAR(255),
    status VARCHAR(50) NOT NULL
    );

-- Tabla de tipos de mantenimiento
CREATE TABLE IF NOT EXISTS maintenance_types (
                                                 id_type INT AUTO_INCREMENT PRIMARY KEY,
                                                 name VARCHAR(100) NOT NULL,
    description TEXT
    );

-- Tabla de visitas
CREATE TABLE IF NOT EXISTS visits (
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
CREATE TABLE IF NOT EXISTS issues (
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

-- Ejemplo de datos iniciales
INSERT INTO maintenance_types (name, description) VALUES
                                                      ('Preventivo', 'Mantenimiento programado para prevenir fallos'),
                                                      ('Correctivo', 'Mantenimiento para corregir una falla existente'),
                                                      ('Predictivo', 'Mantenimiento basado en el análisis de condición');

INSERT INTO users (name, email, role, password) VALUES
                                                    ('Administrador', 'admin@sgs.com', 'admin', 'password123'),
                                                    ('Técnico Principal', 'tecnico@sgs.com', 'technician', 'password456');
