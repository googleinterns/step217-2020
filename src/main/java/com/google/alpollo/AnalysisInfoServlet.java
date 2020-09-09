package com.google.alpollo;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.alpollo.model.AnalysisInfo;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/** Servlet retrieves or saves SongInfo objects to the database. */
@WebServlet("/analysis-info")
public class AnalysisInfoServlet extends HttpServlet {
  private static final String SONG_ID = "id";
  private final Gson gson = new Gson();

  /**
   * Making a GET request to our servlet with the desired song ID as a parameter
   * will return that song from the database.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long songId = gson.fromJson(request.getParameter(SONG_ID), Long.class);
    AnalysisInfo analysisInfo = SongDataBase.getAnanlysisInfo(songId);

    response.setContentType("application/json;");
    try {
      response.getWriter().println(gson.toJson(analysisInfo));
    } catch (IOException e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  /**
   * Making a POST request to the server with the analysisInfo object as a
   * parameter (object containing all the info related to our song) will send the
   * song to the storage layer.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    AnalysisInfo analysisInfo;
    try {
      analysisInfo = gson.fromJson(request.getReader(), AnalysisInfo.class);
      SongDataBase.saveAnalysisInfo(analysisInfo);
      SongDataBase.saveSongRequest(analysisInfo.getSong());
    } catch (JsonSyntaxException | JsonIOException | IOException e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}