package test.pages.base;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import setup.Hooks;
import utilities.Tools;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

public abstract class PageObjectBase {
  public RemoteWebDriver driver;

  public PageObjectBase() {
    this.driver = Hooks.getDriver();
    driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
    setAjaxDecorator();
  }

  private void setAjaxDecorator() {
    AjaxElementLocatorFactory decorator = new AjaxElementLocatorFactory(driver, 1);
    PageFactory.initElements(decorator, this);
  }

  /**
   * implement logic needed to assert page was navigated to. IMPORTANT: Put this inside of
   * navigateHere() after the logic to nav to the page has been performed
   */
  public abstract void trait();

  /** implement logic needed to navigate to the page this method is present on */
  public abstract void navigateHere();

  /** @param element to scroll into view */
  public void scrollIntoView(WebElement element) {
    driver.executeScript("arguments[0].scrollIntoView(true);", element);
  }

  /** @param url to load with defined ENVIRONMENT */
  public void loadUrl(String url) {
    driver.get(url);
  }

  public void jsClick(WebElement element) {
    JavascriptExecutor jse = driver;
    try {
      element.click();
    } catch (Exception e) {
      jse.executeScript("arguments[0].click();", element);
    }
  }

  public void jsSetValue(WebElement element, String value) {
    JavascriptExecutor js = driver;
    js.executeScript(String.format("arguments[0].value='%s';", value), element);
  }

  public void jsClear(WebElement element) {
    JavascriptExecutor js = driver;
    js.executeScript("arguments[0].value = '';", element);
  }

  // <editor-fold desc="Get Methods">
  /**
   * element field to find within the given class
   *
   * @param elementField Name of element field to find
   * @param className name of class to use
   * @return element found casted with WebElement
   */
  public WebElement getElement(String elementField, String className) {

    WebElement field = (WebElement) getField(elementField, getClassByName(className));
    return field;
  }

  /**
   * element field to find within the current class instance being used
   *
   * @param elementField Name of element field to find
   * @return element found casted with WebElement
   */
  public WebElement getElement(String elementField) {

    WebElement field = (WebElement) getField(elementField);
    return field;
  }

  /**
   * @param elementsField Name of element list field to find
   * @return returns found field as List<WebElement>
   */
  @SuppressWarnings("unchecked")
  public List<WebElement> getElements(String elementsField) {

    List<WebElement> field = (List<WebElement>) getField(elementsField);
    return field;
  }

  /**
   * @param elementsField Name of element list field to find
   * @param className name of class to use
   * @return returns found field as List<WebElement>
   */
  @SuppressWarnings("unchecked")
  public List<WebElement> getElements(String elementsField, String className) {

    List<WebElement> field = (List<WebElement>) getField(elementsField, getClassByName(className));
    return field;
  }

  /**
   * @param elementFields list of element fields to get
   * @return returns found fields as List<WebElement>
   */
  public List<WebElement> getElements(List<String> elementFields) {

    List<WebElement> list =
        elementFields.stream().map(this::getElement).distinct().collect(toList());
    return list;
  }

  /**
   * @param elementFields list of element fields to get
   * @return returns found fields as List<WebElement>
   */
  public List<WebElement> getElements(List<String> elementFields, String className) {

    List<WebElement> list =
        elementFields.stream()
            .map(elementField -> getElement(elementField, className))
            .distinct()
            .collect(toList());
    return list;
  }

  /**
   * @param fieldName Name of declared field on page that will get camel cased
   * @return Found field with param fieldName from class
   */
  private Object getField(String fieldName) {
    String target = Tools.toCamelCase(fieldName);
    Class aClass = null;

    try {
      aClass = getClass();
      Field field = aClass.getDeclaredField(target);
      field.setAccessible(true);
      Object fieldFound = field.get(this);
      return fieldFound;
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalArgumentException(
          String.format("Element not found: [%s] in Class [%s]", target, aClass.getSimpleName()));
    }
  }

