package com.craftpg.features.steps;

import com.craftpg.infrastructure.persistence.repository.AppUserRepository;
import com.craftpg.infrastructure.web.client.AuthServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Assertions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AuthenticationFlowSteps extends HttpStepSupport {

    private static final UUID CUCUMBER_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    private AppUserRepository appUserRepository;

    @Before("@auth")
    public void setUpAuthServiceClientStub() {
        when(authServiceClient.getAdminToken(anyString(), any()))
            .thenReturn(new AuthServiceClient.KeycloakTokenResponse("token-value"));
        when(authServiceClient.getUser(anyString(), any(UUID.class), anyString()))
            .thenReturn(new HashMap<>());
    }

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

    @Then("the authentication response display name is {string}")
    public void theAuthenticationResponseDisplayNameIs(final String expectedDisplayName) {
        try {
            var root = objectMapper.readTree(getResponseBody());
            Assertions.assertEquals(expectedDisplayName, root.path("displayName").asText());
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to parse authentication response body", ex);
        }
    }

    @Then("the authenticated user display name is {string} in the database")
    public void theAuthenticatedUserDisplayNameIsInTheDatabase(final String expectedDisplayName) {
        var user = appUserRepository.findById(CUCUMBER_USER_ID)
            .orElseThrow(() -> new IllegalStateException("Cucumber user was not found in database"));

        Assertions.assertEquals(expectedDisplayName, user.getDisplayName());
    }
}
