package com.craftpg.features.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class CampaignManagementSteps extends HttpStepSupport {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private String createdCampaignId;

    @Given("the campaign API route {string} {string}")
    public void theCampaignApiRoute(final String method, final String path) {
        setAuthenticated(true);
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

    @When("the campaign client creates a campaign")
    public void theCampaignClientCreatesACampaign() {
        Assertions.assertTrue(hasPayloadTemplateConfigured(),
                "Payload template must be configured before creating a campaign");
        setAuthenticated(true);
        setRoute("POST", "/campaigns");
        sendRequest();
    }

    @When("the campaign client creates a campaign using payload template {string}")
    public void theCampaignClientCreatesACampaignUsingPayloadTemplate(final String payloadTemplate) {
        setPayloadTemplate(payloadTemplate);
        setAuthenticated(true);
        setRoute("POST", "/campaigns");
        sendRequest();
    }

    @Then("the campaign id is returned in the create response")
    public void theCampaignIdIsReturnedInTheCreateResponse() throws Exception {
        var responseJson = OBJECT_MAPPER.readTree(getResponseBody());
        Assertions.assertTrue(responseJson.hasNonNull("id"),
                () -> "Expected field 'id' in response body: " + getResponseBody());

        createdCampaignId = responseJson.get("id").asText();
        Assertions.assertFalse(createdCampaignId.isBlank(), "Created campaign id should not be blank");
    }

    @When("the campaign client fetches the created campaign by id")
    public void theCampaignClientFetchesTheCreatedCampaignById() {
        Assertions.assertNotNull(createdCampaignId, "Created campaign id must be available before fetch");
        setAuthenticated(true);
        setPayloadTemplate("none");
        setRoute("GET", "/campaigns/" + createdCampaignId);
        sendRequest();
    }

    @Then("the fetched campaign id matches the created campaign id")
    public void theFetchedCampaignIdMatchesTheCreatedCampaignId() throws Exception {
        var responseJson = OBJECT_MAPPER.readTree(getResponseBody());
        Assertions.assertEquals(createdCampaignId, responseJson.path("id").asText(),
                () -> "Fetched campaign id differs from created id. Body: " + getResponseBody());
    }

    @Then("the fetched campaign title is {string}")
    public void theFetchedCampaignTitleIs(final String expectedTitle) throws Exception {
        var responseJson = OBJECT_MAPPER.readTree(getResponseBody());
        Assertions.assertEquals(expectedTitle, responseJson.path("title").asText(),
                () -> "Fetched campaign title differs from expected. Body: " + getResponseBody());
    }

    @Then("the campaign response status is {int}")
    public void theCampaignResponseStatusIs(final Integer status) {
        Assertions.assertEquals(status.intValue(), getResponseStatus(),
                () -> "Unexpected status. Body: " + getResponseBody());
    }
}
