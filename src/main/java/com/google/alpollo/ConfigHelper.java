package com.google.alpollo;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

/**
 * Helps to load configuration file and retrieve data from it.
 */
final public class ConfigHelper {
  private static final String configFileName = "config.json";
  private static final Gson gson = new Gson();

  /**
   * Class which represents the content of configuration file,
   * used only to get Json object from file.
   */
  private static final class ConfigInfo {
    private String projectID;

    public String getProjectID() {
      return projectID;
    }
  }

  private ConfigHelper() {
    throw new RuntimeException("Instantiation of ConfigHelper is not allowed!");
  }

  public static String getProjectID() throws NullPointerException,
      JsonIOException, JsonSyntaxException {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream(configFileName);
    final Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream));
    return gson.fromJson(reader, ConfigInfo.class).getProjectID();
  }
}