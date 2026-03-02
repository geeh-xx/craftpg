# Project Information

- **Project Name**: craftpg
- **Repository**: https://github.com/geeh-xx/craftpg
- **Description**: RPG Campaign Management System with Spring Boot, Keycloak, and PostgreSQL
- **Java Version**: 25
- **Spring Boot Version**: 3.5.5
- **Build Tool**: Maven

## Core Commands

- **Documentation**: Use README.md as the primary source - contains comprehensive asset descriptions
- **No build/test commands**: This is a documentation/asset library with no compiled code
- **File validation**: Ensure Markdown syntax is valid for `.chatmode.md`, `.prompt.md` and `.instruction.md` files

### Major Components
- `.github/copilot-instructions.md` - Repository-level Copilot instructions (n)
- `.github/prompts/` - Reusable prompt files for common tasks
- `.github/instructions/` - Custom instruction files for Copilot
- `/features/` - Project feature for implementation

### File Naming
- Prompts: `[descriptive-name].prompt.md`
- Instructions: `[descriptive-name].instructions.md`
- Features: `[descriptive-name].feature.md`
- Use lowercase with hyphens for file names

### Markdown Standards
- Follow front matter YAML structure exactly as shown in examples
- Use proper heading hierarchy (# ## ###)
- Include description and tools array in chat mode front matter
- Include mode and description in prompt file front matter

## Code Review
When performing a code review:
- validate that there are changes in the `README.md` file that match the changes in the pull request. If there are no changes, or if the changes do not match, then the pull request is not ready to be merged.
- ensure that the values in the front matter are wrapped in single quotes.
- ensure that the `description` field in the front matter is not empty.
- on a `.instructions.md` file, ensure there is an `applyTo` property in the front matter that specifies the file or files to which the instructions apply.

## How to Build, Test, and Run

### Building the Project
```bash
mvn clean install
# Or using Makefile
make install
```

### Running Tests
```bash
mvn test
# Or using Makefile
make unit-test
```

### Running the Application
```bash
mvn spring-boot:run
# Or using Makefile
make run
```

### Code Coverage
```bash
mvn test jacoco:report
# Or using Makefile
make coverage
# View report at: target/site/jacoco/index.html
```

### Infrastructure
```bash
# Start PostgreSQL, Keycloak, and MailHog
docker-compose up -d
# Or using Makefile
make infra-up

# Stop infrastructure
make infra-down

# Reset infrastructure (removes volumes)
make infra-reset
```

## Database

### Liquibase Migrations

- Use Liquibase for database schema migrations
- Changelogs are located in `src/main/resources/db/changelogs/`
- Use `.sql` files for changelogs
- Each migration must have its own changelog sub-folder
- Naming convention: `<date>-<domain>-<description>.sql` (e.g., `db/changelogs/user/2024-06-15-createuser.sql`)
- Never modify existing migrations; create new ones for changes

### Docker Infrastructure

- Dockerfile locations: `utils/` directory (e.g., `utils/keycloak/`)
- Use `docker-compose.yml` in the root directory to manage services
- Services: PostgreSQL, Keycloak, MailHog

## API Development

### API-First Approach

- Use API-First approach for developing new features
- Define OpenAPI 3.0.3 specification before implementation
- OpenAPI specification files are located in `docs/api/`
- Use Spring Boot OpenAPI Maven generator to:
  - Generate API interfaces from OpenAPI specifications
  - Generate DTOs from OpenAPI specifications
- Implement the API interface contract before implementing the feature logic

## Documentation

### Feature Documentation

- Every feature must have documentation using Antora
- Documentation is located in the `docs/documentation` folder
- Each feature must have a separate documentation file
- Follow Antora documentation structure and conventions

## Code Quality and Security

### Quality Checks

- Use code scanning tools for code quality
- Check for security vulnerabilities
- Run linters before committing code
- Ensure all tests pass before submitting pull requests

### SOLID Principles

- Follow SOLID principles for object-oriented design:
  - **S**ingle Responsibility Principle
  - **O**pen/Closed Principle
  - **L**iskov Substitution Principle
  - **I**nterface Segregation Principle
  - **D**ependency Inversion Principle

## Version Control

### Branching Strategy

- Use feature branch strategy
- Branch naming: `feature/<feature-name>`
- Example: `feature/user-authentication`

### Pull Request Strategy

- Use merge rebase strategy for merging pull requests
- Ensure all tests pass before merging
- Squash commits when appropriate
- Write clear commit messages