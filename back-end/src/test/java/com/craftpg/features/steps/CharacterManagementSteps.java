package com.craftpg.features.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class CharacterManagementSteps extends HttpStepSupport {

    @Given("the character API route {string} {string}")
    public void theCharacterApiRoute(final String method, final String path) {
        setRoute(method, path);
    }

    @Given("the character request payload template is {string}")
    public void theCharacterRequestPayloadTemplate(final String payloadTemplate) {
        setPayloadTemplate(payloadTemplate);
    }

    @When("the character client sends the HTTP request")
    public void theCharacterClientSendsTheHttpRequest() {
        sendRequest();
    }

    @Then("the character response status is {int}")
    public void theCharacterResponseStatusIs(final Integer status) {
        Assertions.assertEquals(status.intValue(), getResponseStatus(), () -> "Unexpected status. Body: " + getResponseBody());
    }
}
