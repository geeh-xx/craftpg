@notification @regression @integration
Feature: Notification HTTP Routes
  As an API client
  I want to call notification endpoints through HTTP
  So that can mark all notifications read and get unread notifications

  Scenario Outline: Notification routes return expected status
    Given the notification API route "<method>" "<path>"
    And the notification request authentication is "authenticated"
    When the notification client sends the HTTP request
    Then the notification response status is <status>

    Examples:
      | method | path                               | status |
      | GET    | /notifications/unread              | 200    |
      | POST   | /notifications/read-all            | 200    |
