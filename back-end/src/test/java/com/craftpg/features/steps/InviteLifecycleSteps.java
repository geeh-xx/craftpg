package com.craftpg.features.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.craftpg.domain.input.CreateCampaignInput;
import com.craftpg.domain.model.Campaign;
import com.craftpg.domain.model.CampaignInvite;
import com.craftpg.domain.model.CampaignRole;
import com.craftpg.infrastructure.persistence.repository.CampaignInviteRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRepository;
import com.craftpg.infrastructure.persistence.repository.CampaignRoleRepository;
import com.craftpg.shared.constants.CampaignRoleType;
import com.craftpg.shared.util.HashUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Assertions;

public class InviteLifecycleSteps extends HttpStepSupport {

    private static final UUID CUCUMBER_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignInviteRepository campaignInviteRepository;

    @Autowired
    private CampaignRoleRepository campaignRoleRepository;

    private String campaignId;
    private String inviteToken;
    private boolean authenticatedRequest = true;

    @Given("the invite API route {string} {string}")
    public void theInviteApiRoute(final String method, final String path) {
        setAuthenticated(authenticatedRequest);
        String resolvedPath = path;
        if (campaignId != null && !campaignId.isBlank() && resolvedPath.contains("{campaignId}")) {
            resolvedPath = resolvedPath.replace("{campaignId}", campaignId);
        }
        if (inviteToken != null && !inviteToken.isBlank() && resolvedPath.contains("{token}")) {
            resolvedPath = resolvedPath.replace("{token}", inviteToken);
        }
        setRoute(method, resolvedPath);
    }

    @Given("the invite request authentication is {string}")
    public void theInviteRequestAuthenticationIs(final String mode) {
        authenticatedRequest = !"unauthenticated".equalsIgnoreCase(mode);
    }

    @Given("an existing campaign for the invite scenario")
    public void anExistingCampaignForTheInviteScenario() {
        Campaign campaign = campaignRepository.save(Campaign.create(new CreateCampaignInput(
            "Cucumber invite campaign",
            "desc",
            "WEEKLY",
            "ACTIVE",
            25
        )));
        campaignRoleRepository.save(CampaignRole.create(campaign.getId(), CUCUMBER_USER_ID, CampaignRoleType.DM));
        campaignId = campaign.getId().toString();
    }

    @Given("an existing invite token for the invite preview scenario")
    public void anExistingInviteTokenForTheInvitePreviewScenario() {
        anExistingCampaignForTheInviteScenario();
        inviteToken = "cucumber-invite-token-" + java.util.UUID.randomUUID();

        campaignInviteRepository.save(CampaignInvite.create(
            java.util.UUID.fromString(campaignId),
            "invitee@craftpg.test",
            HashUtil.sha256(inviteToken),
            Set.of(CampaignRoleType.PLAYER),
            LocalDateTime.now().plusDays(7)
        ));
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

    @Then("the invite preview includes campaign name dm name and roles assigned")
    public void theInvitePreviewIncludesCampaignNameDmNameAndRolesAssigned() {
        try {
            final JsonNode root = objectMapper.readTree(getResponseBody());
            final String campaignTitle = root.path("campaignTitle").asText();
            final String dmName = root.path("dmName").asText();
            final JsonNode roles = root.path("roles");

            Assertions.assertFalse(campaignTitle == null || campaignTitle.isBlank(), "campaignTitle is missing");
            Assertions.assertFalse(dmName == null || dmName.isBlank(), "dmName is missing");
            Assertions.assertTrue(roles.isArray() && roles.size() > 0, "roles must be a non-empty array");
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to parse invite preview response", ex);
        }
    }
}
