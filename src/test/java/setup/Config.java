package setup;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.getProperty;

public class Config {
  public static final String WORKSPACE = getProperty("user.dir");

  public static Boolean IS_REMOTE = Boolean.parseBoolean(getProperty("isRemote", "false"));

  private static String deviceName;
  private String url;
  private Map<String, Object> capabilities;

  public Config() {}

  public static String getDeviceName() {
    return deviceName;
  }

  /** sets Web Desired Capabilities */
  void setCapabilities() {
    deviceName = getProperty("deviceName", "chrome");
    capabilities = getDeviceCapabilities(deviceName);
    url = getProperty("seleniumGrid", "http://localhost:4444/wd/hub");
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
  public Map<String, Object> getCapabilities() {
    return capabilities;
  }

  public String getUrl() {
    return url;
  }
  // </editor-fold>
}
