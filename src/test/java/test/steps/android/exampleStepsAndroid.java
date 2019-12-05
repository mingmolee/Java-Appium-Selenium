package test.steps.android;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import test.pages.android.examplePageAndroid;

public class exampleStepsAndroid {
  private examplePageAndroid page;

  public exampleStepsAndroid(examplePageAndroid page) {
    this.page = page;
  }

  @Given("the user logs into the Walmart Grocery App on the Android")
  public void theUserLogsIntoTheWalmartGroceryAppOnTheAndroid() {
    page.navigateHere();
  }

  @Then("verifies the {string} is displayed on the home screen")
  public void verifiesTheIsDisplayedOnTheHomeScreen(String elementField) {
    page.assertDisplayed(page.getElement(elementField), 3);
  }
}
