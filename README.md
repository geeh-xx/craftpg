# CraftPG Monorepo

CraftPG is a monorepo for the CraftPG platform.

## Structure
- `/back-end`: Java 25 + Spring Boot 4.0.3 project
- `/front-end`: Next.js application
- `docker-compose.yml`: Local infrastructure (Postgres, Keycloak, Minio, Mailhog)

## Architecture and docs
- Root documentation files describe scope, architecture and interfaces.
- Backend technical docs are under `back-end/docs`.
- Front-end app structure is under `front-end/src`.

## Getting Started

1. Copy `.env.example` to `.env`
```bash
cp .env.example .env
```

2. Start the infrastructure
```bash
docker-compose up -d
```

3. Keycloak will be available at `http://localhost:8081`, Minio at `http://localhost:9000` (Console at `:9001`), Mailhog at `http://localhost:8025`.

Please refer to the Makefiles in `back-end` and `front-end` for run instructions.
