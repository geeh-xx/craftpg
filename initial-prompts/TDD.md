# TDD Guidelines (Test-Driven Development)

## Goal
Deliver correct behavior with minimal regressions by writing tests first for core business rules.

## Loop
1) Red — write a failing test describing the behavior
2) Green — implement the simplest code to pass
3) Refactor — improve design without changing behavior

---

# Where to apply TDD in this project (high value)

## 1) Invite acceptance (highest risk)
Write tests first for:
- Expired token -> 410/400 style error
- Invite already accepted -> error
- Accept requires authentication
- Accept requires characterBaseId
- Creates membership (campaign_id,user_id -> campaign_character_id)
- Applies roles from invite (MODERATOR, PLAYER)
- Locks character in campaign (locked=true)
- User cannot accept twice

## 2) Permission boundaries
Write tests first for:
- Moderator cannot delete campaign
- Moderator cannot finish campaign
- Player cannot update campaign
- DM can do all above

## 3) “Character per campaign” invariants
Write tests first for:
- Same character_base can be used in multiple campaigns
- campaign_membership is unique per (campaign_id,user_id)
- No endpoint allows swapping a character in a campaign (MVP)

---

# Suggested TDD structure (Backend)

## Testing layers
- domain tests: pure rules
- application tests: usecases with mocked repositories
- (later) integration tests: API + DB + Liquibase

## Naming
- `Should_<expected>_When_<condition>`
Examples:
- `Should_Reject_When_InviteExpired`
- `Should_CreateMembership_When_AcceptInvite`

## Test data builders
Create small builders:
- InviteBuilder
- CampaignBuilder
- CharacterBaseBuilder
- JwtBuilder (or a helper to create Authentication objects)

---

# Practical tips
- Start with the smallest happy-path test, then add edge cases
- Avoid testing framework plumbing
- Use dependency inversion: usecases depend on domain repositories (interfaces) so you can mock them
- Keep validation rules in one place (usecase or domain), then tests become stable

---

# Definition of Done (DoD) for MVP features
A feature is “done” only if:
- Unit tests cover core business rules
- Permission edge cases are tested
- Code is refactored to a clean, readable shape
- OpenAPI is updated and regenerated stubs compile