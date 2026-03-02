package com.craftpg.features.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class CampaignManagementSteps extends HttpStepSupport {

    @Given("the campaign API route {string} {string}")
    public void theCampaignApiRoute(final String method, final String path) {
        setRoute(method, path);
    }

    @Given("the campaign request payload template is {string}")
    public void theCampaignRequestPayloadTemplate(final String payloadTemplate) {
        setPayloadTemplate(payloadTemplate);
    }

    @When("the campaign client sends the HTTP request")
    public void theCampaignClientSendsTheHttpRequest() {
        sendRequest();
    }

    @Then("the campaign response status is {int}")
    public void theCampaignResponseStatusIs(final Integer status) {
        Assertions.assertEquals(status.intValue(), getResponseStatus(), () -> "Unexpected status. Body: " + getResponseBody());
    }
}
