# back-end/src/main/resources/application.yml

Must include:
- server.port=8080
- datasource config using root env vars:
  - APP_DB_HOST, APP_DB_PORT, APP_DB_NAME, APP_DB_USER, APP_DB_PASS
- JPA ddl-auto=validate
- Liquibase change-log:
  classpath:db/changelogs/db.changelog-master.sql
  migrations need to be in .sql
- Resource server JWT issuer:
  KEYCLOAK_ISSUER_URI
- MinIO config:
  - MINIO_ENDPOINT, MINIO_REGION
  - MINIO_BUCKET_MAPS, MINIO_BUCKET_PORTRAITS