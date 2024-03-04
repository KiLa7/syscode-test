-- liquibase formatted sql

-- changeset migration_ddl:1
CREATE TABLE IF NOT EXISTS student (id VARCHAR(36), name VARCHAR(255), email VARCHAR(255), PRIMARY KEY (id))