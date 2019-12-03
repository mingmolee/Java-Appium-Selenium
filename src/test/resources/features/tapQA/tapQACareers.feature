@tapQA
Feature: tapQA Home

  @ValidationSmoke
  Scenario: Validate Job Listings
    Given the User navigates to the tapQA home page
    And navigates to the tapQA Careers page
    And clicks "Jump To Job Listings Button" on "tapQACareersPageWeb"
    Then validates the "Job Listings" are listed on the Careers page