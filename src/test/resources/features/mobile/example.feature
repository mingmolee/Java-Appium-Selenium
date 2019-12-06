Feature: Walmart Grocery Example

  @Android
  Scenario: Successfully Log In with a Valid Account
    Given the user logs into the Walmart Grocery App on the Android
    Then verifies the "Featured Items" is displayed on the home screen

  @iOS
  Scenario: Successfully Log into Walmart Grocery with a Valid Account in iOS
    Given the user logs into the Walmart Grocery application in iOS
    Then verifies the "Featured Items" is displayed on the home screen in iOS