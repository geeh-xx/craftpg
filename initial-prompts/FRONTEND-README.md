# craftpg-web (Front-end)

Tech:
- Next.js (App Router)
- next-auth
- Keycloak OIDC
- flowbite (https://flowbite.com/) UI Component Library

Env:
- NEXT_PUBLIC_API_URL=http://localhost:8080
- NEXTAUTH_URL=http://localhost:3000
- NEXTAUTH_SECRET=...
- KEYCLOAK_ISSUER=http://localhost:8081/realms/craftpg
- KEYCLOAK_CLIENT_ID=craftpg-web

Run:
- make install
- make dev