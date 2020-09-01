package com.google.alpollo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.alpollo.model.SongInfo;
import com.google.gson.Gson;

/** Servlet created to deal with songInfo related operations */
@WebServlet("/song-info")
public class SongInfoServlet extends HttpServlet {
  private static final String SONG_ID = "songId";
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
}