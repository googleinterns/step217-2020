package com.google.alpollo;

import com.google.gson.Gson;
import java.io.*;
import java.util.Objects;
import javax.servlet.ServletContext;

/**
 * Helps to load configuration file and retrieve data from it.
 */
final public class ConfigHelper {
  private static final String CONFIG_FILE_PATH = "/WEB-INF/config.json";
  private static final Gson gson = new Gson();
  public enum SENSITIVE_DATA { PROJECT_ID, API_KEY }

  /**
   * Class which represents the content of configuration file,
   * used only to get Json object from file.
   */
  private static final class ConfigInfo {
    private String projectID;
    private String apiKey;

    public String getProjectID() {
      return projectID;
    }
    public String getApiKey() { return apiKey; }
  }

  private ConfigHelper() {
    throw new RuntimeException("Instantiation of ConfigHelper is not allowed!");
  }

  /**
   * Retrieves sensitive from configuration file.
   * @return String; {@code null} if configuration file wasn't found
   * or it was incorrect/didn't have this field
   */
  public static String getSensitiveData(ServletContext servletContext, SENSITIVE_DATA typeOfData) {
    try (InputStream inputStream = servletContext.getResourceAsStream(CONFIG_FILE_PATH);
         final Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream))) {
      ConfigInfo configInfo = gson.fromJson(reader, ConfigInfo.class);
      switch (typeOfData) {
        case PROJECT_ID: return configInfo.getProjectID();
        case API_KEY: return configInfo.getApiKey();
        default: return null;
      }
    } catch (Exception parseException) {
      return null;
    }
  }
}
