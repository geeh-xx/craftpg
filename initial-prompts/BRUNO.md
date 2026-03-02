# Bruno API Collections

Location:
- back-end/docs/collections/ (native Bruno collection root; contains `bruno.json`)

Use:
- Create a Bruno collection for API endpoints
- Use an environment variable for Bearer token: {{token}}
- Keep collection aligned with docs/api/openapi.yml
- Import using the folder `back-end/docs/collections` (not the legacy `*.bruno.json` file)

Environment variables used by collection:
- `baseUrl` (example: `http://localhost:8080`)
- `token`
- `keycloakUrl` (example: `http://localhost:8081`)
- `realm` (example: `craftpg`)
- `clientId` (example: `craftpg-web`)
- `accessToken`
- `refreshToken`
- `adminAccessToken`
- `keycloakAdminUser` (default: `admin`)
- `keycloakAdminPassword` (default: `admin`)
- `defaultUserEmail` (default: `player@craftpg.dev`)
- `defaultUserPassword` (default: `Player@123`)
- `campaignId`
- `sessionId`
- `inviteToken`
- `characterBaseId`
- `campaignCharacterId`
- `notificationId`

Recommended collection items:
- Health
- Auth (login/refresh/logout/userinfo/cadastro via admin API)
- Campaigns (list/create/get by id/update/delete/finish)
- Invites (create/preview by token/accept)
- Characters (list/create/generate-random/campaign character me/update me/add xp)
- Sessions (list by campaign/create/update)
- Notifications (list/mark as read)

Collection organization:
- Grouped by domain folders (`Health`, `Campaigns`, `Invites`, `Characters`, `Sessions`, `Notifications`)
- `POST`/`PATCH` requests include ready-to-send example JSON bodies