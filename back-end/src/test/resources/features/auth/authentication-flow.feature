@auth @regression @integration
Feature: User HTTP Routes
  As an API client
  I want to call user endpoints through HTTP
  So that user controller routes are covered

  Scenario Outline: Authenticated user routes return success
    Given the authenticated authentication API route "<method>" "<path>"
    And the authentication request payload template is "<payload>"
    When the authentication client sends the HTTP request
    Then the authentication response status is <status>

    Examples:
      | method | path | payload | status |
      | GET    | /me  | none    | 200    |
      | PUT    | /me  | update-me | 200  |

  Scenario Outline: User routes return expected status
    Given the authentication API route "<method>" "<path>"
    And the authentication request payload template is "<payload>"
    When the authentication client sends the HTTP request
    Then the authentication response status is <status>

    Examples:
      | method | path | payload | status |
      | GET    | /me  | none    | 401    |
      | PUT    | /me  | update-me | 401  |

  Scenario: Authenticated profile update persists new display name
    Given the authenticated authentication API route "PUT" "/me"
    And the authentication request payload template is "update-me"
    When the authentication client sends the HTTP request
    Then the authentication response status is 200
    And the authentication response display name is "Updated Cucumber User"
    And the authenticated user display name is "Updated Cucumber User" in the database
