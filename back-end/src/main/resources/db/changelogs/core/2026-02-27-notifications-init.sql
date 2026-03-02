--liquibase formatted sql

--changeset copilot:2026-02-27-notifications-init-notification
CREATE TABLE notification (
    id uuid PRIMARY KEY NOT NULL,
    user_id uuid NOT NULL,
    type varchar(64) NOT NULL,
    payload_json jsonb NOT NULL,
    read_at timestamp,
    created_at timestamp NOT NULL DEFAULT now(),
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);

CREATE INDEX idx_notification_user_created_at ON notification (user_id, created_at DESC);
