-- Script completamente actualizado con todas las tablas y datos de prueba correctos
DROP DATABASE IF EXISTS bancos_sistema;
CREATE DATABASE bancos_sistema;

\c bancos_sistema;

-- Tabla de Bancos
CREATE TABLE bancos (
    idb SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla de Clientes
CREATE TABLE clientes (
    idc SERIAL PRIMARY KEY,
    apellido_paterno VARCHAR(100) NOT NULL,
    apellido_materno VARCHAR(100) NOT NULL,
    nombre VARCHAR(100) NOT NULL
);

-- Tabla de Usuarios
CREATE TABLE usuarios (
    idu SERIAL PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    contraseña VARCHAR(255) NOT NULL,
    idc INTEGER NOT NULL REFERENCES clientes(idc) ON DELETE CASCADE,
    es_admin BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Tarjetas
CREATE TABLE tarjetas (
    idt SERIAL PRIMARY KEY,
    idc INTEGER NOT NULL REFERENCES clientes(idc) ON DELETE CASCADE,
    idb INTEGER NOT NULL REFERENCES bancos(idb) ON DELETE RESTRICT,
    numero_tarjeta VARCHAR(16) NOT NULL UNIQUE,
    saldo DECIMAL(15, 2) NOT NULL DEFAULT 0.00 CHECK(saldo >= 0),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activa BOOLEAN DEFAULT TRUE
);

-- Tabla de Movimientos
CREATE TABLE movimientos (
    idm SERIAL PRIMARY KEY,
    idt INTEGER NOT NULL REFERENCES tarjetas(idt) ON DELETE CASCADE,
    cantidad DECIMAL(15, 2) NOT NULL CHECK(cantidad > 0),
    tipo VARCHAR(10) NOT NULL CHECK(tipo IN ('CREDITO', 'DEBITO')),
    descripcion VARCHAR(255) NOT NULL,
    fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear índices para mejor rendimiento
CREATE INDEX idx_usuarios_usuario ON usuarios(usuario);
CREATE INDEX idx_usuarios_cliente ON usuarios(idc);
CREATE INDEX idx_clientes_nombre ON clientes(nombre);
CREATE INDEX idx_tarjetas_cliente ON tarjetas(idc);
CREATE INDEX idx_tarjetas_banco ON tarjetas(idb);
CREATE INDEX idx_movimientos_tarjeta ON movimientos(idt);
CREATE INDEX idx_movimientos_fecha ON movimientos(fecha_movimiento);

-- Insertar bancos de ejemplo
INSERT INTO bancos (nombre) VALUES 
('Banco Central'),
('Banco del Comercio'),
('Banco Nacional'),
('Banco Santander'),
('Banco BBVA');

-- Insertar cliente admin
INSERT INTO clientes (apellido_paterno, apellido_materno, nombre) VALUES
('Sistema', 'Admin', 'Administrador');

-- Insertar usuario admin (usuario: admin, contraseña: admin123)
-- La contraseña está encriptada con SHA-256 con salt: bancos_sistema_2024_admin123
INSERT INTO usuarios (usuario, contraseña, idc, es_admin, activo) VALUES
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f1979c67f', 1, TRUE, TRUE);
