@character @regression @integration
Feature: Character HTTP Routes
  As an API client
  I want to call character endpoints through HTTP
  So that character controller routes are covered

  Scenario Outline: Character routes return expected status
    Given the character API route "<method>" "<path>"
    And the character request payload template is "<payload>"
    When the character client sends the HTTP request
    Then the character response status is <status>

    Examples:
      | method | path                                                   | payload                   | status |
      | GET    | /characters                                             | none                      | 200    |
      | POST   | /characters                                             | create-character          | 201    |
      | POST   | /characters/generate-random                             | none                      | 201    |
      | GET    | /campaigns/11111111-1111-1111-1111-111111111111/characters/me | none                | 400    |
      | PATCH  | /campaigns/11111111-1111-1111-1111-111111111111/characters/me | update-campaign-character | 400 |
      | POST   | /campaigns/11111111-1111-1111-1111-111111111111/characters/33333333-3333-3333-3333-333333333333/xp | add-xp | 400 |
