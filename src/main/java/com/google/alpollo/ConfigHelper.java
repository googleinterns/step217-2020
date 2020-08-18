package com.google.alpollo;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public class ConfigHelper {
    private String configFileName = "config.json";
    static final private Gson gson = new Gson();

    static final private class ConfigInfo {
        private String projectID;

        public String getProjectID() {
            return projectID;
        }
    }

    static public String getProjectID() throws Exception {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("config.json");
        final Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream));
        return gson.fromJson(reader, ConfigInfo.class).getProjectID();
    }
}
