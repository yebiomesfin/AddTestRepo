-- Initial schema (Spring Boot will manage via hibernate ddl-auto=update)
-- This file runs once on first container start

CREATE TABLE IF NOT EXISTS todos (
    id         BIGSERIAL PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    completed  BOOLEAN NOT NULL DEFAULT FALSE
);
