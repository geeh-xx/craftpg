package com.craftpg.features.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class InviteLifecycleSteps extends HttpStepSupport {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private String campaignId;

    @Given("the invite API route {string} {string}")
    public void theInviteApiRoute(final String method, final String path) {
        String resolvedPath = path;
        if (campaignId != null && !campaignId.isBlank() && path.contains("{campaignId}")) {
            resolvedPath = path.replace("{campaignId}", campaignId);
        }
        setAuthenticated(true);
        setRoute(method, resolvedPath);
    }

    @Given("an existing campaign for the invite scenario")
    public void anExistingCampaignForTheInviteScenario() {
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
        Assertions.assertEquals(status.intValue(), getResponseStatus(),
                () -> "Unexpected status. Body: " + getResponseBody());
    }
}
