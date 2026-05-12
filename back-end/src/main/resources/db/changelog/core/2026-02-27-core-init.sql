--liquibase formatted sql

--changeset craftpg:2026-02-27-core-init

CREATE TABLE app_user (
    id uuid PRIMARY KEY NOT NULL,
    email varchar(255) NOT NULL UNIQUE,
    display_name varchar(255),
    created_at timestamp NOT NULL DEFAULT now(),
    updated_at timestamp NOT NULL DEFAULT now()
);
