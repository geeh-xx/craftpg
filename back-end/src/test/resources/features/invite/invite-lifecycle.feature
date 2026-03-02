@invite @regression @integration
Feature: Invite HTTP Routes
  As an API client
  I want to call invite endpoints through HTTP
  So that invite controller routes are covered

  Scenario Outline: Invite preview returns campaign name, DM name and roles assigned
    Given an existing invite token for the invite preview scenario
    And the invite request authentication is "<auth>"
    And the invite API route "<method>" "<path>"
    And the invite request payload template is "<payload>"
    When the invite client sends the HTTP request
    Then the invite response status is <status>
    And the invite preview includes campaign name dm name and roles assigned

    Examples:
      | auth            | method | path            | payload | status |
      | unauthenticated | GET    | /invites/{token} | none    | 200    |

  Scenario Outline: Accept invite requires authenticated user
    Given an existing invite token for the invite preview scenario
    And the invite request authentication is "<auth>"
    And the invite API route "<method>" "<path>"
    And the invite request payload template is "<payload>"
    When the invite client sends the HTTP request
    Then the invite response status is <status>

    Examples:
      | auth            | method | path                   | payload       | status |
      | unauthenticated | POST   | /invites/{token}/accept | accept-invite | 401    |

  @smoke
  Scenario Outline: Invite routes return success for an existing campaign
    Given an existing campaign for the invite scenario
    And the invite request authentication is "authenticated"
    And the invite API route "<method>" "<path>"
    And the invite request payload template is "<payload>"
    When the invite client sends the HTTP request
    Then the invite response status is <status>

    Examples:
      | method | path                           | payload       | status |
      | POST   | /campaigns/{campaignId}/invites | create-invite | 201    |
      | GET    | /campaigns/{campaignId}/invites | none          | 200    |

  Scenario Outline: Invite routes return expected status
    Given the invite request authentication is "authenticated"
    And the invite API route "<method>" "<path>"
    And the invite request payload template is "<payload>"
    When the invite client sends the HTTP request
    Then the invite response status is <status>

    Examples:
      | method | path                                     | payload       | status |
      | GET    | /campaigns/11111111-1111-1111-1111-111111111111/invites | none | 400 |
      | POST   | /campaigns/11111111-1111-1111-1111-111111111111/invites | create-invite | 400 |
      | DELETE | /campaigns/11111111-1111-1111-1111-111111111111/invites/22222222-2222-2222-2222-222222222222 | none | 400 |
      | GET    | /invites/sample-token                    | none          | 400    |
      | POST   | /invites/sample-token/accept             | accept-invite | 400    |
