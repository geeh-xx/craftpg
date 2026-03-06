---
name: BDD Backend Scaffold
description: Given a Cucumber .feature file, scaffold the complete backend structure (OpenAPI, domain model, repository, use cases, mapper, controller, step definitions, unit tests) following the project conventions.
infer: true
tools: ['read', 'edit', 'create', 'search', 'execute']
---

You are a backend code generator for the **craftpg** project.
When given a Cucumber `.feature` file, generate every backend artifact needed to implement that feature end-to-end, following the project's layered architecture and conventions.

---

## Input

The user provides:
1. The path to a `.feature` file already placed in `src/test/resources/features/<domain>/`.
2. Optionally, the domain name and resource name (infer them from the feature file if not provided).

---

## Generation Order

Work through each step in the order below. Read the relevant instruction file before generating each artifact, and **always** prefer patterns already present in the codebase.

---

### Step 1 — Analyse the Feature File

Read the `.feature` file and extract:
- **Domain name** (e.g., `campaign`, `character`, `invite`)
- **Resource name** in PascalCase (e.g., `Campaign`, `Character`)
- **HTTP method + path** pairs used in the scenarios (e.g., `POST /items`, `GET /items/{itemId}`)
- **Request payload fields** inferred from `Examples` tables or step text
- **Response fields** inferred from `Then` assertions
- **Business rules** described in scenarios (validations, constraints, permissions)
- **Use cases** needed (one per distinct business operation, e.g., `CreateItem`, `ListItem`, `DeleteItem`)

---

### Step 2 — Update the OpenAPI Specification

File: `back-end/docs/api/openapi.yml`

- Add a new `paths` section for each HTTP method + path found in Step 1.
- Define request and response schemas in `components/schemas`:
  - `<Resource>Response` — fields returned to the client
  - `Create<Resource>Request` — request body for creation
  - `Update<Resource>Request` — request body for updates (if applicable)
- Follow `openapi.instructions.md` for schema naming, required fields, and security definitions.
- After editing, run: `mvn generate-sources -pl back-end` so the OpenAPI Maven plugin re-generates the API interface and DTOs.

---

### Step 3 — Create the Domain Model

File: `src/main/java/com/craftpg/domain/model/<Resource>.java`

- `@Entity`, `@Table(name = "<snake_case_table>")`, `@Getter`, `@NoArgsConstructor(access = AccessLevel.PROTECTED)`
- `@Id` UUID field, generated inside the `create()` factory using `Generators.timeBasedEpochGenerator().generate()`
- Static `create(@NonNull <Resource>CreateInput input)` factory method that validates all fields and returns a fully initialised entity
- Behaviour methods (e.g., `update()`, `deactivate()`) for any non-creation mutations
- Corresponding `Input` record: `src/main/java/com/craftpg/domain/input/<Resource>CreateInput.java`

Follow `domain.instructions.md`.

---

### Step 4 — Create the Repository

File: `src/main/java/com/craftpg/infrastructure/persistence/repository/<Resource>Repository.java`

```java
@Repository
public interface <Resource>Repository extends JpaRepository<<Resource>, UUID> {
    // add query methods derived from business rules if needed
}
```

---

### Step 5 — Create Use Case Interfaces

For each operation identified in Step 1, create an interface:

File: `src/main/java/com/craftpg/application/usecase/<domain>/<operationname>/<OperationName>Usecase.java`

- Single `execute()` method
- Parameters: UUIDs for path variables + request DTO (or void)
- Return type: domain entity or `void`

Follow `usecase.instructions.md`.

---

### Step 6 — Create Use Case Implementations

File: `src/main/java/com/craftpg/application/usecase/<domain>/<operationname>/<OperationName>UsecaseImpl.java`

- `@Service`, `@RequiredArgsConstructor`
- `@Transactional` on the `execute()` method
- Inject repository and any other dependencies as `final` fields
- Call the domain entity's factory / behaviour methods
- Add `@RequireCampaignPermission` when the business rule requires role-based access (DM / MODERATOR / PLAYER)
- Throw structured exceptions from `com.craftpg.infrastructure.exception` for validation failures

Follow `usecase.instructions.md`.

---

### Step 7 — Create the Mapper

File: `src/main/java/com/craftpg/application/mapper/<Resource>Mapper.java`

