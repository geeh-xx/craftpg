# CraftPG Back-end

Serviço API do CraftPG.

## Tecnologias e frameworks
- Java 25
- Spring Boot 4
- Spring Security
- Spring Data JPA (Hibernate)
- PostgreSQL
- Liquibase
- OpenAPI (contrato e geração de stubs)

## Estrutura
- `src/main/java`: código-fonte da aplicação
- `src/main/resources`: configurações e recursos
- `docs/api`: contrato OpenAPI
- `docs/collections`: coleções Bruno

## Arquitetura e padrões
- Arquitetura em camadas com separação entre domínio, aplicação, infraestrutura e web.
- Organização por casos de uso (`application/usecase`) para regras de aplicação.
- Mapeamento explícito entre domínio e DTOs de API.
- Contrato-first para API via OpenAPI (`docs/api/openapi.yml`).

## Requisitos
- Java 25
- Maven

## Docker: quando precisa
- Para desenvolvimento local completo, use Docker para subir dependências (PostgreSQL, Keycloak, MinIO, MailHog).
- É possível rodar sem Docker apenas se essas dependências já estiverem disponíveis externamente e configuradas.

## Executar (com Docker)
- `make deps-up`
- `make run`

## Executar (sem Docker)
- Garanta banco, autenticação e serviços auxiliares já disponíveis e configurados.
- Execute: `make run`

## Build e testes
- `make build`
- `make test`

## OpenAPI
- Fonte: `docs/api/openapi.yml`
- Gerar stubs: `make gen-api`

## MailHog (local)
- SMTP local padrão: `localhost:1025`
- UI MailHog: `http://localhost:8025`
