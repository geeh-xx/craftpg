---
applyTo: "src/main/java/com/craftpg/domain/**/*.java"
---

# Domain Layer Instructions

## Domain Entities

- Domain entities MUST extend `AbstractAggregateRoot<T>` if they publish domain events
- Use `@Entity` and `@Table` annotations for JPA entities
- Use `@Getter` but NEVER `@Setter` - entities should be immutable after creation
- Use `@Builder` for the builder pattern
- Use `@AllArgsConstructor` and `@NoArgsConstructor` for JPA compatibility
- Generate UUIDs within the entity's factory method using `Generators.timeBasedEpochGenerator().generate()` (from `com.fasterxml.uuid` library) for entities exposed in APIs
- NEVER rely on database-generated IDs

## Factory Methods

- Every entity MUST have a static `create()` factory method
- Factory method accepts a Command record (from `com.craftpg.mapper.command` package)
- Factory method MUST validate all inputs before creating the entity
- Throw `IllegalArgumentException` for invalid inputs
- Register domain events using `registerEvent()` AFTER entity creation, BEFORE returning the entity
- This ensures the entity is in a valid state when events are registered

## Behavior Methods

- Domain entities MUST have behavior methods that encapsulate business logic
- Examples: `verifyEmail()`, `activateUser()`, `deactivateUser()`
- Behavior methods should validate state before executing
- Throw `IllegalStateException` for invalid state transitions
- Register domain events using `registerEvent()` AFTER the state change completes
- This ensures the domain event reflects the actual state change

## Domain Events

- Events MUST be Java Records
- Events are located in `com.craftpg.domain.event` package
- Event naming: `<Entity><Action>Event` (e.g., `UserRegisteredEvent`, `EmailVerifiedEvent`)
- Events should contain only the data needed by listeners
- Events are triggered AFTER the action completes successfully

## Enums

- Enums are located in `com.craftpg.domain.enums` package
- Use meaningful enum values in UPPER_SNAKE_CASE
- Add business logic to enums when appropriate

## Relationships
- Use JPA annotations for relationships (`@OneToMany`, `@ManyToOne`, etc.)
- Use `FetchType.LAZY` for collections to avoid unnecessary data loading
- Use `CascadeType.ALL` for child entities that should be persisted/removed with the parent
- Use `orphanRemoval = true` for child entities that should be deleted when removed from