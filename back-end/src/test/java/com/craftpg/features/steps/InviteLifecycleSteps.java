package com.craftpg.features.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class InviteLifecycleSteps extends HttpStepSupport {

    @Given("the invite API route {string} {string}")
    public void theInviteApiRoute(final String method, final String path) {
        setRoute(method, path);
    }

    @Given("the invite request payload template is {string}")
    public void theInviteRequestPayloadTemplate(final String payloadTemplate) {
        setPayloadTemplate(payloadTemplate);
    }

    @When("the invite client sends the HTTP request")
    public void theInviteClientSendsTheHttpRequest() {
        sendRequest();
    }

    @Then("the invite response status is {int}")
    public void theInviteResponseStatusIs(final Integer status) {
        Assertions.assertEquals(status.intValue(), getResponseStatus(), () -> "Unexpected status. Body: " + getResponseBody());
    }
}
