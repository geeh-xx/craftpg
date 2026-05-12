@campaign @regression @integration
Feature: Campaign HTTP Routes
  As an API client
  I want to call campaign endpoints through HTTP
  So that campaign controller routes are covered

  @smoke
  Scenario: Creating a campaign can be verified by fetching it by id
    When the campaign client creates a campaign using payload template "create-campaign"
    Then the campaign response status is 201
    And the campaign id is returned in the create response
    When the campaign client fetches the created campaign by id
    Then the campaign response status is 200
    And the fetched campaign id matches the created campaign id
    And the fetched campaign title is "Campaign from payload"

  @smoke
  Scenario Outline: Campaign routes return expected status
    Given the campaign API route "<method>" "<path>"
    And the campaign request payload template is "<payload>"
    When the campaign client sends the HTTP request
    Then the campaign response status is <status>

    Examples:
      | method | path                                                     | payload         | status |
      | GET    | /campaigns                                               | none            | 200    |
      | POST   | /campaigns                                               | create-campaign | 201    |
      | GET    | /campaigns/11111111-1111-1111-1111-111111111111          | none            | 400    |
      | PATCH  | /campaigns/11111111-1111-1111-1111-111111111111          | update-campaign | 400    |
      | POST   | /campaigns/11111111-1111-1111-1111-111111111111/finish   | none            | 400    |
      | GET    | /campaigns/11111111-1111-1111-1111-111111111111/permissions | none        | 200    |
      | DELETE | /campaigns/11111111-1111-1111-1111-111111111111          | none            | 400    |
