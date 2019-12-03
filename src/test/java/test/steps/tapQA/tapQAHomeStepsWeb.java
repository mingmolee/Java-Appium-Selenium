package test.steps.tapQA;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import test.pages.tapQA.tapQAHomePageWeb;

public class tapQAHomeStepsWeb {
  private tapQAHomePageWeb page;

  public tapQAHomeStepsWeb(tapQAHomePageWeb page) {
    this.page = page;
  }

  @Given("the User navigates to the tapQA home page")
  public void theUserNavigatesToTheTapQAHomePage() {
    page.navigateHere();
  }

  @Then("validates the {string} image is displayed")
  public void validatesTheImageIsDisplayed(String element) {
    page.assertDisplayed(page.getElement(element), 30);
  }
}
