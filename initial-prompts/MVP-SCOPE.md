# MVP-SCOPE — CraftPG (Tormenta20 V1)

## Must-have (MVP)

### Auth & Access
- Keycloak OIDC auth (email verification + social login support)
- Only authenticated users can use the app

### Campaigns
- Create / view / list / update campaigns
- Campaign fields:
  - title, description
  - system (Tormenta20 fixed in V1)
  - frequency (weekly/monthly)
  - status (not_started / in_progress / paused / finished)
  - progressPercent (manual field, 0–100)
- Owner is DM automatically
- Moderator can do everything except delete/finish campaign

### Roles & Permissions
- Roles: DM, MODERATOR, PLAYER
- User can have multiple roles (MODERATOR + PLAYER)
- Only DM/MODERATOR can edit campaign content

### Invites
- Invite by email + in-app notification (if user exists)
- Invite must include roles
- User must accept invite
- If user has no account: must create/login before accepting
- Accept requires selecting/creating a character (hard rule)

### Character (Tormenta20)
- Visual character sheet (real-sheet-inspired layout)
- Create character manually (base character)
- Generate random character option (basic)
- Per-campaign character instance with independent progression:
  - level, xp, sheet state, inventory/resources
- Character is locked per campaign: cannot switch after joining
- Level-up wizard (happy-path rules enforcement)

### Sessions
- Create sessions within campaigns (unlimited)
- Session fields:
  - date/time, title
  - summary, notes
  - participants/attendance
  - XP logging
- Session content:
  - NPCs
  - maps (upload link/reference)
  - treasures

## Should-have (Soon after MVP)
- PDF export of the sheet (real format)
- Shared library (NPCs/treasures/maps) across sessions
- Campaign templates and session templates
- Audit log (who changed what)

## Could-have (Future)
- Other RPG systems (D&D etc.) via ruleset plugin model
- Combat tracker / initiative / dice roller
- Real-time collaboration
- Public sharing / marketplace

## Out of scope (V1)
- Full coverage of every Tormenta20 edge-case rule/errata
- VTT features (fog of war, grid measurement, etc.)

## MVP hypotheses to validate
- Mandatory “join with character” does not kill onboarding
- DMs prefer this workflow over Notion/Docs
- Visual sheet + per-campaign progression is a real differentiator

## MVP success criteria
- ≥ 30% of new users create a campaign within 24h
- ≥ 40% invites accepted within 72h
- ≥ 25% campaigns reach 2+ sessions in 2 weeks
- DM 4-week retention ≥ 20% (early target)