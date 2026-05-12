# AGENTS.md — craftpg Back-end

> Single source of truth for any AI coding agent (Claude Code, GitHub Copilot, Cursor, Windsurf, Aider, etc.) working in the **back-end** module.
> Agent-specific files (`.github/copilot-instructions.md`, `CLAUDE.md`, `.cursorrules`) should defer to this document.

---

## Table of Contents

1. [Tech Stack](#1-tech-stack)
2. [Core Workflow — BDD → TDD → Implement](#2-core-workflow--bdd--tdd--implement)
3. [Definition of Done](#3-definition-of-done)
4. [Package Structure](#4-package-structure)
5. [Domain Layer](#5-domain-layer)
6. [Use Case Layer](#6-use-case-layer)
7. [Mapper Layer](#7-mapper-layer)
8. [Web Layer](#8-web-layer)
9. [Infrastructure Layer](#9-infrastructure-layer)
10. [API Design (OpenAPI-First)](#10-api-design-openapi-first)
11. [Database (Liquibase)](#11-database-liquibase)
12. [Testing Standards](#12-testing-standards)
13. [Messaging & Outbox Pattern](#13-messaging--outbox-pattern)
14. [Java Style & Best Practices](#14-java-style--best-practices)
15. [Code Quality & Security](#15-code-quality--security)
16. [Version Control](#16-version-control)
17. [Local Development](#17-local-development)
18. [Anti-Patterns](#18-anti-patterns)

---

## 1. Tech Stack

| Concern | Technology                                  |
|---|---------------------------------------------|
| Language | Java 25                                     |
| Framework | Spring Boot 3.5                             |
| Build tool | Maven                                       |
| Auth | Keycloak (OAuth2 / OIDC)                    |
| Database | PostgreSQL                                  |
| Migrations | Liquibase                                   |
| API contract | OpenAPI 3.0.3 (API-First, code-generated)   |
| Unit testing | JUnit 5 · Mockito · Instancio · AssertJ     |
| BDD testing | Cucumber / Gherkin · TestContainers (PostgreSQL) · RestAssured |
| Coverage | JaCoCo (≥ 80%)                              |

---

## 2. Core Workflow — BDD → TDD → Implement

**Every feature MUST follow this exact order. No exceptions.**

```
1. Write .feature file         (Gherkin / Cucumber — BDD-First)
         ↓
2. Write failing unit test     (Red)
         ↓
3. Write minimum production code to make the test pass  (Green)
         ↓
4. Refactor without breaking tests  (Refactor)
         ↓
5. Run full test suite — all green before committing
```

### Backend Scaffold Order

Generate artifacts in this sequence for every new feature:

1. Gherkin `.feature` file → `src/test/resources/features/<domain>/`
2. OpenAPI spec update → `docs/api/openapi.yml` → run `mvn generate-sources`
3. Input record → `com.craftpg.domain.input`
4. Typed ID class → `com.craftpg.domain.model.<domain>.<Entity>ID` (extends `AggregateTypedId`)
5. Domain entity → `com.craftpg.domain.model.<domain>` (extends `AggregateRoot<EntityID>`)
6. JPA Repository → `com.craftpg.infrastructure.persistence.repository`
7. **For CRUD**: domain management interface extending `UseCaseManagement` + its `Impl` → `com.craftpg.application.usecase.<domain>`
8. **For specialized operations**: individual use case interface + `Impl` → `com.craftpg.application.usecase.<domain>.<feature>`
9. Mapper → `com.craftpg.application.mapper`
10. Controller → `com.craftpg.infrastructure.web.controller`
11. Cucumber step definitions → `src/test/java/com/craftpg/features/steps/`
12. Unit tests mirroring each production class

---

## 3. Definition of Done

A feature is **Done** only when ALL of the following are true:

- [ ] Gherkin `.feature` file exists covering happy path and negative scenarios
- [ ] All Cucumber scenarios pass end-to-end
- [ ] Unit tests cover the domain and use-case layers (JUnit 5)
- [ ] Code coverage ≥ 80% (JaCoCo)
- [ ] OpenAPI spec is updated and `mvn generate-sources` was run
- [ ] No `@Autowired` — constructor injection only
- [ ] No `@Data` on entities
- [ ] No `@Builder` on entities — factory methods used instead
- [ ] No hardcoded secrets or credentials
- [ ] Liquibase migration added for every schema change
- [ ] `mvn verify` passes with no errors or warnings

---

## 4. Package Structure

```
com.craftpg
├── domain
│   ├── (root)              # Base hierarchy — DO NOT modify, only extend
│   ├── model
│   │   └── <domain>/       # One sub-package per domain (campaign, character, notification, user, ...)
│   │                       # Each sub-package contains the aggregate root entity,
│   │                       # its typed ID class, and related value objects / child entities
│   ├── event               # DomainEvent base interface + all domain event records
│   └── input               # Input records consumed by entity factory methods (Create<Entity>Input, ...)
│
├── application
│   ├── usecase
│   │   ├── (root)          # CRUD infrastructure — DO NOT modify, only use
│   │   │                   # UseCaseManagement, SecuredUseCase, OperationResult,
│   │   │                   # UseCaseOperationResultTypeEnum, UseCaseOutput, UseCaseFactory
│   │   └── <domain>/       # One sub-package per domain
│   │       ├── (root)      # <Domain>ManagementUseCase interface + Impl (CRUD via UseCaseManagement)
│   │       └── <feature>/  # One sub-package per specialized operation
│   │                       # <Feature>Usecase interface + Impl
│   ├── mapper              # One mapper class per domain entity
│   ├── listener            # Domain event listeners (@TransactionalEventListener)
│
├── infrastructure
│   ├── configuration       # Spring @Configuration classes (one concern per class)
│   ├── exception           # Custom exceptions (ApiException and domain-specific ones)
│   ├── factory             # UseCaseFactory and UseCaseProvider
│   ├── notification        # Notification implementations (e.g., SMTP)
│   ├── persistence
│   │   └── repository      # Spring Data JPA repositories (one per aggregate root)
│   ├── security            # Necessary Security components (e.g., JWT filter, Keycloak config)
│   ├── web
│   │    ├── client          # External HTTP clients
│   │    ├── controller      # Controllers — implement generated API interfaces
│   │    └── dto             # Generated DTOs — DO NOT edit manually
│   ├── messaging           
│   │    ├──producer         # Outbox message producer/dispatcher 
│   │    ├── consumer        # Messages listeners
│   │    └── dto             # Messages DTO 
│
└── shared
    ├── constants           # Application-wide constants and enums
    └── util                # Stateless utility helpers
```

---

## 5. Domain Layer

### Base Hierarchy (do not modify — extend only)

```
TypedId<T>  (interface)
└── AggregateTypedId  (abstract, implements TypedId<UUID>)
    └── CampaignID, CharacterBaseID, ...  (one per aggregate)

BaseEntity<ID extends TypedId<?>>  (abstract, @MappedSuperclass)
└── AggregateRoot<ID>  (abstract, @MappedSuperclass — holds domain events)
    └── Campaign, CharacterBase, Notification, ...  (concrete entities)

BaseDomain<ID>  (interface — getId() + getOwnerUserId())
```

### Typed ID — one per aggregate root

```java
// com.craftpg.domain.model.campaign.CampaignID
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampaignID extends AggregateTypedId {

    public CampaignID(UUID id) {
        super(id);
    }

    // Always use this to create a new ID
    public static CampaignID generate() {
        return new CampaignID(newUuid());  // newUuid() from AggregateTypedId
    }
}
```

- `@Embeddable` — mapped as `@EmbeddedId` inside the entity
- `newUuid()` (inherited from `AggregateTypedId`) uses `UUID.randomUUID()`
- To get the raw `UUID` value from an ID: `campaignId.getValue()`
- To unwrap generically: `campaignId.unwrap()`

### Entities

```java
// com.craftpg.domain.model.campaign.Campaign
@Entity
@Table(name = "campaign")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // required by JPA/Hibernate
public class Campaign extends AggregateRoot<CampaignID> {

    @EmbeddedId
    private CampaignID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String status;

    @Column(name = "create_by", nullable = false)
    private UUID createBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Static factory — the ONLY way to create a new instance
    public static Campaign create(@NonNull final CreateCampaignInput input) {
        var campaign = new Campaign();
        var now = LocalDateTime.now();
        campaign.id = CampaignID.generate();
        campaign.title = input.title();
        campaign.status = input.status();
        campaign.createBy = input.createBy();
        campaign.createdAt = now;
        campaign.updatedAt = now;
        // registerEvent(...) after state is fully set, if domain event needed
        return campaign;
    }

    // Behavior methods encapsulate business logic — no setters
    public void finish() {
        this.status = "finished";
        this.updatedAt = LocalDateTime.now();
        // registerEvent(new CampaignFinishedEvent(...));
    }
}
```

Entity rules:
- Extend `AggregateRoot<EntityID>` — **never Spring's `AbstractAggregateRoot`**
- ID field: `@EmbeddedId private EntityID id` — never a plain `UUID` with `@Id`
- `@Getter` only — **never `@Setter`**, **never `@Builder`**, **never `@Data`**
- `@NoArgsConstructor(access = AccessLevel.PROTECTED)` — JPA requirement
- `static create(Input)` is the **only** way to construct a new entity
- `registerEvent()` is called **after** the state change, **before** `return`
- Throw `IllegalArgumentException` for invalid inputs in the factory
- Throw `IllegalStateException` for invalid state transitions in behavior methods

### Input Records

```java
// com.craftpg.domain.input.CreateCampaignInput
public record CreateCampaignInput(
    @NonNull UUID createBy,
    @NonNull String title,
    String description,
    @NonNull String frequency,
    @NonNull String status,
    @NonNull Integer progressPercent
) {}
```

- One input record per factory method, located in `com.craftpg.domain.input`
- Naming: `Create<Entity>Input`, `Update<Entity>Input`
- Use `@NonNull` (from `lombok.NonNull`) for mandatory fields

### Domain Events

```java
// com.craftpg.domain.event — implements DomainEvent<T>
public record CampaignFinishedEvent(UUID campaignId) implements DomainEvent<UUID> {}
```

- Naming: `<Entity><Action>Event`
- Must implement `DomainEvent<T>` (the base interface in `com.craftpg.domain.event`)
- Contain only the data needed by listeners
- Registered inside the entity via `registerEvent()` after a successful state change

### JPA Relationships

- `FetchType.LAZY` for all collections
- `CascadeType.ALL` for child entities owned by the aggregate
- `orphanRemoval = true` when removing a child from the collection must delete it

---

## 6. Use Case Layer

There are two patterns. Choose based on the operation type.

---

### Pattern A — CRUD via `UseCaseManagement` (standard for domain entities)

Use this for any domain entity that needs create / update / findById / findAll / delete.

**Step 1 — Domain interface** (`com.craftpg.application.usecase.<domain>`):

```java
// CampaignManagementUseCase.java
public interface CampaignManagementUseCase
    extends UseCaseManagement<OperationResult<?>, CreateCampaignRequest, UpdateCampaignRequest, Pageable, UUID> {
}
```

Generic signature: `UseCaseManagement<OUT extends OperationResult<?>, CI, UI, PI, ID>`
- `CI` = Create Input type (generated DTO, e.g. `CreateCampaignRequest`)
- `UI` = Update Input type (generated DTO, e.g. `UpdateCampaignRequest`)
- `PI` = Pageable type (always `org.springframework.data.domain.Pageable`)
- `ID` = ID type (always `UUID`)
- `OUT` = always `OperationResult<?>`

**Step 2 — Implementation** (`com.craftpg.application.usecase.<domain>`):

```java
// CampaignManagementUseCaseImpl.java
@Service
@RequiredArgsConstructor
public class CampaignManagementUseCaseImpl implements CampaignManagementUseCase {

    private final CampaignRepository campaignRepository;
    private final CurrentUserProvider currentUserProvider;
    private final CampaignMapper campaignMapper;

    @Override
    @Transactional
    public @NonNull OperationResult<CampaignResponse> create(@NonNull final CreateCampaignRequest request) {
        var userId = currentUserProvider.getCurrentUserId();
        var campaign = campaignRepository.save(
            Campaign.create(campaignMapper.toCreateInput(request, userId))
        );
        return OperationResult.ok(campaignMapper.toResponse(campaign));
    }

    @Override
    @Transactional
    public @NonNull OperationResult<CampaignResponse> update(@NonNull final UpdateCampaignRequest request) {
        var campaign = campaignRepository.findById(request.getCampaignId());
        if (campaign.isEmpty()) {
            return OperationResult.failure("campaign not found");
        }
        campaign.get().update(request.getTitle(), request.getDescription(), ...);
        return OperationResult.ok(campaignMapper.toResponse(campaignRepository.save(campaign.get())));
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull OperationResult<CampaignResponse> findById(@NonNull final UUID id) {
        return campaignRepository.findById(id)
            .map(campaignMapper::toResponse)
            .map(OperationResult::ok)
            .orElse(OperationResult.failure("campaign not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull OperationResult<Page<CampaignResponse>> findAll(@NonNull final Pageable pageable) {
        var userId = currentUserProvider.getCurrentUserId();
        var page = campaignRepository.findPageByUserId(userId, pageable).map(campaignMapper::toResponse);
        return OperationResult.ok(page);
    }

    @Override
    @Transactional
    public @NonNull OperationResult<Void> delete(@NonNull final UUID id) {
        if (!campaignRepository.existsById(id)) {
            return OperationResult.failure("campaign not found");
        }
        campaignRepository.deleteById(id);
        return OperationResult.ok();
    }
}
```

**`OperationResult<T>` — the only allowed return type for management use cases:**

| Factory | When to use |
|---|---|
| `OperationResult.ok(value)` | Successful operation with a result body |
| `OperationResult.ok()` | Successful operation with no body (e.g. DELETE) |
| `OperationResult.failure(message)` | Expected business failure (not found, forbidden, …) |
| `OperationResult.error(message)` | Unexpected / infrastructure error |

Check result in callers: `result.isNotSuccess()` returns `true` for both `failure` and `error`.

---

### Pattern B — Specialized use case (non-CRUD, single operation)

Use this for operations that do not fit the CRUD pattern (e.g., `FinishCampaign`, `AcceptInvite`, `GenerateRandomCharacter`).

**Interface** (`com.craftpg.application.usecase.<domain>.<feature>`):

```java
// com.craftpg.application.usecase.campaign.finishcampaign.FinishCampaignUsecase
public interface FinishCampaignUsecase {
    OperationResult<CampaignResponse> execute(@NonNull UUID campaignId);
}
```

**Implementation**:

```java
// FinishCampaignUsecaseImpl.java
@Service
@RequiredArgsConstructor
public class FinishCampaignUsecaseImpl implements FinishCampaignUsecase {

    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;

    @Override
    @Transactional
    public OperationResult<CampaignResponse> execute(@NonNull final UUID campaignId) {
        var optional = campaignRepository.findById(campaignId);
        if (optional.isEmpty()) {
            return OperationResult.failure("campaign not found");
        }
        var campaign = optional.get();
        campaign.finish();
        return OperationResult.ok(campaignMapper.toResponse(campaignRepository.save(campaign)));
    }
}
```

---

### Common Rules for Both Patterns

- `@Service` + `@RequiredArgsConstructor` — no `@Autowired`, all dependencies `final`
- `@Transactional` on the implementation method — **never on the interface**
- `@Transactional(readOnly = true)` for query-only operations
- Return `OperationResult` — never raw entities or `Optional` directly to callers
- Never throw raw `RuntimeException` — use domain-specific exceptions when truly exceptional

---

## 7. Mapper Layer

```java
@Component
public class CampaignMapper {

    public CampaignResponse toResponse(@NonNull final Campaign campaign) {
        var response = new CampaignResponse();
        response.setId(campaign.getId());
        response.setName(campaign.getName());
        return response;
    }

    public CreateCampaignCommand toCommand(@NonNull final CreateCampaignRequest request) {
        return new CreateCampaignCommand(request.getName(), request.getOwnerId());
    }
}
```

- One mapper per domain entity
- Methods: `toResponse()`, `toCommand()`, `toEntity()` (when needed)
- **No business logic** — pure data transformation only
- `@NonNull` on all required parameters
- `final` keyword on immutable parameters

---

## 8. Web Layer

### Controllers

```java
@RestController
@RequiredArgsConstructor
@Slf4j
public class CampaignController implements CampaignApi {

    private final CreateCampaignUseCase createCampaignUseCase;
    private final CampaignMapper campaignMapper;

    @Override
    public ResponseEntity<CampaignResponse> createCampaign(@Valid CreateCampaignRequest request) {
        var command = campaignMapper.toCommand(request);
        var campaign = createCampaignUseCase.execute(command);
        var response = campaignMapper.toResponse(campaign);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

- Implement the generated OpenAPI interface from `com.craftpg.infrastructure.web.api`
- Keep thin: validate (`@Valid`) → call use case → map → return `ResponseEntity`
- **Do not** manually edit generated API interfaces or DTOs — edit the OpenAPI spec and regenerate

### HTTP Status Codes

| Scenario | Code |
|---|---|
| Successful GET / PUT / PATCH | 200 |
| Successful POST (resource created) | 201 |
| Successful DELETE | 204 |
| Validation error | 400 |
| Not authenticated | 401 |
| Forbidden / insufficient permission | 403 |
| Resource not found | 404 |
| Unexpected server error | 500 |

### Error Handling

- Use `@ControllerAdvice` for global exception handling
- Return a consistent error response structure
- Never expose stack traces or sensitive details in error responses

---

## 9. Infrastructure Layer

### Configuration Classes

- Located in `com.craftpg.infrastructure.config`
- `@Configuration` + `@Bean` — one concern per class (e.g., `SecurityConfig`, `DatabaseConfig`)

### Exception Hierarchy

```
RuntimeException
└── SecurityException        (com.craftpg.infrastructure.exception.security)
    └── InvalidCredentialsException
└── ValidationException      (com.craftpg.infrastructure.exception.validation)
└── DataException            (com.craftpg.infrastructure.exception.data)
```

Every custom exception requires:
- `@Serial` annotation
- `private static final long serialVersionUID = 1L;`
- A constructor accepting a descriptive message

### Security

- Located in `com.craftpg.infrastructure.security`
- JWT tokens validated server-side on every protected request
- **Never hardcode** credentials, secrets, or API keys — load from environment variables
- Follow Spring Security best practices

---

## 10. API Design (OpenAPI-First)

**Design the API spec before writing any production code.**

- Spec: `docs/api/openapi.yml` (OpenAPI 3.0.3)
- After any spec change: `mvn generate-sources`
- Generated artifacts live in `com.craftpg.infrastructure.web.api` and `.dto` — **never edit them manually**

### Spec Rules

- Path versioning: `/v1/`, `/v2/`
- Resource names: plural nouns (`/v1/campaigns`, `/v1/characters`)
- All schemas: `description`, `type`, `format` (use `uuid`, `date-time`, `email` where applicable)
- Mandatory fields listed in the `required:` array
- Every endpoint documents all possible responses (success + errors)
- Security scheme defined in `components/securitySchemes`
- Realistic examples in all request/response bodies
- Pagination for list endpoints (`page`, `size`, `sort` query params)

---

## 11. Database (Liquibase)

- Changelogs: `src/main/resources/db/changelog/<domain>/`
- Master: `src/main/resources/db/changelog/db.changelog-master.yaml`
- File naming: `<date>-<domain>-<description>.sql` (e.g., `2026-03-12-campaigns-add-status.sql`)
- **Never modify an existing migration** — always create a new file
- One migration per concern per domain sub-folder

---

## 12. Testing Standards

### Unit Tests (JUnit 5 + Mockito + Instancio)

```java
@ExtendWith(MockitoExtension.class)
class CreateCampaignUseCaseImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private CreateCampaignUseCaseImpl useCase;

    @Test
    void execute_validCommand_createsCampaignSuccessfully() {
        // Given
        var command = Instancio.create(CreateCampaignCommand.class);
        var expected = Instancio.create(Campaign.class);
        when(campaignRepository.save(any())).thenReturn(expected);

        // When
        var result = useCase.execute(command);

        // Then
        assertThat(result).isNotNull();
        verify(campaignRepository).save(any(Campaign.class));
    }

    @Test
    void execute_nullCommand_throwsException() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
    }
}
```

- Test classes mirror production package structure
- Naming: `methodName_scenario_expectedBehavior()`
- No Spring context in unit tests — `@ExtendWith(MockitoExtension.class)` only
- Use Instancio for test data generation; customize only fields relevant to the test
- Use `ArgumentCaptor` to verify argument values passed to mocks
- Test both happy paths and error scenarios; test all business logic branches
- Minimum coverage: **80%** (enforced by JaCoCo)

### Parameterized Tests

Use `@ParameterizedTest` + `@MethodSource` / `@CsvSource` / `@EnumSource` for boundary and multi-case scenarios.

### Cucumber / BDD Tests

#### Infrastructure

Cucumber integration tests use **TestContainers** (real PostgreSQL) and **RestAssured** for HTTP calls. The application boots on a random port (`RANDOM_PORT`) and RestAssured makes **real TCP connections** — no MockMvc, no in-process request dispatch.

```
ContainerTestConfig  (abstract — shared base)
├── starts PostgreSQLContainer (postgres:16-alpine) as a static field
├── wires datasource via @DynamicPropertySource
├── activates @ActiveProfiles("cucumber")
├── WebEnvironment.RANDOM_PORT  ← real HTTP server, real TCP
└── RestAssured.port wired to the random port in @BeforeEach

CucumberSpringConfiguration extends ContainerTestConfig
├── @CucumberContextConfiguration  ← Spring context entry point for Cucumber
└── @MockitoBean AuthServiceClient ← cuts external Keycloak admin calls only

CucumberRunner extends ContainerTestConfig
├── @Suite + @IncludeEngines("cucumber")
├── @SelectClasspathResource("features")
└── @ConfigurationParameter GLUE = "com.craftpg.features.steps, com.craftpg.configuration"
```

JWT authentication uses a **static RSA key pair** configured in `application-cucumber.yml` — the real Spring Security filter chain runs and validates the token, no mocking of security components:

```yaml
# application-cucumber.yml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          public-key-location: classpath:test-keys/public.pem
```

`HttpStepSupport` generates a signed test JWT using the matching private key and passes it as `Authorization: Bearer <token>`.

**`ContainerTestConfig`** — do not duplicate, extend it:

```java
@SpringBootTest(classes = { CraftpgApplication.class },
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("cucumber")
public abstract class ContainerTestConfig {

    @LocalServerPort
    protected int port;

    @BeforeEach
    void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port    = port;
        RestAssured.basePath = "";
    }

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
        new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("craftpg")
            .withUsername("craftpg")
            .withPassword("craftpg");

    static { POSTGRES.start(); }

    @DynamicPropertySource
    static void registerDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",      POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.liquibase.enabled",   () -> "true");
        // other app properties ...
    }
}
```

#### HTTP calls — `HttpStepSupport`

All step classes that make HTTP calls **must extend `HttpStepSupport`**. It wraps RestAssured and exposes a simple API to step definitions:

```java
// HttpStepSupport provides:
setRoute(String method, String path);      // e.g. "POST", "/campaigns"
setPayloadTemplate(String template);       // maps to src/test/resources/data/<template>.json
setAuthenticated(boolean authenticated);   // adds Bearer JWT header to the request
sendRequest();                             // executes the RestAssured call
getResponseStatus();                       // HTTP status code
getResponseBody();                         // response body as String
getResponse();                             // raw Response for JsonPath assertions
hasPayloadTemplateConfigured();            // guards against missing payload
```

Authentication uses a real JWT signed with the test RSA private key (`test-keys/private.pem`). The token is built once per test class and passed in the `Authorization: Bearer` header. The running server's security filter chain validates it against the matching public key — the full auth path executes:

```java
// HttpStepSupport builds the request internally when authenticated = true:
given()
    .header("Authorization", "Bearer " + buildTestJwt())   // signed with test RSA private key
    .contentType(ContentType.JSON)
    .body(payload)
.when()
    .request(method, path)
.then()
    .extract().response();
```

#### Request payload files

Payloads are JSON files read from the test classpath:

```
src/test/resources/data/<domain>/<payload-name>.json
```

Reference them in step definitions via `setPayloadTemplate("campaign/create-campaign-valid")`.
Pass `"none"` (or call `setPayloadTemplate("none")`) for requests without a body (GET, DELETE).

#### Step class structure

```java
// com.craftpg.features.steps.CampaignManagementSteps
public class CampaignManagementSteps extends HttpStepSupport {

    private String createdCampaignId;

    @Given("the campaign API route {string} {string}")
    public void theCampaignApiRoute(String method, String path) {
        setAuthenticated(true);
        setRoute(method, path);
    }

    @Given("the campaign request payload template is {string}")
    public void theCampaignRequestPayloadTemplate(String template) {
        setPayloadTemplate(template);
    }

    @When("the campaign client sends the HTTP request")
    public void theCampaignClientSendsTheHttpRequest() {
        sendRequest();
    }

    @Then("the campaign response status is {int}")
    public void theCampaignResponseStatusIs(Integer expectedStatus) {
        assertThat(getResponseStatus()).isEqualTo(expectedStatus);
    }

    @Then("the campaign id is returned in the create response")
    public void theCampaignIdIsReturnedInTheCreateResponse() {
        createdCampaignId = getResponse().jsonPath().getString("id");
        assertThat(createdCampaignId).isNotBlank();
    }
}
```

Rules for step classes:
- Extend `HttpStepSupport` to get RestAssured support
- Annotate with `@Given`, `@When`, `@Then` from `io.cucumber.java.en`
- State shared via instance fields (Cucumber creates one instance per scenario)
- Step method names mirror Gherkin text in camelCase — **not** the `method_scenario_expected` JUnit convention
- Use `@Autowired` for any additional Spring beans

#### Feature file

```gherkin
@campaign
Feature: Campaign Management
  As a dungeon master
  I want to create and manage campaigns
  So that I can organize RPG sessions

  @smoke @integration
  Scenario: Dungeon master creates a campaign successfully
    Given the campaign API route "POST" "/campaigns"
    And the campaign request payload template is "campaign/create-campaign-valid"
    When the campaign client sends the HTTP request
    Then the campaign response status is 201
    And the campaign id is returned in the create response

  @negative
  Scenario: Campaign creation fails when title is missing
    Given the campaign API route "POST" "/campaigns"
    And the campaign request payload template is "campaign/create-campaign-no-title"
    When the campaign client sends the HTTP request
    Then the campaign response status is 400
```

#### Tagging strategy

| Tag | When to use |
|---|---|
| `@smoke` | Critical happy-path, always run in CI |
| `@regression` | Full regression suite |
| `@integration` | Requires Spring context or real DB |
| `@negative` | Expected error / rejection scenarios |
| `@boundary` | Edge cases and boundary values |
| `@wip` | Work in progress — excluded from CI by default |
| `@<domain>` | Domain tag: `@campaign`, `@invite`, `@character` |

---

## 13. Messaging & Outbox Pattern

All asynchronous messaging follows the **Transactional Outbox Pattern**: the event is persisted to a database table inside the same transaction as the business operation, and a separate scheduled processor is responsible for dispatching it to the message broker. This guarantees at-least-once delivery without distributed transactions.

### Flow

```
Use Case / Event Listener
        │
        │  (same DB transaction)
        ▼
  MessagePublisher  ──saves──▶  outbox_message table  (status = PENDING)
                                         │
                          (different transaction, scheduled)
                                         │
                                  OutboxProcessor
                                         │
                          reads PENDING in batches (Pageable)
                                         │
                           sends to message broker
                                         │
                          marks as SENT (or FAILED + retry)
```

### MessagePublisher

Interface in `application.notification` (or `infrastructure.messaging.producer`).
The implementation saves an `OutboxMessage` row **inside the caller's transaction** — if the business operation rolls back, the message is never persisted.

```java
// interface
public interface MessagePublisher {
    void publish(@NonNull String aggregateType,
                 @NonNull String aggregateId,
                 @NonNull String eventType,
                 @NonNull String payload);
}

// implementation — infrastructure.messaging.producer
@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxMessagePublisher implements MessagePublisher {

    private final OutboxMessageRepository outboxMessageRepository;

    @Override
    public void publish(@NonNull final String aggregateType,
                        @NonNull final String aggregateId,
                        @NonNull final String eventType,
                        @NonNull final String payload) {

        var message = OutboxMessage.create(aggregateType, aggregateId, eventType, payload);
        outboxMessageRepository.save(message);
        log.debug("Outbox message queued type={} aggregateId={}", eventType, aggregateId);
    }
}
```

Rules:
- **Always** call `MessagePublisher` inside a `@Transactional` method — never outside one
- Never call the broker directly from a use case or event listener
- The publisher has no knowledge of the broker; it only writes to the database

### OutboxProcessor

Located in `infrastructure.messaging.producer`. Polls the `outbox_message` table on a fixed schedule and dispatches pending messages to the broker.

`@SchedulerLock` (ShedLock) prevents concurrent execution across multiple application nodes — the lock is stored in the `shedlock` table in the same PostgreSQL database.

```java
// infrastructure.messaging.producer
@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxProcessor {

    private final OutboxMessageRepository outboxMessageRepository;
    private final BrokerGateway brokerGateway;          // abstraction over Kafka/RabbitMQ/etc.

    @Scheduled(fixedDelayString = "${messaging.outbox.poll-delay-ms:5000}")
    @SchedulerLock(
        name = "outbox-processor",
        lockAtLeastFor = "PT4S",
        lockAtMostFor  = "PT30S"
    )
    @Transactional
    public void process() {

        var pending = outboxMessageRepository.findByStatus(
            OutboxMessageStatus.PENDING,
            PageRequest.of(0, 50)
        );

        pending.forEach(message -> {

            try {
                brokerGateway.send(message);
                message.markSent();
            } catch (Exception ex) {
                log.error("Failed to dispatch outbox message id={}", message.getId(), ex);
                message.markFailed();
            }

            outboxMessageRepository.save(message);
        });
    }
}
```

Rules:
- `lockAtLeastFor` must be ≥ the expected processing time to avoid overlapping runs
- `lockAtMostFor` is the safety ceiling — the lock is released even if the node crashes
- Process messages in **small batches** (e.g. 50) to keep transaction time short
- Mark each message individually as SENT or FAILED — never batch-update status blindly
- `BrokerGateway` is an interface; the concrete implementation (Kafka, RabbitMQ, etc.) lives in `infrastructure.messaging.producer`

### Enabling ShedLock

Add to your `@SpringBootApplication` class (or a `@Configuration`):

```java
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
```

---

## 14. Java Style & Best Practices

### Language Features

- Java 25: Records, pattern matching, `var`, Streams, `Optional`, sealed classes
- Use `Optional<T>` instead of returning `null`
- Use `var` when the type is obvious from the right-hand side
- Prefer immutable objects: `final` fields, `List.of()`, `Map.of()`, `Stream.toList()`
- Streams and lambdas for collection processing; prefer method references

### Lombok

| Annotation | Use |
|---|---|
| `@Getter` | Read-only access on entities |
| `@RequiredArgsConstructor` | Constructor injection |
| `@Slf4j` | Logging |
| `@Builder` | DTOs and Command records (not entities) |
| `@AllArgsConstructor` | When needed alongside `@Builder` |
| `@NoArgsConstructor` | JPA entities (use `AccessLevel.PROTECTED`) |
| **`@Data`** | **Never — too broad, enables unwanted mutation** |
| **`@Setter`** | **Never on entities** |

### Dependency Injection

- **Always** constructor injection — `@RequiredArgsConstructor` generates it
- **Never** `@Autowired` on fields or methods
- All injected fields must be `final`

### Logging

```java
log.info("Creating campaign name={} ownerId={}", command.name(), command.ownerId());
log.error("Failed to create campaign name={}", command.name(), exception);
```

- `@Slf4j` — never `System.out.println`
- `debug` for detailed tracing; `info` for significant operations; `warn`/`error` for problems
- Include contextual data in log messages (domain identifiers, relevant field values)

### Code Layout

- Blank line between: methods, if-else blocks, try-catch blocks, class sections
- Follow Google Java Style Guide
- Naming: `PascalCase` for classes/interfaces, `camelCase` for methods/variables, `UPPER_SNAKE_CASE` for constants
- Wildcard imports for packages with ≥ 3 classes

---

## 15. Code Quality & Security

### SOLID Principles

- **S** — Single Responsibility: one reason to change per class
- **O** — Open/Closed: extend via new classes, not by modifying existing ones
- **L** — Liskov Substitution: subtypes must be substitutable
- **I** — Interface Segregation: small, focused interfaces per use case
- **D** — Dependency Inversion: depend on abstractions (interfaces), not concretions

### Code Smells to Eliminate

| Rule | Description | Fix |
|---|---|---|
| S107 | Too many method parameters | Introduce a Command/record |
| S138 | Method too long | Extract to smaller units |
| S3776 | High cognitive complexity | Flatten with guard clauses |
| S1192 | Duplicate string literals | Named constants or enums |
| S109 | Magic numbers | Named constants |
| S1188 | Empty catch blocks | Always log or handle |
| S1698 | `==` for object comparison | `.equals()` |

### Security Requirements

- **Never** hardcode credentials, secrets, or tokens
- Load secrets from environment variables or a secrets manager
- Validate all input at system boundaries (`@Valid`, domain factory methods)
- JWT tokens must be validated server-side on every protected request
- Use domain-specific exceptions — never expose internal details in API error responses
- Follow Spring Security best practices for Keycloak integration

---

## 16. Version Control

### Branch Naming

```
feature/<short-description>
fix/<short-description>
chore/<short-description>
refactor/<short-description>
test/<short-description>
```

### Commit Messages (Conventional Commits)

```
<type>(<scope>): <short summary>

Types : feat | fix | test | refactor | chore | docs | ci
Scopes: campaign | character | invite | auth | infra | db

Examples:
  feat(campaign): add archive campaign use case
  fix(invite): prevent duplicate invite tokens
  test(campaign): add cucumber scenarios for campaign lifecycle
  chore(deps): upgrade spring-boot to 3.5.5
```

### Pull Request Rules

- All tests must pass before requesting review
- Use rebase strategy — no merge commits
- Squash fixup commits before review
- PR description must reference the `.feature` file or ticket

---

## 17. Local Development

```bash
# Start PostgreSQL + Keycloak + MailHog
make infra-up          # or: docker-compose up -d
make infra-down
make infra-reset       # removes volumes (destructive)

# Build & test
make install           # mvn clean install
make unit-test         # mvn test
make coverage          # mvn test jacoco:report → target/site/jacoco/index.html

# Run the application
make run               # mvn spring-boot:run
make integration-test. # run cumcumber 
```

- Local config: `src/main/resources/application-local.yml` (gitignored)
- **Never commit** `.env` files or any file containing credentials

---

## 18. Anti-Patterns

| Never do this | Do this instead |
|---|---|
| `@Autowired` on fields | Constructor injection + `@RequiredArgsConstructor` |
| `@Data` on entities | `@Getter` only; behavior methods for mutation |
| `@Builder` on entities | `static create(Input)` factory method |
| `@Id UUID id` on aggregate roots | `@EmbeddedId EntityID id` with typed ID class |
| Raw `UUID.randomUUID()` in entity | `EntityID.generate()` via `AggregateTypedId.newUuid()` |
| `extends AbstractAggregateRoot` (Spring) | `extends AggregateRoot<EntityID>` (project base class) |
| Input records in `application.mapper.command` | Input records in `com.craftpg.domain.input` |
| CRUD operations as separate use cases | Single `<Domain>ManagementUseCase` extending `UseCaseManagement` |
| Returning raw entities from use cases | Wrap in `OperationResult.ok(value)` |
| Throwing exceptions for "not found" in management use cases | Return `OperationResult.failure(message)` |
| Raw `RuntimeException` | Domain-specific exceptions for truly unexpected errors |
| HTTP calls in step classes without `HttpStepSupport` | Extend `HttpStepSupport` |
| MockMvc in Cucumber tests | RestAssured with `RANDOM_PORT` — real TCP, real security filter chain |
| Mocking `JwtDecoder` / Spring Security in Cucumber tests | Use a static test RSA key pair; let the real security chain validate tokens |
| Mocking the database in Cucumber tests | Let TestContainers provide a real PostgreSQL |
| Instantiating `ContainerTestConfig` directly | Extend it (step config or runner) |
| Hardcoding JWT tokens in step classes | Use `setAuthenticated(true)` from `HttpStepSupport` |
| Production code without a test | Red → Green → Refactor |
| Modify existing Liquibase migrations | Create a new migration file |
| Edit generated API interfaces or DTOs | Edit the OpenAPI spec and run `mvn generate-sources` |
| Business logic in controllers | Delegate to use cases |
| Business logic in mappers | Pure data transformation only |
| `System.out.println` | `@Slf4j` logging |
| `@Transactional` on the interface | `@Transactional` on the implementation method |
| Returning or passing `null` | `Optional<T>` or `@NonNull` guard |
| Calling broker directly from a use case | Save to `outbox_message` via `MessagePublisher` first |
| Sending to broker inside the business DB transaction | `OutboxProcessor` handles dispatch in a separate transaction |
| `@Scheduled` without `@SchedulerLock` in multi-node deploy | Always pair with `@SchedulerLock` (ShedLock) |
| `OutboxMessage` extending `AggregateRoot` | It is an infrastructure entity — plain `@Entity` with `@Id UUID` |

---

> **Rule of thumb**: Write the `.feature` file first, then the test, then the minimum code to make it pass. Stay within layer boundaries. Ask before making cross-cutting changes.
