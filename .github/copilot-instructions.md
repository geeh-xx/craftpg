# GitHub Copilot Code Review Instructions

This repository is the **CraftPG monorepo**, containing:

- `/back-end` — Java 25 + Spring Boot 4.0.3 REST API
- `/front-end` — Next.js application
- `docker-compose.yml` — Local infrastructure (Postgres, Keycloak, Minio, Mailhog)

## Instruction precedence

- Treat this root-level file as the primary source of truth for Copilot review behaviour.
- If guidance here conflicts with `back-end/.github/copilot-instructions.md` or `front-end/.github/copilot-instructions.md`, prefer the instructions in this file.
- For technology and dependency versions (for example, Spring Boot), always treat the build configuration files (such as the backend `pom.xml`) as authoritative if they differ from any Copilot instruction text.
## Code Review Guidelines

When reviewing pull requests, please:

### General
- Summarise every file changed: what was added, modified, or removed.
- Highlight breaking changes or backward-incompatible modifications.
- Flag any potential bugs, null-pointer risks, or unhandled edge cases.
- Point out missing or inadequate error handling.
- Suggest improvements to code readability and maintainability.

### Back-end (Java / Spring Boot)
- Enforce proper use of Spring annotations (`@Service`, `@Repository`, `@Transactional`, etc.).
- Check that REST endpoints follow RESTful conventions and return appropriate HTTP status codes.
- Verify that database queries are safe from SQL injection (prefer JPA/repositories over raw SQL).
- Flag missing input validation (`@Valid`, `@NotNull`, etc.).
- Ensure sensitive configuration is read from environment variables, not hardcoded.

### Front-end (Next.js / TypeScript)
- Ensure React hooks are used correctly and follow the Rules of Hooks.
- Check for missing `key` props in lists.
- Flag direct DOM manipulation that should use React state instead.
- Suggest extracting repeated logic into reusable components or custom hooks.
- Verify that API calls handle loading and error states.

### Security
- Flag any hardcoded credentials, tokens, or secrets.
- Highlight missing authentication or authorization checks.
- Check for improper CORS configuration.
- Flag user-supplied data rendered without sanitisation (XSS risk).

### Testing
- Note when new logic is added without corresponding unit or integration tests.
- Suggest test cases for critical paths that are not yet covered.
