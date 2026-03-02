---
applyTo: "src/main/java/com/craftpg/infrastructure/web/*.java"
---

# Web Layer Instructions

## Controllers

- Controllers are in `com.craftpg.infrastructure.web.controller` package
- Naming: `<Entity>Controller` (e.g., `UserController`, `AuthController`)
- Use `@RestController` annotation
- Use `@RequestMapping` for base path (e.g., `@RequestMapping("/api/v1/users")`)
- Controllers MUST implement the API interface from `com.craftpg.infrastructure.web.api`
- Use constructor injection with `@RequiredArgsConstructor`

## API Contracts

- API interfaces are generated from OpenAPI specifications
- Located in `com.craftpg.infrastructure.web.api` package
- DO NOT manually edit generated API interfaces
- Update the OpenAPI spec in `docs/api/` to make changes
- Re-generate interfaces using Maven plugin after spec changes

## DTOs (Data Transfer Objects)

- DTOs are in `com.craftpg.infrastructure.web.dto` package
- DTOs are generated from OpenAPI specifications
- DO NOT manually edit generated DTOs
- Use separate request and response DTOs when needed
- Use mappers to convert between entities and DTOs

## Controller Implementation

- Keep controllers thin - delegate business logic to use cases
- Controllers should only:
  1. Validate input (using `@Valid` annotation)
  2. Call use cases
  3. Map results to DTOs
  4. Return HTTP responses
- Example:
  ```java
  @RestController
  @RequiredArgsConstructor
  @Slf4j
  public class UserController implements UserApi {
      private final CreateUserUsecase createUserUsecase;
      private final UserMapper userMapper;
      
      @Override
      public ResponseEntity<UserResponse> createUser(@Valid UserRequest request) {
          var command = userMapper.toCommand(request);
          var user = createUserUsecase.execute(command);
          var response = userMapper.toResponse(user);
          return ResponseEntity.ok(response);
      }
  }
  ```

## HTTP Status Codes

- Use appropriate HTTP status codes:
  - 200 OK: Successful GET, PUT, PATCH
  - 201 Created: Successful POST
  - 204 No Content: Successful DELETE
  - 400 Bad Request: Validation errors
  - 401 Unauthorized: Authentication required
  - 403 Forbidden: Authorization failed
  - 404 Not Found: Resource not found
  - 500 Internal Server Error: Unexpected errors

## Error Handling

- Use `@ControllerAdvice` for global exception handling
- Return consistent error response structure
- Log errors appropriately
- Don't expose sensitive information in error messages
