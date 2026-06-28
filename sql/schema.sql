-- ============================================================
-- TallerTech S.R.L. — Schema de base de datos v2 (TP4)
-- Motor: MySQL 8 — Ejecutar en XAMPP antes de correr la app
-- ============================================================

CREATE DATABASE IF NOT EXISTS tallertech
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE tallertech;

CREATE TABLE IF NOT EXISTS cliente (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    apellido   VARCHAR(100) NOT NULL,
    telefono   VARCHAR(20),
    email      VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS vehiculo (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    marca      VARCHAR(80)  NOT NULL,
    modelo     VARCHAR(80)  NOT NULL,
    anio       YEAR,
    patente    VARCHAR(20)  NOT NULL UNIQUE,
    CONSTRAINT fk_vehiculo_cliente
        FOREIGN KEY (id_cliente) REFERENCES cliente(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS mecanico (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    apellido     VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS orden_trabajo (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    id_vehiculo    INT NOT NULL,
    descripcion    TEXT,
    estado         ENUM('RECIBIDO','EN_REPARACION','LISTO','ENTREGADO')
                       NOT NULL DEFAULT 'RECIBIDO',
    fecha_ingreso  DATE NOT NULL,
    fecha_estimada DATE,
    fecha_entrega  DATE,
    CONSTRAINT fk_orden_vehiculo
        FOREIGN KEY (id_vehiculo) REFERENCES vehiculo(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tarea (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    id_orden    INT NOT NULL,
    id_mecanico INT NOT NULL,
    descripcion TEXT NOT NULL,
    fecha       DATE NOT NULL,
    CONSTRAINT fk_tarea_orden
        FOREIGN KEY (id_orden)    REFERENCES orden_trabajo(id) ON DELETE CASCADE,
    CONSTRAINT fk_tarea_mecanico
        FOREIGN KEY (id_mecanico) REFERENCES mecanico(id)      ON DELETE RESTRICT
);

-- Nueva tabla: servicios (integra POO del TP3 con persistencia)
CREATE TABLE IF NOT EXISTS servicio (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    tipo             ENUM('MANTENIMIENTO','REPARACION') NOT NULL,
    descripcion      VARCHAR(255) NOT NULL,
    precio_base      DECIMAL(10,2) NOT NULL,
    -- Campos de ServicioMantenimiento
    tipo_mantenimiento VARCHAR(100),
    costo_insumos    DECIMAL(10,2),
    -- Campos de ServicioReparacion
    pieza            VARCHAR(100),
    nivel_complejidad INT
);

-- Datos de ejemplo
INSERT INTO cliente (nombre, apellido, telefono, email) VALUES
    ('Carlos',  'Gómez',    '1134567890', 'cgomez@email.com'),
    ('Lucía',   'Martínez', '1145678901', 'lmartinez@email.com'),
    ('Empresa', 'FlotaSA',  '1156789012', 'flota@empresa.com');

INSERT INTO vehiculo (id_cliente, marca, modelo, anio, patente) VALUES
    (1, 'Ford',       'Focus',   2018, 'AB123CD'),
    (1, 'Toyota',     'Corolla', 2020, 'EF456GH'),
    (2, 'Chevrolet',  'Cruze',   2019, 'IJ789KL'),
    (3, 'Volkswagen', 'Amarok',  2021, 'MN012OP');

INSERT INTO mecanico (nombre, apellido, especialidad) VALUES
    ('Juan',   'Pérez',    'Motor y transmisión'),
    ('Marcos', 'López',    'Electricidad'),
    ('Diego',  'Fernández','Chapa y pintura');

INSERT INTO orden_trabajo (id_vehiculo, descripcion, estado, fecha_ingreso, fecha_estimada) VALUES
    (1, 'Revisión de frenos', 'EN_REPARACION', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 2 DAY)),
    (3, 'Falla eléctrica en tablero', 'RECIBIDO', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 3 DAY));

INSERT INTO servicio (tipo, descripcion, precio_base, tipo_mantenimiento, costo_insumos) VALUES
    ('MANTENIMIENTO', 'Cambio de aceite y filtro', 2500, 'Preventivo', 1800);

INSERT INTO servicio (tipo, descripcion, precio_base, pieza, nivel_complejidad) VALUES
    ('REPARACION', 'Reparación de frenos delanteros', 3000, 'Pastillas', 2);
