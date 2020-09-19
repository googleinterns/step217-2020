package com.google.alpollo;

import com.google.cloud.translate.v3.DetectLanguageRequest;
import com.google.cloud.translate.v3.DetectLanguageResponse;
import com.google.cloud.translate.v3.DetectedLanguage;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.google.cloud.translate.v3.TranslationServiceSettings;
import com.google.api.gax.rpc.FixedHeaderProvider;
import java.io.IOException;
import java.util.List;

public class DetectLanguageService {
  private static TranslationServiceSettings settings;

  private DetectLanguageService() {}

  /**
   * Creates setting for Translation service. Adds special header to make it
   * work on Cloud Shell.
   * @param projectID
   * @throws IOException
   */
  private static void createTranslationServiceSettings(String projectID) throws IOException {
    if (projectID == null) {
      throw new IllegalStateException("Project ID wasn't defined.");
    }

    if (settings == null) {
      settings = TranslationServiceSettings.newBuilder().setHeaderProvider(
          FixedHeaderProvider.create("X-Goog-User-Project", projectID)).build();
    }
  }

  /**
   * Generates speech audio file from the text.
   *
   * @param text the text for convertion
   * @return ByteString of audio; {@code null} if something went wrong
   */
  public static String detectLanguage(String text, String projectID) throws IOException {
    createTranslationServiceSettings(projectID);

    try (TranslationServiceClient client = TranslationServiceClient.create(settings)) {
      LocationName parent = LocationName.of(projectID, "global");

      DetectLanguageRequest request =
        DetectLanguageRequest.newBuilder()
            .setParent(parent.toString())
            .setMimeType("text/plain")
            .setContent(text)
            .build();

      List<DetectedLanguage> response = client.detectLanguage(request).getLanguagesList();

      /* Return null if language wasn't detected or 
         return the code of the language with the largest confidence. */
      if (response.isEmpty()) {
        return null;
      } else {
        return response.get(0).getLanguageCode();
      }
    }
  }
}
