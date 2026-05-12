--liquibase formatted sql

--changeset craftpg:2026-03-06-campaigns-add-create-by

ALTER TABLE campaign
    ADD COLUMN create_by uuid NOT NULL;

ALTER TABLE campaign
    ADD CONSTRAINT fk_campaign_create_by_user
    FOREIGN KEY (create_by) REFERENCES app_user(id);
