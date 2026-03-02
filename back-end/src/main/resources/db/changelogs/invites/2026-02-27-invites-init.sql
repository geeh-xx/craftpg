--liquibase formatted sql

--changeset copilot:2026-02-27-invites-init-campaign-invite
CREATE TABLE campaign_invite (
    id uuid PRIMARY KEY NOT NULL,
    campaign_id uuid NOT NULL,
    email varchar(255) NOT NULL,
    token_hash varchar(255) NOT NULL UNIQUE,
    roles_json jsonb NOT NULL,
    expires_at timestamp NOT NULL,
    accepted_at timestamp,
    accepted_by_user_id uuid,
    created_at timestamp NOT NULL DEFAULT now(),
    CONSTRAINT fk_campaign_invite_campaign FOREIGN KEY (campaign_id) REFERENCES campaign(id)
);
