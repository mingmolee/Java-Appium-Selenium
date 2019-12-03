package test.pages.tapQA;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.pages.base.PageObjectBase;

public class tapQAHomePageWeb extends PageObjectBase {

  @FindBy(css = "[id='rev_slider_2_1']")
  private static WebElement pageTrait;

  @FindBy(css = "img[title='startribbestplaces']")
  private static WebElement topWorkPlaces;

  @Override
  public void trait() {
    assertDisplayed(pageTrait, 30);
  }

  @Override
  public void navigateHere() {
    String url = "https://www.tapqa.com/";
    loadUrl(url);
    trait();
  }
}
