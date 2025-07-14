CREATE TABLE Granja
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    codigo VARCHAR(50) UNIQUE
);

CREATE TABLE ControlIngreso
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    fecha         DATE,
    id_granja     INT,
    numero_galpon INT,
    cantidad      INT,
    sexo          ENUM ('M','F'),
    lote          VARCHAR(50),
    observaciones TEXT,
    FOREIGN KEY (id_granja) REFERENCES Granja (id)
);

CREATE TABLE ControlConsumo
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    fecha         DATE,
    id_granja     INT,
    numero_galpon INT,
    tipo_alimento VARCHAR(100),
    cantidad      DOUBLE,
    observaciones TEXT,
    FOREIGN KEY (id_granja) REFERENCES Granja (id)
);


CREATE TABLE ControlMortalidad
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    fecha         DATE           NOT NULL,
    id_granja     INT            NOT NULL,
    numero_galpon INT            NOT NULL,
    cantidad      INT            NOT NULL CHECK (cantidad >= 0),
    sexo          ENUM ('M','F') NOT NULL,
    lote          VARCHAR(50),
    observaciones TEXT,
    FOREIGN KEY (id_granja) REFERENCES Granja (id)
);
CREATE TABLE usuario
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);


-- insertar datos en las tablas
-- Granja
INSERT INTO Granja (nombre, codigo)
VALUES ('Granja Central', 'GC001'),
       ('Granja Norte', 'GN002');

-- ControlIngreso
INSERT INTO ControlIngreso (fecha, id_granja, numero_galpon, cantidad, sexo, lote, observaciones)
VALUES ('2025-07-01', 1, 1, 1200, 'M', 'Lote X1', 'Ingreso inicial de pollos machos'),
       ('2025-07-02', 1, 2, 800, 'F', 'Lote X2', 'Ingreso de hembras');

-- ControlConsumo
INSERT INTO ControlConsumo (fecha, id_granja, numero_galpon, tipo_alimento, cantidad, observaciones)
VALUES ('2025-07-03', 1, 1, 'Balanceado Inicial', 250.0, 'Consumo diario'),
       ('2025-07-03', 1, 2, 'Balanceado Inicial', 180.0, 'Consumo diario');

-- ControlMortalidad
INSERT INTO ControlMortalidad (fecha, id_granja, numero_galpon, cantidad, sexo, lote, observaciones)
VALUES ('2025-07-04', 1, 1, 4, 'M', 'Lote X1', 'Mortalidad por calor'),
       ('2025-07-04', 1, 2, 2, 'F', 'Lote X2', 'Mortalidad por enfermedad');
