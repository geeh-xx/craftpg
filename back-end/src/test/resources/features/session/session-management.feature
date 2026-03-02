@session @regression @integration
Feature: Session HTTP Routes
  As an API client
  I want to call session endpoints through HTTP
  So that session controller routes are covered

  @smoke
  Scenario: Create session for an existing campaign
    Given an existing campaign for the session scenario
    And the session API route for the created campaign "POST" "/campaigns/{campaignId}/sessions"
    And the session request payload template is "create-session"
    When the session client sends the HTTP request
    Then the session response is successful

  @negative
  Scenario Outline: Session routes return expected error status
    Given the session API route "<method>" "<path>"
    And the session request payload template is "<payload>"
    When the session client sends the HTTP request
    Then the session response status is <status>

    Examples:
      | method | path                                                                                                   | payload        | status |
      | GET    | /campaigns/11111111-1111-1111-1111-111111111111/sessions                                              | none           | 400    |
      | PATCH  | /campaigns/11111111-1111-1111-1111-111111111111/sessions/44444444-4444-4444-4444-444444444444        | update-session | 400    |
