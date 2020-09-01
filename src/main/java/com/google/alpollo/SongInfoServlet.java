package com.google.alpollo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.alpollo.model.SongInfo;
import com.google.gson.Gson;

@WebServlet("/song-info")
public class SongInfoServlet extends HttpServlet {
  private static final String SONG_ID = "songId";
  private final Gson gson = new Gson();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    String songId = request.getParameter(SONG_ID);
    SongInfo songInfo = SongDataBase.getSongInfo(songId);

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(songInfo));
  }
}