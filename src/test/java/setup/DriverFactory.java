package setup;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.grid.internal.utils.configuration.StandaloneConfiguration;
import org.openqa.grid.selenium.GridLauncherV3;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.server.SeleniumServer;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {

  private static boolean isFirstServerRun = false;
  private Config config = new Config();
  private URL url;
  private DesiredCapabilities capabilities;

  public DriverFactory(Config config) throws MalformedURLException {
    this.url = new URL(config.getUrl());
    this.capabilities = new DesiredCapabilities(config.getCapabilities());
  }

  /** Creates the driver with the given capabilities */
  RemoteWebDriver createDriver() {
    String platform = config.getPlatform().toUpperCase();

    switch (platform) {
      case "ANDROID":
        return new AndroidDriver<MobileElement>(url, capabilities);
      case "IOS":
        return new IOSDriver<MobileElement>(url, capabilities);
      case "WEB":
        startStandaloneServer();
        return new RemoteWebDriver(url, capabilities);

      default:
        throw new IllegalStateException("Unexpected value: " + platform);
    }
  }

  /** Starts the Standalone Selenium Server if it is ran locally. */
  private void startStandaloneServer() {
    boolean firstRunAndNotRemote = !isFirstServerRun && !Config.IS_REMOTE;

    if (firstRunAndNotRemote) {
      SeleniumServer seleniumServer = new SeleniumServer(new StandaloneConfiguration());
      boolean isChrome = Config.getDeviceName().equalsIgnoreCase("chrome");
      boolean isFireFox = Config.getDeviceName().equalsIgnoreCase("firefox");

      if (isChrome) WebDriverManager.chromedriver().setup();
      else if (isFireFox) WebDriverManager.firefoxdriver().setup();

      if (!seleniumServer.isStarted()) {
        GridLauncherV3.main(new String[] {});
        isFirstServerRun = true;
        if (isChrome) System.setProperty("webdriver.chrome.silentOutput", "true");
      }
    }
  }
}
