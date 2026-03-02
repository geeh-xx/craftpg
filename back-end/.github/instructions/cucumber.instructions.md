---
applyTo: 'src/test/resources/features/**/*.feature'
---

# Cucumber Feature File Instructions

## Purpose

Every feature delivered in this project **MUST** have a Cucumber `.feature` file written in Gherkin that captures the expected behavior before any production code is written (TDD / BDD).

## File Location

```
src/test/resources/features/
└── <domain>/
    └── <feature-name>.feature
```

Examples:
- `src/test/resources/features/invite/invite-acceptance.feature`
- `src/test/resources/features/campaign/campaign-permissions.feature`
- `src/test/resources/features/character/character-per-campaign.feature`

## Gherkin Conventions

### Feature Header

```gherkin
@<domain-tag>
Feature: <Feature Name in Title Case>
  As a <role>
  I want <goal>
  So that <benefit>
```

- Always include the three-line user story in the `Feature` description
- Use a domain tag on the `Feature` line (e.g., `@invite`, `@campaign`, `@character`)

### Background

Use `Background` for shared preconditions that apply to **all** scenarios in the file:

```gherkin
  Background:
    Given a registered user with email "player@example.com" exists
    And a campaign "Dragon Quest" is created by the dungeon master
```

### Entry Point
- Each scenario should start with a clear entry point e.g (api endpoint, kafka event, etc.):

```gherkin
  Scenario: Player accepts a valid invite
    Given the player has a valid invite token "abc-123"
    When When the endpoint is called through /v1/invite/accept/{token}
    Then the player becomes a member of the campaign
    And the character is locked to the campaign
```

### Scenarios

- Each `Scenario` must test **exactly one business rule or behavior**
- Use `Scenario Outline` + `Examples` table for parameterized / boundary cases
- Name scenarios with plain business language — avoid technical jargon
- Cover: happy path, error/rejection path, and edge cases

```gherkin
  Scenario: Player accepts a valid invite
    Given the player has a valid invite token "abc-123"
    When the player accepts the invite with character base id "char-001"
    Then the player becomes a member of the campaign
    And the character is locked to the campaign

  Scenario Outline: Invite acceptance fails for invalid tokens
    Given the player has an invite token "<token>"
    When the player attempts to accept the invite
    Then the system returns an error "<error>"

    Examples:
      | token       | error                  |
      | expired-tok | INVITE_EXPIRED         |
      | used-token  | INVITE_ALREADY_USED    |
      | bad-token   | INVITE_NOT_FOUND       |
```

### Step Phrasing

- **Given** – Establish preconditions / system state
- **When** – Perform the action under test
- **Then** – Assert the observable outcome
- **And / But** – Continue the previous step type (avoid mixing Given/When/Then in the same And)

### Tagging Strategy

| Tag             | When to use                                    |
| --------------- | ---------------------------------------------- |
| `@smoke`        | Critical path scenarios for quick validation   |
| `@regression`   | Full regression suite                          |
| `@integration`  | Scenarios requiring Spring context / DB        |
| `@negative`     | Scenarios that expect errors or rejections     |
| `@boundary`     | Edge cases and boundary-value scenarios        |
| `@wip`          | Work in progress — excluded from CI by default |

## Step Definition Requirements

For every `.feature` file, create a matching step definition class:

- **Location**: `src/test/java/com/craftpg/features/steps/<domain>/`
- **Class name**: `<Feature>Steps.java` (e.g., `InviteAcceptanceSteps.java`)
- **Annotations**: `@Given`, `@When`, `@Then` from `io.cucumber.java.en`
- Use `@Autowired` for Spring beans
- Share state between steps via instance fields (one instance per scenario)

## Cucumber Runner Requirements

Create a runner for each domain (or a single shared runner):

- **Location**: `src/test/java/com/craftpg/features/runner/`
- **Class name**: `CucumberRunner.java`

```java
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.craftpg")
@SpringBootTest
@CucumberContextConfiguration
public class CucumberRunner {}
```

## Required Maven Dependencies

Ensure the following are present in `pom.xml`:

```xml
<!-- Cucumber -->
<dependency>
  <groupId>io.cucumber</groupId>
  <artifactId>cucumber-java</artifactId>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>io.cucumber</groupId>
  <artifactId>cucumber-junit-platform-engine</artifactId>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>io.cucumber</groupId>
  <artifactId>cucumber-spring</artifactId>
  <scope>test</scope>
</dependency>
```

## Definition of Done for a Feature

A feature is **Done** only when:

- [ ] At least one Cucumber `.feature` file exists with scenarios covering the happy path
- [ ] Negative / error scenarios are covered
- [ ] All step definitions are implemented
- [ ] The Cucumber runner executes all scenarios successfully
- [ ] Unit tests (JUnit 5) cover the domain and use-case layer
- [ ] Code coverage ≥ 80%
- [ ] OpenAPI spec is updated
