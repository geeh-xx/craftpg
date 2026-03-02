--liquibase formatted sql

--changeset copilot:2026-02-27-sessions-init-campaign-session
CREATE TABLE campaign_session (
    id uuid PRIMARY KEY NOT NULL,
    campaign_id uuid NOT NULL,
    title varchar(180) NOT NULL,
    scheduled_at timestamp NOT NULL,
    summary text,
    notes text,
    attendance_json jsonb,
    xp_json jsonb,
    npcs_json jsonb,
    maps_json jsonb,
    treasures_json jsonb,
    created_at timestamp NOT NULL DEFAULT now(),
    CONSTRAINT fk_session_campaign FOREIGN KEY (campaign_id) REFERENCES campaign(id)
);
