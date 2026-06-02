CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    nombre_comprador VARCHAR(255) NOT NULL,
    email_comprador VARCHAR(255) NOT NULL,
    evento_id BIGINT NOT NULL,
    CONSTRAINT fk_evento FOREIGN KEY (evento_id) REFERENCES evento(id) ON DELETE CASCADE
);