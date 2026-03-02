# CraftPG Monorepo Structure

/
├── docker-compose.yml
├── .env.example
├── README.md
│
├── back-end/
│   ├── pom.xml
│   ├── Makefile
│   ├── README.md
│   ├── docs/
│   │   ├── api/
│   │   │   └── openapi.yml
│   │   ├── collections/
│   │   │   └── craftpg-api.bruno.json
│   │   └── documentation/
│   │       ├── antora-playbook.yml
│   │       ├── antora.yml
│   │       └── modules/
│   │           └── ROOT/
│   │               ├── nav.adoc
│   │               └── pages/
│   │                   ├── index.adoc
│   │                   ├── architecture.adoc
│   │                   ├── auth-keycloak.adoc
│   │                   ├── api.adoc
│   │                   └── runbook.adoc
│   ├── docker/
│   │   └── keycloak/
│   │       └── realm-craftpg.json
│   └── src/
│       ├── main/
│       │   ├── java/com/craftpg/...
│       │   └── resources/
│       │       ├── application.yml
│       │       └── db/
│       │           └── changelogs/
│       │               ├── db.changelog-master.yaml
│       │               ├── core/
│       │               │   └── 0001-core.yaml
│       │               ├── campaigns/
│       │               │   └── 0001-campaigns.yaml
│       │               ├── invites/
│       │               │   └── 0001-invites.yaml
│       │               ├── characters/
│       │               │   └── 0001-characters.yaml
│       │               └── sessions/
│       │                   └── 0001-sessions.yaml
│       └── test/
│           └── ...
│
└── front-end/
    ├── package.json
    ├── Makefile
    ├── README.md
    ├── .env.local.example
    └── src/
        └── ...