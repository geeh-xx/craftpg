---
applyTo: "src/main/java/com/craftpg/application/usecase/**/*.java"
---

# Use Case Instructions

## Use Case Structure

- Each use case has two files:
  1. Interface in `com.craftpg.application.usecase.<domainame>` (e.g., `com.craftpg.application.usecase.user.CreateUserUsecase`)
  2. Implementation in `com.craftpg.application.usecase.<domainame>.<featurename>` (e.g., `com.craftpg.application.usecase.user.createusecase.CreateUserUsecaseImpl`)

## Interface Definition

- Interface naming: `<Action><Entity>Usecase`
- Single method that represents the use case action
- Method should accept a command/request object
- Method should return a result object or Optional
- Example:
  ```java
  public interface CreateUserUsecase {
      User execute(CreateUserCommand command);
  }
  ```

## Implementation Class

- Implementation naming: `<Interface>Impl`
- Use `@Service` or `@Component` annotation
- Use constructor injection with `@RequiredArgsConstructor`
- All dependencies should be `final` fields
- Example:
  ```java
  @Service
  @RequiredArgsConstructor
  @Slf4j
  public class CreateUserUsecaseImpl implements CreateUserUsecase {
      private final UserRepository userRepository;
      private final OtherDependency dependency;
      
      @Override
      public User execute(@NonNull final CreateUserCommand command) {
          // implementation
      }
  }
  ```

## Use Case Responsibilities

- Orchestrate domain entities and services
- Validate input data
- Call domain entity factory methods or behavior methods
- Persist entities using repositories
- Handle transaction boundaries
- Log important operations
- Throw appropriate exceptions

## Transaction Management

- Use `@Transactional` for database operations
- Place `@Transactional` on the implementation method, not the interface
- Use appropriate propagation and isolation levels when needed

## Error Handling

- Throw specific business exceptions (not generic RuntimeException)
- Use structured exceptions from `com.craftpg.infrastructure.exception`
- Log errors with appropriate log levels
- Provide meaningful error messages
