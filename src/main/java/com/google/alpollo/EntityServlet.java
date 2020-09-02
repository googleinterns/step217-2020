package com.google.alpollo;

import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.alpollo.model.Lyrics;
import com.google.alpollo.model.SongEntity;
import com.google.cloud.language.v1.Entity;
import com.google.gson.Gson;

/**
 * Sending a POST request with the lyrics of the song as a parameter will return to the client
 * a list of the most important entities (people, places, things) found in that song.
 */
@WebServlet("/entity")
public class EntityServlet extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      final String lyrics = gson.fromJson(request.getReader(), Lyrics.class).getLyrics();
      String projectID = ConfigHelper.getSensitiveData(this.getServletContext(), ConfigHelper.SENSITIVE_DATA.PROJECT_ID);
      // Get the Entity list from the API
      List<Entity> entityList = AnalysisHelper.getEntityList(projectID, lyrics);
      List<SongEntity> simplifiedEntityList = AnalysisHelper.getSimplifiedEntityList(entityList);
      List<SongEntity> topSalientEntities = AnalysisHelper.getTopSalientEntities(simplifiedEntityList);

      String json = gson.toJson(topSalientEntities);
      response.setContentType("application/json;");
      response.getWriter().println(json);
    } catch (IllegalStateException | IOException entityException) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            entityException.getMessage());
    }
  }
}