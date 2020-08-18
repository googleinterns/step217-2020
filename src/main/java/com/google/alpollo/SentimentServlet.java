package com.google.alpollo;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.api.gax.rpc.HeaderProvider;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.LanguageServiceSettings.Builder;
import com.google.cloud.language.v1.Sentiment;
import com.google.gson.Gson;

@WebServlet("/sentiment")
public class SentimentServlet extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String lyrics = request.getParameter("lyrics");
    Sentiment sentiment = getSentiment(lyrics);
    
    String json = gson.toJson(sentiment);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  /**
   * Based on the lyrics sent, the AI can extract the main "sentiment" of the text.
   * This sentiment has a score, showing the overall positivity of the text, ranging from -1 to 1
   * and a magnitude, representing how strong the sentiment is, ranging from 0 to 1
   */
  public Sentiment getSentiment(String lyrics) throws IOException {
    Document doc =
        Document.newBuilder().setContent(lyrics).setType(Document.Type.PLAIN_TEXT).build();
    
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    languageService.close();
    return sentiment;
  }
}