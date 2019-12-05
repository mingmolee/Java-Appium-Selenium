package setup;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.grid.internal.utils.configuration.StandaloneConfiguration;
import org.openqa.grid.selenium.GridLauncherV3;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.server.SeleniumServer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.file.Paths;

import static setup.Config.WORKSPACE;

public class DriverFactory {

  private static boolean isFirstServerRun = false;
  private Config config = new Config();
  private URL url;
  private DesiredCapabilities capabilities;
  private AppiumDriverLocalService service;

  public DriverFactory(Config config) throws MalformedURLException {
    this.url = new URL(config.getUrl());
    this.capabilities = new DesiredCapabilities(config.getCapabilities());
  }

  /** Creates the driver with the given capabilities */
  RemoteWebDriver createDriver() {
    String platform = config.getPlatform().toUpperCase();

    switch (platform) {
      case "ANDROID":
        if (Config.getDeviceName().contains("emulator")) {
          startAndroidEmulation();
        }
        startAppiumServer();
        return new AndroidDriver<MobileElement>(url, capabilities);
      case "IOS":
        startAppiumServer();
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
      boolean isEdge = Config.getDeviceName().equalsIgnoreCase("edge");

      if (isChrome) WebDriverManager.chromedriver().setup();
      else if (isFireFox) WebDriverManager.firefoxdriver().setup();
      else if (isEdge) WebDriverManager.edgedriver().setup();
      else System.out.printf("Incompatible device name: [%s]%n", Config.getDeviceName());

      if (!seleniumServer.isStarted()) {
        GridLauncherV3.main(new String[] {});
        isFirstServerRun = true;
        if (isChrome) System.setProperty("webdriver.chrome.silentOutput", "true");
      }
    }
  }

  /**
   * Start the Appium Server with default services
   *
   * @return
   */
  public AppiumDriverLocalService startAppiumServer() {
    boolean serverIsNotRunning = checkIfServerIsRunning(4723);

    if (!serverIsNotRunning) {
      service = AppiumDriverLocalService.buildDefaultService();
      service.start();
    }
    return service;
  }

  /**
   * Check if the Appium Server is Running on port
   *
   * @param port
   * @return
   */
  private static boolean checkIfServerIsRunning(int port) {
    boolean isServerRunning = false;

    ServerSocket serverSocket;
    try {
      serverSocket = new ServerSocket(port);
      serverSocket.close();
    } catch (IOException e) {
      // If control reaches this point, then the port is in use
      isServerRunning = true;
    } finally {
      serverSocket = null;
    }
    return isServerRunning;
  }

  /** Start Android Emulator */
  private void startAndroidEmulation() {

    boolean isWindows = Config.OS.equalsIgnoreCase("windows");
    String scriptsPath = Paths.get(WORKSPACE, "src", "resources", "scripts").toString();

    try {
      if (isWindows) {
        Runtime.getRuntime().exec(Paths.get(scriptsPath, "startAndroidEmulator.bat").toString());
      } else {
        Runtime.getRuntime().exec("cd $ANDROID_HOME/emulator");
        Runtime.getRuntime().exec("emulator -avd emulator-5554");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
