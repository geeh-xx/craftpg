@invite @regression @integration
Feature: Invite HTTP Routes
  As an API client
  I want to call invite endpoints through HTTP
  So that invite controller routes are covered

  Scenario Outline: Invite routes return expected status
    Given the invite API route "<method>" "<path>"
    And the invite request payload template is "<payload>"
    When the invite client sends the HTTP request
    Then the invite response status is <status>

    Examples:
      | method | path                                     | payload       | status |
      | GET    | /campaigns/11111111-1111-1111-1111-111111111111/invites | none | 400 |
      | POST   | /campaigns/11111111-1111-1111-1111-111111111111/invites | create-invite | 400 |
      | DELETE | /campaigns/11111111-1111-1111-1111-111111111111/invites/22222222-2222-2222-2222-222222222222 | none | 400 |
      | GET    | /invites/sample-token                    | none          | 500    |
      | POST   | /invites/sample-token/accept             | accept-invite | 400    |