  /**
   * @param fieldName Name of declared field on page that will get camel cased
   * @param aClass class to find field in
   * @return Found field with param fieldName from class
   */
  @SuppressWarnings("unchecked")
  private Object getField(String fieldName, Class aClass) {
    String target = Tools.toCamelCase(fieldName);

    try {
      trait();
      Field field = aClass.getDeclaredField(target);
      field.setAccessible(true);
      Object fieldFound = field.get(this);
      return fieldFound;
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalArgumentException(
          String.format("Element not found: [%s] in Class [%s]", target, aClass.getSimpleName()));
    }
  }

  /**
   * @param className name of class to use
   * @return found class with declared packages and provided name
   */
  private Class<?> getClassByName(String className) {
    Class<?> targetClass = null;
    int index = 0;

    List<String> packages = Arrays.asList("tapQA", "base");

    while (targetClass == null && index <= packages.size() - 1) {

      try {
        targetClass =
            Class.forName(
                String.format(
                    "test.pages.%s.%s", packages.get(index), className.replaceAll("\\s", "")));
      } catch (ClassNotFoundException e) {
        index++;
      }
    }

    Assert.assertNotNull(String.format("[%s] was not found", className), targetClass);
    return targetClass;
  }

  /**
   * Get element with text
   *
   * @param elements list of elements to search through
   * @param text text to look for in elements
   * @return element found with text
   */
  public WebElement getElementWithText(List<WebElement> elements, String text) {
    WebElement webElement =
        elements.stream()
            .filter(elem -> elem.getText().trim().equalsIgnoreCase(text))
            .findFirst()
            .orElseThrow(
                () ->
                    new NoSuchElementException(
                        String.format("Element Target Text was not found: [%s]", text)));
    return webElement;
  }

  /**
   * Assert Element Displayed and return found elements
   *
   * @param element the WebElement we want to wait for to be displayed
   * @param waitSec how many seconds to wait
   * @return element to be chained off of EX:assertDisplayed(element, 5).click
   */
  public WebElement assertDisplayed(WebElement element, int waitSec) {
    fluentWait(waitSec, 1)
        .until(
            ExpectedConditions.or(
                ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)),
                ExpectedConditions.visibilityOf(element)));

    return element;
  }

  /**
   * Assert all Element Displayed and return found elements
   *
   * @param elements
   * @return
   */
  public List<WebElement> assertAllDisplayed(List<WebElement> elements) {
    return elements.stream()
        .map(element -> assertDisplayed(element, 1))
        .distinct()
        .collect(toList());
  }

  /**
   * @param seconds seconds to wait
   * @param pollTime how often the condition should be evaluated
   * @return chain of returned wait. IMPORTANT! -> must have .until(ExpectedConditions) or it will
   *     not wait
   */
  public FluentWait<WebDriver> fluentWait(Integer seconds, Integer pollTime) {
    assertWaitLimit(seconds);

    FluentWait<WebDriver> fluentWait =
        new FluentWait<WebDriver>(driver)
            .withTimeout(Duration.ofSeconds(seconds))
            .pollingEvery(Duration.ofSeconds(pollTime))
            .ignoring(AssertionError.class)
            .ignoring(IndexOutOfBoundsException.class)
            .ignoring(WebDriverException.class);

    if (seconds == 180)
      fluentWait.withMessage(
          "Time waited reached [3 minute] mark. Test was failed for taking too long.");
    String.format(
        "Waiting:[%ss] and pollingEvery:[%ss] for condition to be met", seconds, pollTime);
    return fluentWait;
  }

  private void assertWaitLimit(int seconds) {
    boolean timeToWaitIsLessThan3min = 0 < seconds && seconds < 181;

    if (!timeToWaitIsLessThan3min) {
      Assert.fail("Time waited needs to be greater than 0 and less than 3 minutes");
    }
  }
}
