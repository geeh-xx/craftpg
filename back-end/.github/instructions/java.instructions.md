---
description: 'Guidelines for building Java base applications'
applyTo: '**/*.java'
---

# Java Development

## Language Features

- Use Java Streams and Lambdas for collection processing
- Use Java Records for simple data carrier classes (e.g., DTOs, Commands, Events)
- Use `Optional<T>` to handle nullable values instead of returning null
- Use Lombok`@NonNull` annotation for required method parameters
- Use `final` keyword for immutable method parameters

## General Instructions

- First, prompt the user if they want to integrate static analysis tools (SonarQube, PMD, Checkstyle)
  into their project setup. If yes, provide guidance on tool selection and configuration.
- If the user declines static analysis tools or wants to proceed without them, continue with implementing the Best practices, bug patterns and code smell prevention guidelines outlined below.
- Address code smells proactively during development rather than accumulating technical debt.
- Focus on readability, maintainability, and performance when refactoring identified issues.
- Use IDE / Code editor reported warnings and suggestions to catch common patterns early in development.

- Use Java 25 features and syntax
- Follow best practices for Java development
- Follow the Google Java Style Guide: https://google.github.io/styleguide/javaguide.html
- Ensure code is well-documented with Javadoc comments for public APIs
- Use `var` for local variable type inference where appropriate
- Follow standard naming conventions:
  - PascalCase for class and interface names
  - camelCase for method and variable names
  - UPPER_SNAKE_CASE for constants

## Best practices

- **Records**: For classes primarily intended to store data (e.g., DTOs, immutable data structures), **Java Records should be used instead of traditional classes**.
- **Pattern Matching**: Utilize pattern matching for `instanceof` and `switch` expression to simplify conditional logic and type casting.
- **Type Inference**: Use `var` for local variable declarations to improve readability, but only when the type is explicitly clear from the right-hand side of the expression.
- **Immutability**: Favor immutable objects. Make classes and fields `final` where possible. Use collections from `List.of()`/`Map.of()` for fixed data. Use `Stream.toList()` to create immutable lists.
- **Streams and Lambdas**: Use the Streams API and lambda expressions for collection processing. Employ method references (e.g., `stream.map(Foo::toBar)`).
- **Null Handling**: Avoid returning or accepting `null`. Use `Optional<T>` for possibly-absent values and `Objects` utility methods like `equals()` and `@NonNull`.

### Naming Conventions

- Follow Google's Java style guide:
  - `UpperCamelCase` for class and interface names.
  - `lowerCamelCase` for method and variable names.
  - `UPPER_SNAKE_CASE` for constants.
  - `lowercase` for package names.
- Use nouns for classes (`UserService`) and verbs for methods (`getUserById`).
- Avoid abbreviations and Hungarian notation.


## Dependency Injection

- **NEVER** use `@Autowired` annotation - use constructor injection instead
- Use interfaces for defining contracts between components (not concrete classes)
- Use Lombok's `@RequiredArgsConstructor` annotation to generate constructors automatically

## Lombok Usage

- Use Lombok annotations to reduce boilerplate code:
  - `@Getter` for getter methods
  - `@Setter` for setter methods (use sparingly, prefer immutability)
  - `@RequiredArgsConstructor` for constructor injection
  - `@Slf4j` for logging
  - `@SneakyThrows` for methods that throw checked exceptions
  - `@Builder` for builder pattern on entities
  - `@AllArgsConstructor` when needed with Builder
  - `@NoArgsConstructor` for JPA entities (required by JPA)
- **NEVER** use `@Data` annotation (too broad, avoid implicit behaviors)

## Logging and Output

- Use `@Slf4j` for logging instead of `System.out.println`
- Use appropriate log levels: debug, info, warn, error

## Domain-Driven Design (DDD) and Architecture

### Behavior-Driven Development (BDD)

- Domain entities **MUST NOT** be anemic - they must have behavior methods
- Example behavior methods: `activateUser()`, `deactivateUser()`, `addProductToOrder()`, `removeProductFromOrder()`
- Domain entities must have **only getters, NO setters** (enforce immutability)
- Use `@NoArgsConstructor` for JPA entities (required by JPA/Hibernate)
    - Example annotations in domain entity:
      ```java
      @Entity
      @Table(name = "users")
      @NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA requirement
      @Getter
      public class User extends AbstractAggregateRoot<User> {
          // fields and behavior methods
      }
      ```
