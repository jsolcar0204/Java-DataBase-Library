DROP DATABASE IF EXISTS videojuegos;

CREATE DATABASE videojuegos CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE videojuegos;

-- Crear tablas
CREATE TABLE editor (
	nombre VARCHAR(100) PRIMARY KEY,
    fecha_fundacion DATE
);

CREATE TABLE desarrollador (
	nombre VARCHAR(100) PRIMARY KEY,
    fecha_fundacion DATE
);

CREATE TABLE genero (
	nombre VARCHAR(50) PRIMARY KEY,
    descripcion VARCHAR(100)
);

CREATE TABLE plataforma (
	nombre VARCHAR(50) PRIMARY KEY
);

CREATE TABLE videojuego_genero (
	nombre_videojuego VARCHAR(200),
    nombre_genero VARCHAR(50)
);

CREATE TABLE videojuego_plataforma (
	nombre_videojuego VARCHAR(200),
    nombre_plataforma VARCHAR(50)
);

CREATE TABLE videojuego (
	nombre VARCHAR(200) PRIMARY KEY,
    sinopsis VARCHAR(1000),
    fecha_publicacion DATE,
    nombre_desarrollador VARCHAR(100),
    nombre_editor VARCHAR(100)
);

-- Añadir claves primarias y claves foráneas
ALTER TABLE videojuego_genero
ADD CONSTRAINT pk_videojuego_genero PRIMARY KEY (nombre_videojuego, nombre_genero),
ADD CONSTRAINT fk_nombre_videojuego_videojuego_genero FOREIGN KEY (nombre_videojuego) REFERENCES videojuego(nombre),
ADD CONSTRAINT fk_nombre_genero_videojuego_genero FOREIGN KEY (nombre_genero) REFERENCES genero(nombre)
;

ALTER TABLE videojuego_plataforma
ADD CONSTRAINT pk_videojuego_plataforma PRIMARY KEY (nombre_videojuego, nombre_plataforma),
ADD CONSTRAINT fk_nombre_videojuego_videojuego_plataforma FOREIGN KEY (nombre_videojuego) REFERENCES videojuego(nombre),
ADD CONSTRAINT fk_nombre_genero_videojuego_plataforma FOREIGN KEY (nombre_plataforma) REFERENCES plataforma(nombre)
;

ALTER TABLE videojuego
ADD CONSTRAINT fk_nombre_desarrollador FOREIGN KEY (nombre_desarrollador) REFERENCES desarrollador(nombre) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT fk_nombre_editor FOREIGN KEY (nombre_editor) REFERENCES editor(nombre) ON DELETE CASCADE ON UPDATE CASCADE
;

-- Índices
CREATE INDEX idx_editor
ON editor (nombre, fecha_fundacion)
;

CREATE INDEX idx_desarrollador
ON desarrollador (nombre, fecha_fundacion)
;

CREATE INDEX idx_genero
ON genero (nombre)
;

CREATE INDEX idx_plataforma
ON plataforma (nombre)
;

CREATE INDEX idx_videojuego_genero
ON videojuego_genero (nombre_videojuego, nombre_genero)
;

CREATE INDEX idx_videojuego_plataforma
ON videojuego_plataforma (nombre_videojuego, nombre_plataforma)
;

CREATE INDEX idx_videojuego
ON videojuego (nombre, nombre_desarrollador)
;
