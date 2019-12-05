package setup;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.HashMap;

import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;

public class Config {
  public static final String WORKSPACE = getProperty("user.dir");
  public static String USER = getProperty("user", "user1");
  public static String USERNAME = getProperty("username", "notSet");
  public static String PASSWORD = getProperty("password", "notSet");
  public static String OS = getProperty("os.name").toLowerCase();

  public static Boolean IS_REMOTE = Boolean.parseBoolean(getProperty("isRemote", "false"));

  private static String deviceName;
  private String url;
  private DesiredCapabilities capabilities;
  private boolean isAndroid;
  private boolean isIos;
  private boolean isWeb;
  private boolean isMobile;
  private String platform;
  private String appName;

  public Config() {
    platform = getProperty("platform", "Android");
    appName = getProperty("appName", "notSet");
  }

  /** Set Capabilities for the Platform: Android, iOS or Web */
  public void setCapabilitiesForPlatform() {
    isAndroid = platform.equalsIgnoreCase("Android");
    isIos = platform.equalsIgnoreCase("iOS");
    isWeb = platform.equalsIgnoreCase("Web");

    if (isAndroid || isIos) isMobile = true;

    if (isAndroid) setAndroidCapabilities();
    if (isIos) setIosCapabilities();
    if (isWeb) setWebCapabilities();
  }

  /** sets Web Desired Capabilities */
  private void setWebCapabilities() {
    deviceName = getProperty("deviceName", "chrome");
    capabilities = new DesiredCapabilities(getDeviceCapabilities(deviceName));
    url = getProperty("seleniumGrid", "http://localhost:4444/wd/hub");
  }

  /** sets iOS Desired Capabilities */
  private void setIosCapabilities() {
    deviceName = getProperty("deviceName", "iPhone x");
    url = getProperty("seleniumGrid", "http://0.0.0.0:4723/wd/hub");

    capabilities = new DesiredCapabilities();
    capabilities.setCapability("app", Paths.get(WORKSPACE, "apps", getAppName()).toString());
    capabilities.setCapability("platformName", "iOS");
    capabilities.setCapability("automationName", "XCUITest");
    capabilities.setCapability("xcodeOrgId", getProperty("xcodeSigningId", "iPhone Developer"));
  }

  /** sets Android Desired Capabailities */
  private void setAndroidCapabilities() {
    deviceName = getProperty("deviceName", "emulator-5554");
    url = getProperty("seleniumGrid", "http://0.0.0.0:4723/wd/hub");

    capabilities = new DesiredCapabilities();

    capabilities.setCapability("deviceName", getDeviceName());
    capabilities.setCapability("platformName", "Android");
    capabilities.setCapability("automationName", "UiAutomator2");
    capabilities.setCapability("app", Paths.get(WORKSPACE, "apps", getAppName()).toString());
    capabilities.setCapability("systemPort", parseInt(getProperty("systemPort", "8200")));
    capabilities.setCapability("autoGrantPermissions", true);
  }

  /**
   * Deserialize json into desired capabilities map
   *
   * @param device
   * @return desired capabilities map created from json
   */
  private HashMap<String, Object> getDeviceCapabilities(String device) {
    InputStream file;
    BufferedReader reader;
    Type hashType;
    JsonElement jsonElement;

    file = getClass().getResourceAsStream("/jsonData/devices.json");
    reader = new BufferedReader(new InputStreamReader(file));
    hashType = new TypeToken<HashMap<String, Object>>() {}.getType();
    jsonElement = new JsonParser().parse(reader).getAsJsonObject().get(device);

    return new Gson().fromJson(jsonElement, hashType);
  }

  // <editor-fold desc="Get and Sets">

  public DesiredCapabilities getCapabilities() {
    return capabilities;
  }

  public boolean isAndroid() {
    return isAndroid;
  }

  public boolean isIos() {
    return isIos;
  }

  public boolean isWeb() {
    return isWeb;
  }

  public boolean isMobile() {
    return isMobile;
  }

  public String getUrl() {
    return url;
  }

  String getPlatform() {
    return platform;
  }

  public static String getDeviceName() {
    return deviceName;
  }

  public String getAppName() {
    return appName;
  }
  // </editor-fold>
}
