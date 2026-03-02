package com.craftpg.features.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class SessionManagementSteps extends HttpStepSupport {

    private static final String CAMPAIGN_ID_PLACEHOLDER = "{campaignId}";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String campaignId;

    @Given("the session API route {string} {string}")
    public void theSessionApiRoute(final String method, final String path) {
        setAuthenticated(false);
        setRoute(method, path);
    }

    @Given("the session request payload template is {string}")
    public void theSessionRequestPayloadTemplate(final String payloadTemplate) {
        setPayloadTemplate(payloadTemplate);
    }

    @Given("an existing campaign for the session scenario")
    public void anExistingCampaignForTheSessionScenario() {
        setAuthenticated(true);
        setRoute("POST", "/campaigns");
        setPayloadTemplate("create-campaign");
        sendRequest();
        Assertions.assertEquals(201, getResponseStatus(), () -> "Unexpected campaign setup status. Body: " + getResponseBody());

        try {
            final JsonNode root = objectMapper.readTree(getResponseBody());
            campaignId = root.path("id").asText();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to parse created campaign id", ex);
        }

        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalStateException("Created campaign response does not contain id");
        }
    }

    @Given("the session API route for the created campaign {string} {string}")
    public void theSessionApiRouteForTheCreatedCampaign(final String method, final String pathTemplate) {
        if (campaignId == null || campaignId.isBlank()) {
            throw new IllegalStateException("Campaign id not prepared for scenario");
        }
        setAuthenticated(true);
        setRoute(method, pathTemplate.replace(CAMPAIGN_ID_PLACEHOLDER, campaignId));
    }

    @When("the session client sends the HTTP request")
    public void theSessionClientSendsTheHttpRequest() {
        sendRequest();
    }

    @Then("the session response status is {int}")
    public void theSessionResponseStatusIs(final Integer status) {
        Assertions.assertEquals(status.intValue(), getResponseStatus(), () -> "Unexpected status. Body: " + getResponseBody());
    }

    @Then("the session response is successful")
    public void theSessionResponseIsSuccessful() {
        final int responseStatus = getResponseStatus();
        Assertions.assertTrue(
            responseStatus >= 200 && responseStatus < 300,
            () -> "Expected 2xx status, but got " + responseStatus + ". Body: " + getResponseBody()
        );
    }
}
