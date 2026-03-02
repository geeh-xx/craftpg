# Copilot Path-Specific Instructions

This directory contains path-specific instructions for GitHub Copilot. These instructions provide context-specific guidance for different parts of the codebase.

> **Important**: All code generation follows the **TDD cycle (Red → Green → Refactor)**.
> Every new feature also requires a **Cucumber `.feature` file** before production code is written.

## How It Works

When you work on files matching a specific path pattern, Copilot will automatically apply the relevant instructions from this directory in addition to the repository-wide instructions in `.github/copilot-instructions.md`.

## Available Instructions

### Java Source Files (`java.instructions.md`)
- **Applies to**: `**/*.java`
- **Purpose**: Java language guidelines and **mandatory TDD practice**
- **Key Topics**:
  - Test-Driven Development (Red → Green → Refactor) — enforced for all code
  - Java 25 features and best practices
  - DDD, Aggregate Root, Use Cases, and Exception Handling

### Test Layer (`test.instructions.md`)
- **Applies to**: `src/test/java/**/*.java`
- **Purpose**: Guidelines for writing unit tests and **Cucumber step definitions**
- **Key Topics**:
  - TDD cycle enforcement
  - Test structure and naming
  - Mocking strategies with Mockito and Instancio
  - Cucumber step definition conventions
  - Coverage requirements (80% minimum via JaCoCo)

### Cucumber Feature Files (`cucumber.instructions.md`)
- **Applies to**: `src/test/resources/features/**/*.feature`
- **Purpose**: Guidelines for writing Gherkin `.feature` files — required for every feature
- **Key Topics**:
  - Feature file structure and location
  - Gherkin conventions (Feature, Background, Scenario, Scenario Outline)
  - Step definition and runner requirements
  - Tagging strategy
  - Definition of Done checklist

### Domain Layer (`domain.instructions.md`)
- **Applies to**: `src/main/java/com/craftpg/domain/*.java`
- **Purpose**: Guidelines for domain entities, aggregate roots, domain events, and enums
- **Key Topics**: 
  - Domain entity structure
  - Factory methods and behavior methods
  - Domain event patterns
  - Aggregate root implementation

### Use Case Layer (`usecase.instructions.md`)
- **Applies to**: `src/main/java/com/craftpg/application/usecase/*.java`
- **Purpose**: Guidelines for use case interfaces and implementations
- **Key Topics**:
  - Use case structure and naming
  - Interface and implementation patterns
  - Transaction management
  - Error handling

### Web Layer (`web.instructions.md`)
- **Applies to**: `src/main/java/com/craftpg/infrastructure/web/*.java`
- **Purpose**: Guidelines for controllers, DTOs, and API contracts
- **Key Topics**:
  - Controller implementation
  - API contract adherence
  - DTO usage
  - HTTP status codes

### Mapper Layer (`mapper.instructions.md`)
- **Applies to**: `src/main/java/com/craftpg/application/mapper/*.java`
- **Purpose**: Guidelines for mapper classes that convert between entities and DTOs
- **Key Topics**:
  - Mapper structure and responsibilities
  - Entity to DTO conversion
  - DTO to command object conversion
  - Enum mapping patterns

### Infrastructure Layer (`infrastructure.instructions.md`)
- **Applies to**: `src/main/java/com/craftpg/infrastructure/*.java`
- **Purpose**: Guidelines for configuration, exceptions, and security
- **Key Topics**:
  - Configuration classes
  - Structured exception hierarchy
  - Security implementations
  - Infrastructure best practices

### OpenAPI Specifications (`openapi.instructions.md`)
- **Applies to**: `docs/api/*.yaml`, `docs/api/*.yml`
- **Purpose**: Guidelines for API specification files
- **Key Topics**:
  - API design principles
  - Schema definitions
  - Security definitions
  - Best practices for RESTful APIs

## Best Practices

1. **TDD First**: Always write tests before production code — no exceptions
2. **Cucumber per Feature**: Every feature must have a `.feature` file with Gherkin scenarios
3. **Specificity**: Path-specific instructions should be focused on concerns specific to that layer or component type
4. **Complementary**: These instructions complement the repository-wide instructions, not replace them
5. **Maintenance**: Keep these instructions up to date as the codebase evolves
6. **Consistency**: Ensure instructions across different files don't conflict with each other

## Adding New Instructions

To add new path-specific instructions:

1. Create a new file with the naming convention: `<name>.instructions.md`
2. Add frontmatter with the `applyTo` glob pattern:
   ```yaml
   ---
   applyTo: "path/pattern/*.java"
   ---
   ```
3. Write clear, actionable instructions in Markdown format
4. Test the instructions by working on files matching the pattern

## Learn More

- [GitHub Copilot Documentation](https://docs.github.com/en/copilot)
- [Custom Instructions Guide](https://docs.github.com/en/copilot/customizing-copilot/adding-custom-instructions-for-github-copilot)
