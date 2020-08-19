package com.google.alpollo;

import java.io.IOException;
import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.Sentiment;

/**
 * Helper class for cleaner code and easier testing.
 */
public class AnalysisHelper {
  
  /**
   * Based on the lyrics sent, the AI can extract the main "sentiment" of the text.
   * This sentiment has a score, showing the overall positivity of the text, ranging from -1 to 1
   * and a magnitude, representing how strong the sentiment is, ranging from 0 to 1
   */
  public Sentiment getSentiment(String lyrics) throws IOException {
    LanguageServiceSettings settings = LanguageServiceSettings.newBuilder().setHeaderProvider(
        FixedHeaderProvider.create("X-Goog-User-Project", "google.com:alpollo-step-2020")).build();
    try (LanguageServiceClient language = LanguageServiceClient.create(settings)) {
      Document doc = Document.newBuilder().setContent(lyrics).setType(Document.Type.PLAIN_TEXT).build();
      Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
      return sentiment;
   }
  }
}