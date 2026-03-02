--liquibase formatted sql

--changeset copilot:2026-02-27-characters-init-character-base
CREATE TABLE character_base (
    id uuid PRIMARY KEY NOT NULL,
    owner_user_id uuid NOT NULL,
    name varchar(180) NOT NULL,
    race varchar(100),
    clazz varchar(100),
    attributes_json jsonb,
    created_at timestamp NOT NULL DEFAULT now(),
    CONSTRAINT fk_character_base_user FOREIGN KEY (owner_user_id) REFERENCES app_user(id)
);

--changeset copilot:2026-02-27-characters-init-campaign-character
CREATE TABLE campaign_character (
    id uuid PRIMARY KEY NOT NULL,
    campaign_id uuid NOT NULL,
    character_base_id uuid NOT NULL,
    user_id uuid NOT NULL,
    level int NOT NULL,
    xp int NOT NULL,
    sheet_state_json jsonb,
    inventory_json jsonb,
    locked boolean NOT NULL,
    created_at timestamp NOT NULL DEFAULT now(),
    CONSTRAINT fk_campaign_character_campaign FOREIGN KEY (campaign_id) REFERENCES campaign(id),
    CONSTRAINT fk_campaign_character_base FOREIGN KEY (character_base_id) REFERENCES character_base(id)
);

--changeset copilot:2026-02-27-characters-init-campaign-membership
CREATE TABLE campaign_membership (
    campaign_id uuid NOT NULL,
    user_id uuid NOT NULL,
    campaign_character_id uuid NOT NULL,
    created_at timestamp NOT NULL DEFAULT now(),
    CONSTRAINT pk_campaign_membership PRIMARY KEY (campaign_id, user_id),
    CONSTRAINT fk_campaign_membership_campaign FOREIGN KEY (campaign_id) REFERENCES campaign(id),
    CONSTRAINT fk_campaign_membership_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_campaign_membership_character FOREIGN KEY (campaign_character_id) REFERENCES campaign_character(id)
);
