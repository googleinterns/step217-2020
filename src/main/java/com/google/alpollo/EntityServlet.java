package com.google.alpollo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
  private static String LYRICS;

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      LYRICS = request.getParameter("lyrics");

      // Get the Entity list from the API
      List<Entity> entityList = new ArrayList<>(AnalysisHelper.getEntityList(LYRICS));
      List<SongEntity> simplifiedEntityList = AnalysisHelper.getSimplifiedEntityList(entityList);
      simplifiedEntityList.sort(SongEntity.ORDER_BY_SALIENCE_DESCENDING);
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