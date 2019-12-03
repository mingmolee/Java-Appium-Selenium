package test.steps.common;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import test.pages.base.PageObjectBase;

import static org.junit.Assert.assertEquals;

public class commonSteps extends PageObjectBase {
  @Override
  public void trait() {
    // is not used
  }

  @Override
  public void navigateHere() {
    // is not used
  }

  @And("(common_)clicks {string} on {string}")
  public void clicksOn(String elementField, String pageClassName) {
    jsClick(getElement(elementField, pageClassName));
  }

  @And("(commmon_)scrolls to {string} on {string}")
  public void scrollsTo(String elementField, String pageClassName) {
    scrollIntoView(getElement(elementField, pageClassName));
  }

  @Then("(common_)verifies {string} equals the correct {string} on {string}")
  public void verifiesDisplaysTheCorrect(
      String elementField, String expectedValue, String pageClassName) {
    assertDisplayed(getElement(elementField, pageClassName), 30);
    assertEquals(expectedValue, getElement(elementField, pageClassName).getText());
  }
}
