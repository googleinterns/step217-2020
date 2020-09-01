package com.google.alpollo;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.alpollo.model.SongEntity;
import com.google.alpollo.model.SongInfo;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

/** Servlet created to deal with songInfo related operations */
@WebServlet("/song-info")
public class SongInfoServlet extends HttpServlet {
  private static final String SONG_ID = "songId";
  private static final String NAME = "name";
  private static final String ARTIST = "artist";
  private static final String SCORE = "score";
  private static final String MAGNITUDE = "magnitude";
  private static final String TOP_SALIENT_ENTITIES = "topSalientEntities";
  private final Gson gson = new Gson();

  /** 
   * Making a GET request to our servlet with the desired song ID as a parameter will return that 
   * song from the database.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    String songId = request.getParameter(SONG_ID);
    SongInfo songInfo = SongDataBase.getSongInfo(songId);

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(songInfo));
  }

  @Override 
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    String name = request.getParameter(NAME);
    String artist = request.getParameter(ARTIST);
    float score = Float.parseFloat(request.getParameter(SCORE));
    float magnitude = Float.parseFloat(request.getParameter(MAGNITUDE));

    Type listType = new TypeToken<List<SongEntity>>(){}.getType();
    List<SongEntity> topSalientEntities = gson.fromJson(
        request.getParameter(TOP_SALIENT_ENTITIES), listType);
  }
}