- `@Component`
- `toResponse(@NonNull final <Resource> entity)` → `<Resource>Response`
- `toCreateInput(@NonNull final Create<Resource>Request request)` → `<Resource>CreateInput`
- Add overloaded or additional mapper methods for any other DTOs

Follow `mapper.instructions.md`.

---

### Step 8 — Create the Controller

File: `src/main/java/com/craftpg/infrastructure/web/controller/<Resource>Controller.java`

- `@RestController`, `@RequiredArgsConstructor`
- Implements the OpenAPI-generated `<Resource>sApi` interface
- Each method delegates to a use case and maps the result via the mapper
- Returns the correct HTTP status code (`200`, `201`, `204`)

Follow `web.instructions.md`.

---

### Step 9 — Implement Step Definitions

File: `src/test/java/com/craftpg/features/steps/<Domain>ManagementSteps.java`

> Skip if the file already exists and covers all steps in the feature file.

- Extend `HttpStepSupport`
- Implement `@Given`, `@When`, `@Then` methods matching every step in the feature file
- Use `setRoute()`, `setPayloadTemplate()`, `setAuthenticated()`, `sendRequest()`, `getResponseStatus()`, `getResponseBody()` from `HttpStepSupport`
- For scenarios requiring pre-existing data (e.g., `Given an existing <resource> for the scenario`), create the resource via another HTTP call (POST) inside the step and store the returned id in an instance field, then substitute `{resourceId}` in subsequent path strings

Follow `cucumber.instructions.md`.

---

### Step 10 — Create Unit Tests for Each Use Case

File: `src/test/java/com/craftpg/application/usecase/<domain>/<operationname>/<OperationName>UsecaseImplTest.java`

- `@ExtendWith(MockitoExtension.class)`
- `@Mock` repository and dependencies, `@InjectMocks` implementation
- Cover: happy path, validation failure, not-found case, permission denial (if applicable)
- Naming convention: `methodName_scenario_expectedBehavior()`

Follow `test.instructions.md`.

---

## Checklist: Definition of Done

Before finishing, verify that each item is complete:

- [ ] `.feature` file exists with happy-path, negative, and edge-case scenarios
- [ ] OpenAPI spec updated and `mvn generate-sources` run successfully
- [ ] Domain entity created with factory method and `Input` record
- [ ] JPA repository created
- [ ] Use case interface(s) created (one per operation)
- [ ] Use case implementation(s) created with `@Transactional`
- [ ] Mapper created with `toResponse` and `toCreateInput`
- [ ] Controller created, implementing the OpenAPI interface
- [ ] Step definitions implemented and all Gherkin steps are bound
- [ ] Unit tests created for every use case (happy path + at least one error path)
- [ ] All tests pass: `mvn test -pl back-end`

---

## Conventions Reference

| Artifact              | Location template                                                                                                   |
|-----------------------|---------------------------------------------------------------------------------------------------------------------|
| Feature file          | `src/test/resources/features/<domain>/<domain>-management.feature`                                                 |
| OpenAPI spec          | `back-end/docs/api/openapi.yml`                                                                                     |
| Domain entity         | `src/main/java/com/craftpg/domain/model/<Resource>.java`                                                            |
| Domain input record   | `src/main/java/com/craftpg/domain/input/<Resource>CreateInput.java`                                                 |
| Repository            | `src/main/java/com/craftpg/infrastructure/persistence/repository/<Resource>Repository.java`                         |
| Usecase interface     | `src/main/java/com/craftpg/application/usecase/<domain>/<operation>/<OperationName>Usecase.java`                    |
| Usecase impl          | `src/main/java/com/craftpg/application/usecase/<domain>/<operation>/<OperationName>UsecaseImpl.java`                |
| Mapper                | `src/main/java/com/craftpg/application/mapper/<Resource>Mapper.java`                                                |
| Controller            | `src/main/java/com/craftpg/infrastructure/web/controller/<Resource>Controller.java`                                 |
| Step definitions      | `src/test/java/com/craftpg/features/steps/<Domain>ManagementSteps.java`                                             |
| Use case unit tests   | `src/test/java/com/craftpg/application/usecase/<domain>/<operation>/<OperationName>UsecaseImplTest.java`            |
