package test.pages.android;

import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.FindBy;
import setup.Config;
import test.pages.base.PageObjectBase;

public class exampleAndroidPage extends PageObjectBase {

  public exampleAndroidPage() {}

  @FindBy(xpath = "//android.widget.Button[@text='SIGN IN']")
  private AndroidElement signInButton;

  @FindBy(
      xpath =
          "//android.widget.EditText[@resource-id='com.walmart.grocery:id/sign_in_email_field']")
  private AndroidElement emailTextField;

  @FindBy(
      xpath =
          "//android.widget.EditText[@resource-id='com.walmart.grocery:id/sign_in_password_field']")
  private AndroidElement passwordTextField;

  @FindBy(xpath = "//android.widget.TextView[@text='Featured Items']")
  private AndroidElement featuredItems;

  @FindBy(xpath = "//android.widget.TextView[@resource-id='com.walmart.grocery:id/action_scan']")
  private AndroidElement trait;

  @Override
  public void trait() {
    assertDisplayed(trait, 30);
  }

  @Override
  public void navigateHere() {
    jsClick(signInButton);
    emailTextField.sendKeys(Config.USERNAME);
    passwordTextField.sendKeys(Config.PASSWORD);
    jsClick(signInButton);
    trait();
  }
}
