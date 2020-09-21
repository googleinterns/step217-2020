package com.google.alpollo.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.alpollo.helpers.AnalysisHelper;
import com.google.alpollo.helpers.ConfigHelper;
import com.google.alpollo.model.Lyrics;
import com.google.alpollo.model.SongSentiment;
import com.google.cloud.language.v1.Sentiment;
import com.google.gson.Gson;

/**
 * Sending a POST request with the lyrics of the song as a parameter will give the Client
 * the overall positivity of the song and how strong the sentiment of the song is.
 */
@WebServlet("/sentiment")
public class SentimentServlet extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      final String lyrics = gson.fromJson(request.getReader(), Lyrics.class).getLyrics();
      String projectID = ConfigHelper.getSensitiveData(this.getServletContext(), ConfigHelper.SENSITIVE_DATA.PROJECT_ID);

      Sentiment sentiment = AnalysisHelper.getSentiment(projectID, lyrics);
      SongSentiment songSentiment = new SongSentiment(sentiment.getScore(), sentiment.getMagnitude());

      String json = gson.toJson(songSentiment);
      response.setContentType("application/json;");
      response.getWriter().println(json);
    } catch (IllegalStateException | IOException sentimentException) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
              sentimentException.getMessage());
    }
  }
}