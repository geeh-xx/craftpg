# Package Structure

## Base Packages

- **Domain entities** (database models): `com.craftpg.domain.model`
- **Domain events**: `com.craftpg.domain.event` (use Records for events)
- **Enums**: `com.craftpg.domain.enums`
- **Use case interfaces**: `com.craftpg.application.usecase.<domainname>` 
  - Example:  `com.craftpg.application.usecase.user`, `com.craftpg.application.usecase.product`
- **Use case implementations**: `com.craftpg.application.usecase.<domainname>.<featurename>` (package by feature)
  - Example: `com.craftpg.application.usecase.user.createuser.CreateUserUsecaseImpl`,  `com.craftpg.application.usecase.user.deleteuser.DeleteUserUsecaseImpl`,`com.craftpg.application.usecase.product.createproduct.CreateProductUsecaseImpl`,`com.craftpg.application.usecase.product.deleteproduct.DeleteProductUsecaseImpl`
- **Event listeners**: `com.craftpg.application.listener`
- **Repositories**: `com.craftpg.infrastructure.persistence` 
- **Mappers**: `com.craftpg.application.mapper`
- **Command classes**: `com.craftpg.application.mapper.command`
- **Infrastructure base**: `com.craftpg.infrastructure`
  - Configuration: `com.craftpg.infrastructure.config`
  - Exceptions: `com.craftpg.infrastructure.exception`
  - Security: `com.craftpg.infrastructure.security`

## Web Layer Packages

- **API contracts** (OpenAPI interfaces): `com.craftpg.infrastructure.web.api`
  - Generated from OpenAPI specification files in `docs/api/`
  - API version suffix: `v1` (e.g., `/v1/auth`, `/v1/user`, `/v1/product`)
- **Controllers**: `com.craftpg.infrastructure.web.controller`
  - Naming: Entity name + `Controller` suffix (e.g., `UserController`, `ProductController`)
  - Controllers **MUST** implement the API interface contract
  - One controller per domain entity
- **DTOs**: `com.craftpg.infrastructure.web.dto`
  - One DTO per entity (e.g., `UserDTO`, `ProductDTO`)
  - Separate request and response DTOs if needed
  - Generated from OpenAPI specification using springboot openapi maven generator

## Package by Feature Structure

- Use package-by-feature structure within the `application.usecase.<domainname>` package
- Each feature has its own sub-package: `com.craftpg.application.usecase.<domainname>.<featurename>`
- Example structure:
  - `com.craftpg.application.usecase.user.createuser.CreateUserUsecaseImpl`
  - `com.craftpg.application.usecase.user.updateuser.UpdateUserUsecaseImpl`
  - `com.craftpg.application.usecase.product.updateproduct.UpdateProductUsecaseImpl`
  - `com.craftpg.application.usecase.product.createproduct.CreateProductUsecaseImpl`

- One controller per domain entity can handle multiple features for that entity
