---
applyTo: "src/main/java/com/craftpg/application/mapper/*.java"
---

# Mapper Layer Instructions

## Mapper Structure

- Mappers are located in `com.craftpg.application.mapper` package
- One mapper class per domain entity (e.g., `UserMapper`, `ProductMapper`)
- Use `@Component` annotation to make them Spring-managed beans
- Use constructor injection with `@RequiredArgsConstructor` if the mapper has dependencies

## Mapper Responsibilities

- Convert domain entities to DTOs (Data Transfer Objects)
- Convert DTOs to command objects
- Handle enum conversions between domain and API layers
- Keep mappers focused on data transformation only - NO business logic

## Implementation Guidelines

- Use `@NonNull` annotation for all required parameters
- Use `final` keyword for immutable parameters
- Create separate methods for different mapping directions:
  - `toResponse()` - entity to response DTO
  - `toCommand()` - request DTO to command object
  - `toEntity()` - DTO to entity (if needed)
- Use private helper methods for complex field mappings (e.g., enum conversions)

## Example Mapper

```java
@Component
public class UserMapper {
    
    public UserResponse toUserResponse(@NonNull final User user) {
        
        final var response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setEmailVerified(user.getEmailVerified());
        response.setProvider(mapProvider(user.getProvider()));
        response.setRole(mapRole(user.getRole()));
        response.setEnabled(user.getEnabled());
        return response;
    }
    
    public CreateUserCommand toCreateCommand(@NonNull final RegisterRequest request) {
        
        return new CreateUserCommand(
            request.getEmail(),
            request.getName(),
            AuthProvider.LOCAL,
            null,
            generateVerificationToken()
        );
    }
    
    private UserResponse.ProviderEnum mapProvider(@NonNull final AuthProvider provider) {
        return UserResponse.ProviderEnum.fromValue(provider.name());
    }
    
    private UserResponse.RoleEnum mapRole(@NonNull final Role role) {
        return UserResponse.RoleEnum.fromValue(role.name());
    }
}
```

## Best Practices

- Always add blank lines between methods for readability
- Keep mapping logic simple and straightforward
- Use meaningful method names that clearly indicate the mapping direction
- Handle null values appropriately (prefer `@NonNull` to prevent nulls)
- Use `final` keyword for immutable parameters
- Don't mix mapping logic with validation or business rules
- Keep mappers focused on data transformation only - no business logic

