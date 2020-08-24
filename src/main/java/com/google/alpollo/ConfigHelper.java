package com.google.alpollo;

import com.google.gson.Gson;
import java.io.*;
import java.util.Objects;

/**
 * Helps to load configuration file and retrieve data from it.
 */
final public class ConfigHelper {
  private static final String CONFIG_FILE_NAME = "config.json";
  private static final String CONFIG_FILE_PATH = "WEB-INF/config.json";
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

  /**
   * Retrieves projectID from configuration file.
   * @return projectID; {@code null} if configuration file wasn't found
   * or it was incorrect/didn't have projectID field
   */
  public static String getProjectID() {
    try (InputStream inputStream = new FileInputStream(new File(CONFIG_FILE_PATH));
         final Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream))) {
      return gson.fromJson(reader, ConfigInfo.class).getProjectID();
    } catch (Exception parseException) {
      try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(CONFIG_FILE_NAME);
           final Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream))) {
        return gson.fromJson(reader, ConfigInfo.class).getProjectID();
      } catch (Exception exception) {
        return null;
      }
    }
  }
}
