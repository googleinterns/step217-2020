package com.google.alpollo.helpers;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Class for creating YouTube service.
 */
public final class YouTubeService {
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  private YouTubeService() {}

  /**
   * Build and return an authorized API client service.
   *
   * @return an authorized API client service
   * @throws GeneralSecurityException, IOException
   */
  public static YouTube getService(String apiKey) throws GeneralSecurityException, IOException {
    final YouTubeRequestInitializer keyInitializer = new YouTubeRequestInitializer(apiKey);
    final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
        .setApplicationName("YouTube Alpollo")
        .setYouTubeRequestInitializer(keyInitializer)
        .build();
  }
}
