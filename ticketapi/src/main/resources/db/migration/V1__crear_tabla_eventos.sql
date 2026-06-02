CREATE TABLE evento (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    capacidad_maxima INTEGER NOT NULL
);