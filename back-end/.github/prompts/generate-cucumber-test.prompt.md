---
description: 'Generate a Cucumber feature file and step definitions for the current feature'
agent: 'agent'
tools: ['search', 'read', 'edit', 'create']
---

Generate a Cucumber BDD test for the feature described in [${fileBasename}](${file}).

## Steps

1. **Create the `.feature` file** at `src/test/resources/features/<domain>/<feature-name>.feature`
   - Write a `Feature` header with a user story (As a / I want / So that)
   - Add a `Background` section for shared preconditions if applicable
   - Cover: happy path scenario, at least one negative/error scenario, and one edge case
   - Use `Scenario Outline` + `Examples` for parameterized cases
   - Tag with domain and type tags (e.g., `@campaign @smoke`)

2. **Create the step definition class** at `src/test/java/com/craftpg/<domain>/steps/<Feature>Steps.java`
   - Implement `@Given`, `@When`, `@Then` methods matching the Gherkin steps
   - Use `@SpringBootTest` context and `@Autowired` for Spring beans where needed
   - Follow the conventions in [cucumber.instructions.md](../instructions/cucumber.instructions.md)

3. **Ensure a Cucumber runner exists** at `src/test/java/com/craftpg/runner/CucumberRunner.java`
   - Create it if it does not exist, using `@Suite`, `@IncludeEngines("cucumber")`, `@SpringBootTest`, and `@CucumberContextConfiguration`

4. **Follow TDD**: the `.feature` file and step definitions must be created **before** any production code changes

If there is a selection, generate Cucumber tests only for this scope:
${selection}
