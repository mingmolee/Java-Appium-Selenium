package test.pages.tapQA;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.pages.base.PageObjectBase;

import java.util.List;

public class tapQACareersPageWeb extends PageObjectBase {

  @FindBy(xpath = "//h1[text()='Careers']")
  private WebElement pageTrait;

  @FindBy(xpath = "//span[contains(@class,'menu-text')][text()='CAREERS']")
  private WebElement careersNavButton;

  @FindBy(css = "[href='#listings']")
  private WebElement jumpToJobListingsButton;

  @FindBy(css = "li[class*='job']")
  private List<WebElement> jobListings;

  @Override
  public void trait() {
    assertDisplayed(pageTrait, 30);
  }

  @Override
  public void navigateHere() {
    jsClick(careersNavButton);
    trait();
  }

  public void clickMe() {
    jumpToJobListingsButton.click();
  }
}
