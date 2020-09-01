package com.google.alpollo;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class YouTubeService {
    private static final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    public YouTubeService() {}

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public static YouTube getService(String apiKey) throws GeneralSecurityException, IOException {
      final YouTubeRequestInitializer keyInitializer = new YouTubeRequestInitializer(apiKey);
      final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      return new YouTube.Builder(httpTransport, jsonFactory, null)
          .setApplicationName("YouTube Alpollo")
          .setYouTubeRequestInitializer(keyInitializer)
          .build();
    }
}
