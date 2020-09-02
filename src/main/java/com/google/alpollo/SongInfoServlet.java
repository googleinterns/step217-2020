package com.google.alpollo;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.alpollo.model.SongInfo;
import com.google.gson.Gson;

/** Servlet retrieves or saves SongInfo objects to the database. */
@WebServlet("/song-info")
public class SongInfoServlet extends HttpServlet {
  private static final String SONG_ID = "id";
  private static final String SONG_INFO = "songInfo";
  private final Gson gson = new Gson();

  /**
   * Making a GET request to our servlet with the desired song ID as a parameter
   * will return that song from the database.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String songId = request.getParameter(SONG_ID);
    SongInfo songInfo = SongDataBase.getSongInfo(songId);

    response.setContentType("application/json;");
    try {
      response.getWriter().println(gson.toJson(songInfo));
    } catch (IOException e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
        e.getMessage());
    }
  }

  /**
   * Making a POST request to the server with the songInfo object as a parameter (object containing
   * all the info related to our song) will send the song to the storage layer. 
   */
  @Override 
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    SongInfo songInfo = gson.fromJson(request.getParameter(SONG_INFO), SongInfo.class);
    SongDataBase.saveSongInfo(songInfo);
  }
}