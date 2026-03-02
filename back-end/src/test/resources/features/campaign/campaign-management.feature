@campaign @regression @integration
Feature: Campaign HTTP Routes
  As an API client
  I want to call campaign endpoints through HTTP
  So that campaign controller routes are covered

  @smoke
  Scenario Outline: Campaign routes return expected status
    Given the campaign API route "<method>" "<path>"
    And the campaign request payload template is "<payload>"
    When the campaign client sends the HTTP request
    Then the campaign response status is <status>

    Examples:
      | method | path                                                     | payload         | status |
      | GET    | /campaigns                         | none            | 400    |
      | POST   | /campaigns                         | create-campaign | 400    |
      | GET    | /campaigns/11111111-1111-1111-1111-111111111111         | none            | 400    |
      | PATCH  | /campaigns/11111111-1111-1111-1111-111111111111         | update-campaign | 400    |
      | POST   | /campaigns/11111111-1111-1111-1111-111111111111/finish  | none            | 400    |
      | GET    | /campaigns/11111111-1111-1111-1111-111111111111/permissions | none        | 400    |
      | DELETE | /campaigns/11111111-1111-1111-1111-111111111111         | none            | 400    |
