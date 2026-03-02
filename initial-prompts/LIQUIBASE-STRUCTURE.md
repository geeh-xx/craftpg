# Liquibase Structure (Domain-based)

Location:
- back-end/src/main/resources/db/changelogs/

Master:
- db.changelog-master.yaml (includes all domain changelogs)

Domains (folders):
- core/       (app_user)
- campaigns/  (campaign, campaign_role)
- invites/    (campaign_invite)
- characters/ (character_base, campaign_character, campaign_membership)
- sessions/   (session, attendance, xp, npc, map, treasure)

Naming convention:
- 0001-<domain>.yaml
- 0002-<domain>.yaml (future changes)

Rules:
- Keep foreign keys in the same domain where possible
- Use uuid as PKs
- Use jsonb for payload fields