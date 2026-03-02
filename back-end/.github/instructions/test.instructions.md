---
applyTo: "src/test/java/**/*.java"
---

# Test Instructions

## Test-Driven Development (TDD) — Mandatory

**All tests MUST be written BEFORE the production code (Red → Green → Refactor):**

1. **Red** – Write a failing test describing one specific behavior
2. **Green** – Write the minimum production code to make the test pass
3. **Refactor** – Improve design without altering behavior; ensure tests still pass

- Tests are the specification; production code exists only to satisfy them
- One behavior per test — keep tests small and focused
- After each Red → Green cycle, run the full test suite to catch regressions


## Test Framework

- Use JUnit 5 (`@Test`, `@BeforeEach`, `@AfterEach`)
- Use Mockito for mocking (`@Mock`, `@InjectMocks`, `@ExtendWith(MockitoExtension.class)`)
- Use Instancio for test data generation
- Test classes should mirror the production code structure
- Write unit tests for every feature
- Minimum code coverage: 80% (enforced by JaCoCo)
- Test class naming: `<ClassName>Test` (e.g., `UserServiceTest`, `AuthControllerTest`)

## Test conventions
* Write clear, focused tests that verify one behavior at a time
* Use descriptive test names that explain what is being tested and the expected outcome
* Follow Given-When-Then pattern: set up test data, execute the code under test, verify results
* Keep tests independent - each test should run in isolation without depending on other tests
* Start with the simplest test case, then add edge cases and error conditions
* Tests should fail for the right reason - verify they catch the bugs they're meant to catch
* Mock external dependencies to keep tests fast and reliable

## Test Structure

- Use Given-When-Then  pattern
- Organize tests with clear sections:
  ```java
  // Given
  // ... setup test data
  
  // When
  // ... execute the method under test
  
  // Then
  // ... verify the results
  ```

## Test Naming

- Test method naming: `methodName_scenario_expectedBehavior()`
- Examples:
  - `create_validCommand_createsUserSuccessfully()`
  - `verifyEmail_alreadyVerified_throwsIllegalStateException()`
  - `login_invalidCredentials_throwsAuthenticationException()`

## Mocking Guidelines

- Mock external dependencies (repositories, services, clients)
- Use a mocking framework like Mockito to create mock objects for dependencies.
- Use `when().thenReturn()` for stubbing method calls
- Use `verify()` to assert method invocations
- Use `ArgumentCaptor` when you need to verify argument values
- NEVER mock the class under test

## Test Data Generation

- Use Instancio for generating test data
- Example:
  ```java
  User user = Instancio.create(User.class);
  ```
- Customize specific fields when needed:
  ```java
  User user = Instancio.of(User.class)
      .set(field(User::getEmail), "test@example.com")
      .create();
  ```

## Assertions

- Use the static methods from `org.junit.jupiter.api.Assertions` (e.g., `assertEquals`, `assertTrue`, `assertNotNull`).
- For more fluent and readable assertions, consider using a library like AssertJ (`assertThat(...).is...`).
- Use `assertThrows` or `assertDoesNotThrow` to test for exceptions.
- Group related assertions with `assertAll` to ensure all assertions are checked before the test fails.
- Use descriptive messages in assertions to provide clarity on failure.


## Data-Driven (Parameterized) Tests

- Use `@ParameterizedTest` to mark a method as a parameterized test.
- Use `@ValueSource` for simple literal values (strings, ints, etc.).
- Use `@MethodSource` to refer to a factory method that provides test arguments as a `Stream`, `Collection`, etc.
- Use `@CsvSource` for inline comma-separated values.
- Use `@CsvFileSource` to use a CSV file from the classpath.
- Use `@EnumSource` to use enum constants.

## Test Organization

- Group tests by feature or component using packages.
- Use `@Tag` to categorize tests (e.g., `@Tag("fast")`, `@Tag("integration")`).
- Use `@TestMethodOrder(MethodOrderer.OrderAnnotation.class)` and `@Order` to control test execution order when strictly necessary.
- Use `@Disabled` to temporarily skip a test method or class, providing a reason.
- Use `@Nested` to group tests in a nested inner class for better organization and structure.

## Coverage Requirements

- Minimum 80% code coverage (enforced by JaCoCo)
- Test both happy paths and error scenarios
- Test boundary conditions
- Test all business logic branches

## Cucumber / BDD Tests — Mandatory per Feature

**Every new feature MUST have a corresponding Cucumber `.feature` file.**

### Setup

- Feature files: `src/test/resources/features/<domain>/<feature>.feature`
- Data files: `src/test/resources/data/<domain>/<data>.json` (if needed)
- Step definitions: `src/test/java/com/craftpg/features/steps/<domain>/<Feature>Steps.java`
- Runner classes: `src/test/java/com/craftpg/features/runner/<Domain>CucumberRunner.java`
- Use `@CucumberContextConfiguration` + `@SpringBootTest` on the runner (or a shared base config)
- Required Maven dependencies: `io.cucumber:cucumber-java`, `io.cucumber:cucumber-junit-platform-engine`, `io.cucumber:cucumber-spring`

### Runner Example

```java
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.craftpg")
@SpringBootTest
@CucumberContextConfiguration
public class CucumberRunner {}
```

### Feature File Structure

```gherkin
@tag
Feature: <Feature Name>
  As a <role>
  I want <goal>
  So that <benefit>

  Background:
    Given <shared precondition>

  Scenario: <Happy path description>
    Given <precondition>
    When  <action>
    Then  <expected outcome>

  Scenario Outline: <Parameterized scenario>
    Given <precondition with <param>>
    When  <action>
    Then  <expected outcome>

    Examples:
      | param  |
      | value1 |
      | value2 |
```

### Step Definition Conventions

- Annotate with `@Given`, `@When`, `@Then` from `io.cucumber.java.en`
- Use `@Autowired` or constructor injection for Spring beans in step classes
- Capture variables with Cucumber expression parameters or regex groups
- Share state between steps via instance fields (Cucumber creates one instance per scenario)
- **Step method names** follow a descriptive `camelCase` style that mirrors the Gherkin text — they do **not** use the `Should_<expected>_When_<condition>` convention (which applies only to JUnit test methods)
- Example:
  ```java
  @Given("a campaign with id {string} exists")
  public void aCampaignWithIdExists(String campaignId) {
      // setup
  }

  @When("the user accepts the invite with token {string}")
  public void theUserAcceptsTheInvite(String token) {
      // action
  }

  @Then("the membership is created successfully")
  public void theMembershipIsCreatedSuccessfully() {
      // assertion
  }
  ```

### Tagging Strategy

- Tag by domain: `@campaign`, `@invite`, `@character`, `@user`
- Tag by test type: `@smoke`, `@regression`, `@integration`
- Tag negative/edge cases: `@negative`, `@boundary`
