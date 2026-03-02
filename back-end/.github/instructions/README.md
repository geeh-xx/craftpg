# Copilot Path-Specific Instructions

This directory contains path-specific instructions for GitHub Copilot. These instructions provide context-specific guidance for different parts of the codebase.

## How It Works

When you work on files matching a specific path pattern, Copilot will automatically apply the relevant instructions from this directory in addition to the repository-wide instructions in `.github/copilot-instructions.md`.

## Available Instructions

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

### Test Layer (`test.instructions.md`)
- **Applies to**: `src/test/java/**/*.java`
- **Purpose**: Guidelines for writing tests
- **Key Topics**:
  - Test structure and naming
  - Mocking strategies
  - Test data generation with Instancio
  - Coverage requirements

### OpenAPI Specifications (`openapi.instructions.md`)
- **Applies to**: `docs/api/*.yaml`, `docs/api/*.yml`
- **Purpose**: Guidelines for API specification files
- **Key Topics**:
  - API design principles
  - Schema definitions
  - Security definitions
  - Best practices for RESTful APIs

## Best Practices

1. **Specificity**: Path-specific instructions should be focused on concerns specific to that layer or component type
2. **Complementary**: These instructions complement the repository-wide instructions, not replace them
3. **Maintenance**: Keep these instructions up to date as the codebase evolves
4. **Consistency**: Ensure instructions across different files don't conflict with each other

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
