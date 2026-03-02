# Keycloak Setup

Realm:
- craftpg

Client:
- craftpg-web (public client)
- Redirect URIs: http://localhost:3000/*
- Web Origins: http://localhost:3000

Default user created on startup (realm import):
- Email/Username: `player@craftpg.dev`
- Password: `Player@123`
- Enabled and email verified

Realm settings:
- registrationAllowed=true
- verifyEmail=true
- email-as-username=true (optional)

Email (local Docker):
- SMTP host: `mailhog`
- SMTP port: `1025`
- From: `noreply@craftpg.local`
- Mail inbox UI: `http://localhost:8025`

Social login providers:
- Google provider alias: `google`
- GitHub provider alias: `github`

Required environment variables for realm import:
- `KEYCLOAK_GOOGLE_CLIENT_ID`
- `KEYCLOAK_GOOGLE_CLIENT_SECRET`
- `KEYCLOAK_GITHUB_CLIENT_ID`
- `KEYCLOAK_GITHUB_CLIENT_SECRET`

Provider callback URL (for both Google and GitHub app config):
- `http://localhost:8081/realms/craftpg/broker/<provider>/endpoint`
- Examples:
	- `http://localhost:8081/realms/craftpg/broker/google/endpoint`
	- `http://localhost:8081/realms/craftpg/broker/github/endpoint`

JWT issuer used by API:
- http://localhost:8081/realms/craftpg

Note:
- Campaign roles (DM/MODERATOR/PLAYER) are stored in PostgreSQL, not Keycloak.