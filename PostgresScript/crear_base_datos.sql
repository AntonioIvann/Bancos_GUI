-- Crear base de datos
CREATE DATABASE bancos_sistema;

-- Conectarse a la base de datos
\c bancos_sistema;

-- Tabla de Usuarios para autenticación
CREATE TABLE usuarios (
    idu SERIAL PRIMARY KEY,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK(tipo IN ('admin', 'usuario')),
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla de Bancos
CREATE TABLE bancos (
    idb SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Clientes
CREATE TABLE clientes (
    idc SERIAL PRIMARY KEY,
    apellido_paterno VARCHAR(50) NOT NULL,
    apellido_materno VARCHAR(50) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    telefono VARCHAR(20),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Tarjetas
CREATE TABLE tarjetas (
    idt SERIAL PRIMARY KEY,
    idc INTEGER NOT NULL,
    idb INTEGER NOT NULL,
    numero_tarjeta VARCHAR(20) UNIQUE NOT NULL,
    saldo DECIMAL(15, 2) DEFAULT 0.00,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activa BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (idc) REFERENCES clientes(idc) ON DELETE CASCADE,
    FOREIGN KEY (idb) REFERENCES bancos(idb) ON DELETE CASCADE
);

-- Tabla de Movimientos
CREATE TABLE movimientos (
    idm SERIAL PRIMARY KEY,
    idt INTEGER NOT NULL,
    cantidad DECIMAL(15, 2) NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK(tipo IN ('credito', 'debito')),
    descripcion VARCHAR(255),
    fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idt) REFERENCES tarjetas(idt) ON DELETE CASCADE
);

-- Crear índices para mejorar el rendimiento
CREATE INDEX idx_usuarios_usuario ON usuarios(usuario);
CREATE INDEX idx_clientes_nombre ON clientes(nombre);
CREATE INDEX idx_tarjetas_idc ON tarjetas(idc);
CREATE INDEX idx_tarjetas_idb ON tarjetas(idb);
CREATE INDEX idx_movimientos_idt ON movimientos(idt);
CREATE INDEX idx_movimientos_fecha ON movimientos(fecha_movimiento);

-- Insertar bancos de ejemplo
INSERT INTO bancos (nombre) VALUES ('Banco Nacional'), ('Banco Regional'), ('Banco Internacional');

-- Insertar usuario admin de prueba (contraseña: admin123)
INSERT INTO usuarios (usuario, contrasena, tipo, nombre, apellido, email) 
VALUES ('admin', '1234', 'admin', 'Administrador', 'Sistema', 'admin@banco.com');

-- Insertar usuario normal de prueba (contraseña: user123)
INSERT INTO usuarios (usuario, contrasena, tipo, nombre, apellido, email) 
VALUES ('usuario', '1234', 'usuario', 'Usuario', 'Prueba', 'usuario@banco.com');

COMMIT;
