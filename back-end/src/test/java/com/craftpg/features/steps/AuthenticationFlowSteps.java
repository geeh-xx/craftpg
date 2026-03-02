package com.craftpg.features.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class AuthenticationFlowSteps extends HttpStepSupport {

    @Given("the authentication API route {string} {string}")
    public void theAuthenticationApiRoute(final String method, final String path) {
        setAuthenticated(false);
        setRoute(method, path);
    }

    @Given("the authenticated authentication API route {string} {string}")
    public void theAuthenticatedAuthenticationApiRoute(final String method, final String path) {
        setAuthenticated(true);
        setRoute(method, path);
    }

    @Given("the authentication request payload template is {string}")
    public void theAuthenticationRequestPayloadTemplate(final String payloadTemplate) {
        setPayloadTemplate(payloadTemplate);
    }

    @When("the authentication client sends the HTTP request")
    public void theAuthenticationClientSendsTheHttpRequest() {
        sendRequest();
    }

    @Then("the authentication response status is {int}")
    public void theAuthenticationResponseStatusIs(final Integer status) {
        Assertions.assertEquals(status.intValue(), getResponseStatus(), () -> "Unexpected status. Body: " + getResponseBody());
    }
}
