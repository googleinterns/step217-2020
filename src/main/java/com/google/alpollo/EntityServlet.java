package com.google.alpollo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.alpollo.model.SongEntity;
import com.google.cloud.language.v1.Entity;
import com.google.gson.Gson;

@WebServlet("/entity")
public class EntityServlet extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String lyrics = request.getParameter("lyrics");
    AnalysisHelper helper = new AnalysisHelper();

    // Get the Entity list from the API
    List<Entity> entityList = new ArrayList<>(helper.getEntityList(lyrics));

    // Make a new list with simplified entities, they work better in the frontend
    List<SongEntity> simplifiedEntityList = helper.getSimplifiedEntityList(entityList);
    simplifiedEntityList.sort(Collections.reverseOrder(SongEntity.ORDER_BY_SALIENCE));
    List<SongEntity> topSalientEntities = helper.getTopSalientEntities(simplifiedEntityList);

    String json = gson.toJson(topSalientEntities);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}