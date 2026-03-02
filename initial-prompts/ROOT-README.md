# AI Generator Instruction — CraftPG Monorepo

Generate the complete monorepo strictly based on the specification documents listed below.

Do NOT reinterpret, extend, or modify rules.  
All technical, architectural, business, and UI rules are defined in those documents.

---



## Source of Truth Documents

### Architecture & Infrastructure
- REPO-STRUCTURE.md
- DOCKER-COMPOSE.md
- ENV.md
- BACKEND-PACKAGES.md
- BACKEND-POM.md
- BACKEND-MAKEFILE.md
- BACKEND-APPLICATION-YML.md
- BACKEND-SECURITY.md
- LIQUIBASE-STRUCTURE.md
- OPENAPI.md
- ENDPOINTS.md
- KEYCLOAK.md

### Engineering & Quality
- UNIT-TEST.md
- TDD.md

## isntructions

- Use the patterns for the backend inside the folder back-end/.github/copilot-instructions.md and  back-end/.github/instructions folder
-  Use the patterns for the frontend inside the folder front-end/.github/copilot-instructions.md and  front-end/.github/instructions folder


### Frontend
- FRONTEND-AUTH.md
- FRONTEND-MAKEFILE.md
- FRONTEND-README.md

### Product & UX
- BRIEF.md
- MVP-SCOPE.md
- PRD.md
- LANDING-PAGE-SPEC.md
- DESIGN-GUIDELINES.md

---

## Generation Rules

1. Every requirement must come exclusively from the documents above.
2. If there is any conflict, prioritize:
   - PRD.md (business behavior)
   - BACKEND-SECURITY.md (authorization rules)
   - LIQUIBASE-STRUCTURE.md (database structure)
   - OPENAPI.md (API contract)
3. Do not invent additional features.
4. Do not remove required folders.
5. Do not replace defined technologies.
6. Do not introduce alternative tooling.

---

## Expected Output

The generator must output:

- Full monorepo folder structure
- Backend project (Java 25 + Spring Boot 4.0.3 + Maven)
- Liquibase migrations grouped by domain
- OpenAPI spec and generated stubs
- Keycloak realm export
- Docker Compose infrastructure
- Frontend Next.js app with Flowbite
- Antora documentation skeleton
- Bruno collections
- Makefiles (backend and frontend)

The generated project must compile and run using only the instructions defined in the referenced documents.

---

No additional explanations.  
No reinterpretation.  
Strictly implement the specification.