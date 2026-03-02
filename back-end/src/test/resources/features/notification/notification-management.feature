@notification @regression @integration
Feature: Notification HTTP Routes
  As an API client
  I want to call notification endpoints through HTTP
  So that notification controller routes are covered

  Scenario Outline: Notification routes return expected status
    Given the notification API route "<method>" "<path>"
    And the notification request payload template is "<payload>"
    When the notification client sends the HTTP request
    Then the notification response status is <status>

    Examples:
      | method | path                               | payload | status |
      | GET    | /notifications                     | none    | 400    |
      | POST   | /notifications/55555555-5555-5555-5555-555555555555/read | none | 500 |
