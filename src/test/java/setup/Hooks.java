package setup;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import utilities.Tools;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Hooks {
  private static Scenario currentScenario;
  private static boolean reportsCreated = false;
  private static RemoteWebDriver driver;

  // quits driver
  static {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> driver.quit()));
  }

  // quits driver
  static {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  driver.quit();
                }));
  }

  private DriverFactory factory;
  private boolean setup = false;
  private Config config;
  private URL url;
  private DesiredCapabilities capabilities;

  public Hooks() {}

  public static void takeScreenshot() {
    fileScreenshot();
    embedScreenshot();
  }

  private static void fileScreenshot() {
    try {
      File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      String fileName =
          String.format(
              "./TestResults/Screenshots/Feature_%s_Line%s_Time[%s].png",
              currentScenario.getName(), currentScenario.getLine(), Tools.getDate("hh-mm-ss", 0));
      FileUtils.copyFile(sourceFile, new File(fileName));
    } catch (WebDriverException | NullPointerException | IOException e) {
      System.out.println("Failed to take screenshot");
    }
  }

  public static void embedScreenshot() {
    try {
      final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
      currentScenario.embed(screenshot, "image/png");
    } catch (WebDriverException | NullPointerException e) {
      System.out.println("Failed to take embed Screenshot");
    }
  }

  // <editor-fold desc="Gets and Sets">
  public static RemoteWebDriver getDriver() {
    return driver;
  }

  private void setDriver(RemoteWebDriver driver) {
    Hooks.driver = driver;
  }

  @Before(order = 1)
  public void beforeAll(Scenario scenario) throws MalformedURLException {
    if (!scenario.getName().equalsIgnoreCase("Help")) {
      manageResults();

      config = new Config();
      config.setCapabilitiesForPlatform();

      setupEnvironment(scenario);

      if (driver == null) {
        try {
          factory = new DriverFactory(config);
          setDriver(factory.createDriver());
          if (Config.getDeviceName().equalsIgnoreCase("Web")) driver.manage().deleteAllCookies();
        } catch (ElementNotInteractableException e) {
          // Ignore exception
        }
      }
    }
  }

  @After(order = 1)
  public void afterAll(Scenario scenario) {
    boolean notHelpTask = !scenario.getName().equalsIgnoreCase("Help");

    try {
      if (notHelpTask) {
        if (scenario.isFailed()) {
          takeScreenshot();
        }
      }
    } finally {
      if (notHelpTask) {
        setup = false;

        if (driver != null) {
          driver.manage().deleteAllCookies();
          driver.executeScript("window.sessionStorage.clear();");
          driver.executeScript("window.localStorage.clear();");
        }
      }
    }
  }

  /**
   * Sets up environment and capabilities for given properties and data.
   *
   * @param scenario instanced use of Scenario to get state of current scenario
   * @throws MalformedURLException
   */
  private void setupEnvironment(Scenario scenario) throws MalformedURLException {
    if (!setup) {
      currentScenario = scenario;

      this.url = new URL(config.getUrl());
      this.capabilities = new DesiredCapabilities(config.getCapabilities());

      setup = true;
    }
  }

  /** removes previously created reports and temp files */
  private void manageResults() {
    if (!reportsCreated) {
      try {
        FileUtils.deleteDirectory(new File("./TestResults"));
      } catch (IOException e) {
        // Ignored files not present
      }

      try {
        FileUtils.forceMkdir(new File("./TestResults/ScreenShots"));
      } catch (IOException e) {
        System.err.println("Failed to create TestResults file directory");
      }

      reportsCreated = true;
    }
  }
  // </editor-fold>
}
