package com.google.alpollo;

import java.io.IOException;
import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.Sentiment;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * Helper class with methods that use the Natural Language API or use the data that comes
 * from the API
 */
public class AnalysisHelper {
  private final Gson gson = new Gson();

  /**
   * Based on the lyrics sent, the AI can extract the main "sentiment" of the text.
   * This sentiment has a score, showing the overall positivity of the text, ranging from -1.0 to 1.0
   * and a magnitude, representing how strong the sentiment is, ranging from 0.0 to +inf
   */
  public Sentiment getSentiment(String lyrics) throws IOException, NullPointerException,
      JsonIOException, JsonSyntaxException {
    String projectID = ConfigHelper.getProjectID();

    // Set the header manually so we can use the Natural Language API.
    LanguageServiceSettings settings = LanguageServiceSettings.newBuilder().setHeaderProvider(
        FixedHeaderProvider.create("X-Goog-User-Project", projectID)).build();
    try (LanguageServiceClient language = LanguageServiceClient.create(settings)) {
      Document doc = Document.newBuilder().setContent(lyrics).setType(Document.Type.PLAIN_TEXT).build();
      Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
      return sentiment;
    }
  }
}