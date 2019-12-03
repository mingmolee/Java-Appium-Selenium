package test.steps.tapQA;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import test.pages.tapQA.tapQACareersPageWeb;

public class tapQACareersStepsWeb {
  private tapQACareersPageWeb page;

  public tapQACareersStepsWeb(tapQACareersPageWeb page) {
    this.page = page;
  }

  @And("navigates to the tapQA Careers page")
  public void navigatesToTheTapQACareersPage() {
    page.navigateHere();
  }

  @Then("validates the {string} are listed on the Careers page")
  public void validatesTheAreListedOnTheCareersPage(String elementFields) {
    page.assertAllDisplayed(page.getElements(elementFields));
  }
}
