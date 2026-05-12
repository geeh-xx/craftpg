package com.craftpg.features.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class NotificationManagementSteps extends HttpStepSupport {

    private boolean authenticatedRequest = true;

    @Given("the notification API route {string} {string}")
    public void theNotificationApiRoute(final String method, final String path) {
        setAuthenticated(authenticatedRequest);
        setRoute(method, path);
    }

    @Given("the notification request authentication is {string}")
    public void theNotificationRequestAuthenticationIs(final String mode) {
        authenticatedRequest = !"unauthenticated".equalsIgnoreCase(mode);
    }

    @Given("the notification request payload template is {string}")
    public void theNotificationRequestPayloadTemplate(final String payloadTemplate) {
        setPayloadTemplate(payloadTemplate);
    }

    @When("the notification client sends the HTTP request")
    public void theNotificationClientSendsTheHttpRequest() {
        sendRequest();
    }

    @Then("the notification response status is {int}")
    public void theNotificationResponseStatusIs(final Integer status) {
        Assertions.assertEquals(status.intValue(), getResponseStatus(),
                () -> "Unexpected status. Body: " + getResponseBody());
    }
}
