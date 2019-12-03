@tapQA
Feature: tapQA Home

  @ValidationSmoke
  Scenario: Validate Top Work Place Image
    Given the User navigates to the tapQA home page
    And scrolls to "Top Work Places" on "tapQAHomePageWeb"
    Then validates the "Top Work Places" image is displayed