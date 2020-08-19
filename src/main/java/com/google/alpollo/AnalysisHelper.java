package com.google.alpollo;

import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.Sentiment;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

/**
 * Helper class for cleaner code and easier testing.
 */

public class AnalysisHelper {
  private final Gson gson = new Gson();

  /**
   * Based on the lyrics sent, the AI can extract the main "sentiment" of the text.
   * This sentiment has a score, showing the overall positivity of the text, ranging from -1 to 1
   * and a magnitude, representing how strong the sentiment is, ranging from 0 to 1
   */
  public Sentiment getSentiment(String lyrics) throws IOException, NullPointerException,
      JsonIOException, JsonSyntaxException {
    Document doc =
        Document.newBuilder().setContent(lyrics).setType(Document.Type.PLAIN_TEXT).build();

    String projectID = ConfigHelper.getProjectID();

    // Set the header manually so we can use the Natural Language API.
    LanguageServiceSettings settings = LanguageServiceSettings.newBuilder().setHeaderProvider(
        FixedHeaderProvider.create("X-Goog-User-Project", projectID)).build();
    LanguageServiceClient languageService = LanguageServiceClient.create(settings);
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    languageService.close();
    return sentiment;
  }
}