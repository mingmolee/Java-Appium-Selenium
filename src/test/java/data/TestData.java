package data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestData {
  public final User user;

  public TestData(String username) {
    this.user = setUserData(username);
  }

  private User setUserData(String username) {
    InputStream resourceJsonFile;
    BufferedReader reader;
    JsonElement jsonElement;

    resourceJsonFile = getClass().getResourceAsStream(String.format("/jsonData/%s.json", username));
    reader = new BufferedReader(new InputStreamReader(resourceJsonFile));
    jsonElement = new JsonParser().parse(reader).getAsJsonObject();

    User user = new Gson().fromJson(jsonElement, User.class);
    return user;
  }

  public static class User {
    public String user;
    public String username;
    public String password;
  }
}
