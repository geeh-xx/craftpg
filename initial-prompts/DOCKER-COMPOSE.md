# docker-compose.yml (Root) — Postgres + Keycloak + MinIO (S3)

Services:
- postgres: application database
- keycloak-db: keycloak database
- keycloak: auth server (imports realm on startup)
- minio: S3-compatible storage
- createbuckets: creates buckets on startup
- mailhog: SMTP email local

Default URLs:
- Keycloak: http://localhost:8081
- MinIO API: http://localhost:9000
- MinIO Console: http://localhost:9001
- Postgres app: localhost:5432
- Postgres keycloak: localhost:5433
- Mailhog: http://localhost:8025 

Env vars expected (root .env):
- MINIO_ROOT_USER
- MINIO_ROOT_PASSWORD
- MINIO_BUCKET_MAPS
- MINIO_BUCKET_PORTRAITS