--liquibase formatted sql

--changeset copilot:2026-02-27-campaigns-init-campaign
CREATE TABLE campaign (
    id uuid PRIMARY KEY NOT NULL,
    title varchar(180) NOT NULL,
    description text,
    system varchar(80) NOT NULL,
    frequency varchar(40) NOT NULL,
    status varchar(40) NOT NULL,
    progress_percent int NOT NULL,
    created_at timestamp NOT NULL DEFAULT now(),
    updated_at timestamp NOT NULL DEFAULT now(),
    CONSTRAINT ck_campaign_progress CHECK (progress_percent >= 0 AND progress_percent <= 100)
);

--changeset copilot:2026-02-27-campaigns-init-role
CREATE TABLE campaign_role (
    campaign_id uuid NOT NULL,
    user_id uuid NOT NULL,
    role varchar(40) NOT NULL,
    created_at timestamp NOT NULL DEFAULT now(),
    CONSTRAINT pk_campaign_role PRIMARY KEY (campaign_id, user_id, role),
    CONSTRAINT fk_campaign_role_campaign FOREIGN KEY (campaign_id) REFERENCES campaign(id),
    CONSTRAINT fk_campaign_role_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);
