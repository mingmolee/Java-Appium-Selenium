package test.steps.ios;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import test.pages.ios.exampleIosPage;

public class exampleIosSteps {
    private exampleIosPage page;

    public exampleIosSteps(exampleIosPage page) {
        this.page = page;
    }

    @Given("the user logs into the Walmart Grocery application in iOS")
    public void theUserLogsIntoTheWalmartGroceryApplicationInIOS() {
        page.navigateHere();
    }

    @Then("verifies the {string} is displayed on the home screen in iOS")
    public void verifiesTheIsDisplayedOnTheHomeScreenInIOS(String elementField) {
        page.assertDisplayed(page.getElement(elementField),30);
    }
}
