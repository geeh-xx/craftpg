---
applyTo: "src/test/java/**/*.java"
---

# Test Instructions


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
