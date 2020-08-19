package com.google.alpollo;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.alpollo.model.SongSentiment;
import com.google.cloud.language.v1.Sentiment;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

@WebServlet("/sentiment")
public class SentimentServlet extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String lyrics = request.getParameter("lyrics");
    AnalysisHelper helper = new AnalysisHelper();
    try {
      Sentiment sentiment = helper.getSentiment(lyrics);
      SongSentiment songSentiment = new SongSentiment(sentiment.getScore(), sentiment.getMagnitude());

      String json = gson.toJson(songSentiment);
      response.setContentType("application/json;");
      response.getWriter().println(json);
    } catch (JsonIOException | JsonSyntaxException jsonException){
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
              "Json file with configuration info is invalid");
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND,
              "Internal error occurred or config.json file not found");
    }
  }
}