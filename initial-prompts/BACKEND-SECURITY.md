# Backend Security (Spring Boot Resource Server + Keycloak)

## Goals
- Validate Keycloak-issued JWT access tokens
- Authorize actions using DB-based campaign roles, not Keycloak realm roles
- Enforce "moderator can do everything except delete/finish campaign"
- Enforce "user cannot be in campaign without a character" (membership rule)

## Authentication
- Spring Security configured as OAuth2 Resource Server (JWT)
- JWT validation uses:
  - issuer-uri = KEYCLOAK_ISSUER_URI (e.g. http://localhost:8081/realms/craftpg)
- The `sub` claim is treated as the canonical user ID (UUID stored in app_user.id)

## Authorization model
### Data sources
- campaign_role(campaign_id, user_id, role)
- campaign_membership(campaign_id, user_id, campaign_character_id)

### Core rules
- DM:
  - full access to campaign, sessions, invites, characters in campaign
  - only DM can delete campaign
  - only DM can finish campaign
- MODERATOR:
  - can edit campaign, sessions, invite users, manage session content
  - cannot delete or finish campaign
- PLAYER:
  - read access to campaign
  - can edit ONLY their own `campaign_character` (and only where allowed)
  - cannot edit campaign metadata

## Implementation strategy
### Recommended components
- `CurrentUserService`
  - Extract userId from JWT (sub)
  - Ensure app_user exists (create minimal profile on first request if desired)
- `CampaignPermissionService`
  - `boolean hasRole(UUID campaignId, UUID userId, Role role)`
  - `boolean canEditCampaign(UUID campaignId, UUID userId)` (DM or MODERATOR)
  - `boolean canDeleteCampaign(UUID campaignId, UUID userId)` (DM only)
  - `boolean canFinishCampaign(UUID campaignId, UUID userId)` (DM only)
  - `boolean canInvite(UUID campaignId, UUID userId)` (DM or MODERATOR)
  - `boolean canEditSession(UUID campaignId, UUID userId)` (DM or MODERATOR)
- Method-level security:
  - Use `@EnableMethodSecurity`
  - Use `@PreAuthorize("@campaignPermissionService.canEditCampaign(#campaignId, @currentUser.id())")`

## Endpoint access matrix (MVP)
- GET /campaigns: any authenticated user
- POST /campaigns: any authenticated user
- GET /campaigns/{id}: user must be in campaign_role for that campaign
- PATCH /campaigns/{id}: DM/MODERATOR
- DELETE /campaigns/{id}: DM only
- POST /campaigns/{id}/finish: DM only
- POST /campaigns/{id}/invites: DM/MODERATOR
- GET /invites/{token}: public (no auth)
- POST /invites/{token}/accept: authenticated + requires characterBaseId
- GET/POST /characters: authenticated
- POST /campaigns/{id}/sessions: DM/MODERATOR

## Hard constraints to enforce
- Accept invite MUST:
  - create campaign_membership(campaign_id, user_id, campaign_character_id)
  - create campaign_role rows based on invite roles (+ DM is never granted here in MVP)
  - mark invite accepted
- Prevent character swap:
  - campaign_membership has PK (campaign_id, user_id)
  - do not expose "replace character" endpoint in MVP
  - if needed later: require DM to remove membership first

## Security hygiene
- Validate invite token via hash in DB (store token_hash only)
- Invite expires_at is enforced server-side
- Use UUIDs everywhere
- Avoid trusting any role data coming from the client