- Don't Use `@Builder` for the builder pattern on entities
  - Domain entities must have factory methods for creating new instances:
    - Factory method name: `create()`
    - Factory method accepts a Record class with all required fields
    - Example create method:
        ```java
        @Component
          public static User create(@NonNull final CreateUserCommand input) {
            
              //validation mandatory fields
              if (input.email() == null || input.email().isBlank()) {
                  throw new IllegalArgumentException("Email cannot be empty");
              }
            
              if (input.name() == null || input.name().isBlank()) {
                  throw new IllegalArgumentException("Name cannot be empty");
              }
            
              if (input.provider() == null) {
                  throw new IllegalArgumentException("Provider cannot be null");
              }
            
              if (input.verificationToken() == null || input.verificationToken().isBlank()) {
                  throw new IllegalArgumentException("Verification token cannot be empty");
              }
            
              // Generate UUID in application layer
              var user = new User();
              user.id = Generators.timeBasedGenerator().generate();
              user.email = input.email();
              user.name = input.name();
              user.provider = input.provider();
              user.providerId = input.providerId();
              user.emailVerified = false;
              user.enabled = true;
              user.role = Role.USER;
            
              // Trigger event BEFORE returning (as per DDD aggregate root pattern)
              user.registerEvent(new UserRegisteredEvent(
                      user.getId(), 
                      user.getEmail(), 
                      user.getName(), 
                      input.verificationToken()
              ));
            
              return user;
          }
        ```

### Aggregate Root Pattern and Events

- Use Aggregate Root pattern for domain entities
- Extend `AbstractAggregateRoot<T>` for entities that publish domain events
- Domain events **MUST** be triggered from the aggregate root entity after a state change and after the action completes
- Use `registerEvent()` to publish domain events
- Example: After `createUser()` completes, trigger `UserRegisteredEvent` from the User aggregate root
- Events should be Records located in `com.craftpg.domain.event` package

### Use Cases

- Use case interfaces should be named after the action: `CreateUserUsecase`, `UpdateProductUsecase`, `DeleteOrderUsecase`
- Implementation classes have the interface name plus `Impl` suffix: `CreateUserUsecaseImpl`, `UpdateProductUsecaseImpl`
- Use cases are in the `com.craftpg.application.usecase` package
- Each domain have you own usecase package in the `com.craftpg.application.usecase.<domainname>`, example: `com.craftpg.application.usecase.user`
- Each use case should be in its own sub-package in the domain package use case (package by feature structure)
- Example: `com.craftpg.application.usecase.user.createuser.CreateUserUsecaseImpl`,  `com.craftpg.application.usecase.user.deleteuser.DeleteUserUsecaseImpl`

## Exception Handling

### Structured Exceptions

- Use structured exception hierarchy with business-specific sub-packages
- Sub-packages by business domain: 
  - `com.craftpg.infrastructure.exception.security`
  - `com.craftpg.infrastructure.exception.validation`
  - `com.craftpg.infrastructure.exception.data`
- Each exception category must have its own base class (e.g., `SecurityException`, `DataException`, `ValidationException`)
- Specific exceptions must extend from their base class
  - Example: `InvalidCredentialsException` extends `SecurityException`
- Base exception classes must extend from `RuntimeException`
- **NEVER** use generic exceptions like `RuntimeException` or `Exception` directly
- All exceptions must have:
  - `serialVersionUID` field
  - `@Serial` annotation
- Follow exception handling best practices

## Code Style and Formatting

### Import Statements

- Avoid using full package names in import statements
- Use wildcard imports for packages with more than 3 classes
- Example: `import java.util.*` instead of importing each class individually

### Code Layout

- Always keep a blank line between:
  - Start and end of methods
  - If-else blocks
  - Class definitions
  - Loops (for, while, etc.)
  - Try-catch blocks
- This improves code readability


### Bug Patterns

| Rule ID | Description                                                 | Example / Notes                                                                                  |
| ------- | ----------------------------------------------------------- | ------------------------------------------------------------------------------------------------ |
| `S2095` | Resources should be closed                                  | Use try-with-resources when working with streams, files, sockets, etc.                           |
| `S1698` | Objects should be compared with `.equals()` instead of `==` | Especially important for Strings and boxed primitives.                                           |
| `S1905` | Redundant casts should be removed                           | Clean up unnecessary or unsafe casts.                                                            |
| `S3518` | Conditions should not always evaluate to true or false      | Watch for infinite loops or if-conditions that never change.                                     |
| `S108`  | Unreachable code should be removed                          | Code after `return`, `throw`, etc., must be cleaned up.                                          |

## Code Smells

| Rule ID | Description                                            | Example / Notes                                                               |
| ------- | ------------------------------------------------------ | ----------------------------------------------------------------------------- |
| `S107`  | Methods should not have too many parameters            | Refactor into helper classes or use builder pattern.                          |
| `S121`  | Duplicated blocks of code should be removed            | Consolidate logic into shared methods.                                        |
| `S138`  | Methods should not be too long                         | Break complex logic into smaller, testable units.                             |
| `S3776` | Cognitive complexity should be reduced                 | Simplify nested logic, extract methods, avoid deep `if` trees.                |
| `S1192` | String literals should not be duplicated               | Replace with constants or enums.                                              |
| `S1854` | Unused assignments should be removed                   | Avoid dead variables—remove or refactor.                                      |
| `S109`  | Magic numbers should be replaced with constants        | Improves readability and maintainability.                                     |
| `S1188` | Catch blocks should not be empty                       | Always log or handle exceptions meaningfully.                                 |


## Build and Verification

- After adding or modifying code, verify the project continues to build successfully.
- If the project uses Maven, run `mvn clean install`.
- If the project uses Gradle, run `./gradlew build` (or `gradlew.bat build` on Windows).
- Ensure all tests pass as part of the build.