package com.google.alpollo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/song-info")
public class SongInfoServlet extends HttpServlet {
  private static final String SONG_ID = "songId";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    String songId = request.getParameter(SONG_ID);
    SongInfo songInfo = SongDataBase.getSongInfo(songId);
  }
}