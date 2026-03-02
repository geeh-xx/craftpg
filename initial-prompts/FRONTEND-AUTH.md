# Frontend Auth (Next.js + next-auth + Keycloak OIDC)

## Goals
- Authenticate users via Keycloak (OIDC)
- Keep all `/app/**` routes protected
- Send `Authorization: Bearer <access_token>` to the Spring Boot API
- Support invite acceptance flow at `/invites/[token]`

## Recommended approach
- Use `next-auth` with a Keycloak (OIDC) provider
- Store `access_token` in the NextAuth session/jwt callback
- Use Next.js middleware to protect private routes

## Environment variables
- NEXT_PUBLIC_API_URL=http://localhost:8080
- NEXTAUTH_URL=http://localhost:3000
- NEXTAUTH_SECRET=...
- KEYCLOAK_ISSUER=http://localhost:8081/realms/craftpg
- KEYCLOAK_CLIENT_ID=craftpg-web
- KEYCLOAK_CLIENT_SECRET= (empty for public client, if using PKCE)

## Auth flow
1) User opens a protected page under `/app`
2) Middleware redirects to `/login` if not authenticated
3) `/login` triggers next-auth sign-in to Keycloak
4) Keycloak returns tokens to next-auth
5) Frontend stores `access_token` in session
6) API calls include Bearer token

## Route protection
- Public:
  - `/login`
  - `/invites/[token]` (still requires auth to accept; but token can be viewed)
- Protected:
  - `/app/**`
  - `/characters/**`

## API client pattern
- Create a small `apiClient` wrapper:
  - Reads session (server) or session hook (client)
  - Adds Authorization header
  - Handles 401 by redirecting to login

## Invite acceptance UX
- `/invites/[token]`:
  - Fetch invite public preview (GET /invites/{token})
  - If user not logged in, show "Sign in to accept"
  - After login:
    - Must pick/create a character base
    - Call POST /invites/{token}/accept with { characterBaseId }
  - On success, redirect to `/app/campaigns/{campaignId}`