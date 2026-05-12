--liquibase formatted sql

--changeset craftpg:2026-05-12-campaigns-add-version

ALTER TABLE campaign
    ADD COLUMN version bigint DEFAULT 0 NOT NULL;
