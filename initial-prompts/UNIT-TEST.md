# Unit Testing Strategy (Backend + Frontend)

## Purpose
Unit tests validate business logic in isolation (no real DB, no Keycloak, no HTTP server). They must be fast and deterministic.

---

# Backend (Spring Boot)

## Scope (what to unit test)
- application/usecase classes
- domain rules (locking, role constraints)
- permission checks (CampaignPermissionService) with mocked repositories
- token parsing helpers (CurrentUserService) with mocked Authentication/JWT

## What NOT to unit test
- JPA mappings (prefer integration tests)
- Liquibase migrations (prefer migration smoke tests / integration)
- Keycloak itself (mock JWT)

## Tooling (recommended)
- JUnit 5
- Mockito
- AssertJ

## Examples of unit test targets
- AcceptInviteUseCase:
  - rejects expired invite
  - rejects already accepted invite
  - rejects missing characterBaseId
  - creates membership + roles + campaign_character instance
- CampaignUpdateUseCase:
  - MODERATOR allowed
  - PLAYER forbidden
  - DM allowed
- CampaignFinishUseCase:
  - only DM allowed

## Test conventions
- One test class per usecase/service
- Use Arrange/Act/Assert
- No static shared state
- Use builders/factories for DTO/domain objects

---

# Frontend (Next.js)

## Scope
- UI logic functions (formatters, mapping)
- small hooks with deterministic behavior
- client API wrapper behavior (header injection) with mocks

## Tooling (recommended)
- Vitest or Jest
- React Testing Library (for components)
- MSW (for mocking API in component tests)

## What NOT to unit test
- next-auth internal behavior (use minimal smoke tests)
- large integration flows (prefer e2e later)