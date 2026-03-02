---
applyTo: "src/main/java/com/craftpg/infrastructure/**/*.java"
---

# Infrastructure Layer Instructions

## Configuration Classes

- Located in `com.craftpg.infrastructure.config` package
- Use `@Configuration` annotation
- Use `@Bean` for bean definitions
- Keep configuration classes focused on a single concern
- Example: `SecurityConfig`, `DatabaseConfig`, `EmailConfig`

## Exception Classes

- Located in `com.craftpg.infrastructure.exception` package
- Organized by business domain in sub-packages:
  - `security` - authentication/authorization exceptions
  - `validation` - input validation exceptions
  - `data` - database/data access exceptions
- Each domain has a base exception class extending `RuntimeException`
- Specific exceptions extend from their domain base class
- All exceptions MUST have:
  - `@Serial` annotation
  - `private static final long serialVersionUID` field
- Include meaningful error messages
- Example:
  ```java
  public class SecurityException extends RuntimeException {
      @Serial
      private static final long serialVersionUID = 1L;
      
      public SecurityException(String message) {
          super(message);
      }
  }
  
  public class InvalidCredentialsException extends SecurityException {
      @Serial
      private static final long serialVersionUID = 1L;
      
      public InvalidCredentialsException(String message) {
          super(message);
      }
  }
  ```

## Security Classes

- Located in `com.craftpg.infrastructure.security` package
- Include authentication and authorization logic
- JWT token handling
- Security filters and handlers
- Use Spring Security best practices
- NEVER hardcode credentials or secrets

## Best Practices

- Keep infrastructure concerns separate from business logic
- Use interfaces to define contracts
- Make infrastructure components testable
- Use dependency injection
- Follow the dependency inversion principle
