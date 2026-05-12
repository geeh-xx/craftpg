---
description: 'Scaffold the complete backend structure from a Cucumber .feature file (OpenAPI, domain model, repository, use cases, mapper, controller, step definitions, unit tests)'
agent: 'agent'
tools: ['read', 'edit', 'create', 'search', 'execute']
---

Scaffold the full backend for the feature described in [${fileBasename}](${file}).

## Context

The feature file has been written first following the BDD (Behaviour-Driven Development) approach.
Now generate **all** backend artifacts needed to make the scenarios pass.

## Instructions

Follow every step defined in the BDD Backend Scaffold agent (`bdd-backend-scaffold.agent.md`):

1. **Analyse** the feature file — extract domain, resource name, HTTP routes, business rules, and required use cases.

2. **Update the OpenAPI spec** (`back-end/docs/api/openapi.yml`) — add paths and schema definitions for the new resource.  
   Then run: `mvn generate-sources -pl back-end` to regenerate the API interface and DTOs.

3. **Domain model** — create the JPA entity with a static `create()` factory method and its `Input` record.  
   Location: `src/main/java/com/craftpg/domain/model/` and `src/main/java/com/craftpg/domain/input/`

4. **Repository** — create the Spring Data JPA repository.  
   Location: `src/main/java/com/craftpg/infrastructure/persistence/repository/`

5. **Use case interfaces** — one interface per business operation.  
   Location: `src/main/java/com/craftpg/application/usecase/<domain>/<operation>/`

6. **Use case implementations** — `@Service`, `@RequiredArgsConstructor`, `@Transactional`.  
   Location: same package as the interface.

7. **Mapper** — `toResponse()` and `toCreateInput()` methods.  
   Location: `src/main/java/com/craftpg/application/mapper/`

8. **Controller** — thin controller that implements the generated OpenAPI interface and delegates to use cases.  
   Location: `src/main/java/com/craftpg/infrastructure/web/controller/`

9. **Step definitions** — implement every `@Given / @When / @Then` step from the feature file by extending `HttpStepSupport`.  
   Location: `src/test/java/com/craftpg/features/steps/`

10. **Unit tests** — JUnit 5 + Mockito tests for every use case (happy path + error paths).  
    Location: `src/test/java/com/craftpg/application/usecase/<domain>/<operation>/`

## Reference Files

- Layer conventions: `.github/instructions/*.instructions.md`
- Existing patterns: look at `CampaignController`, `CreateSessionUsecaseImpl`, `SessionMapper`, `SessionManagementSteps` as reference implementations
- Feature file location convention: `src/test/resources/features/<domain>/<domain>-management.feature`

## Finish

After generating all artifacts, run `mvn test -pl back-end` and confirm all scenarios pass.

If there is a selection, scaffold only for this scope:
${selection}
