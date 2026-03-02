# back-end/Makefile Targets

Required targets:
- help
- deps-up (runs docker compose up -d from repo root)
- deps-down
- clean (mvn clean)
- build (mvn clean package)
- test (mvn test)
- run (mvn spring-boot:run)
- gen-api (mvn generate-sources, OpenAPI Generator)
- docs (build Antora site)

Notes:
- gen-api must generate Spring interfaces from docs/api/openapi.yml
- docs must run Antora from back-end/docs/documentation