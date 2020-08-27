package com.google.alpollo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;

/** Servlet that returns a list of the most requested songs. */
@WebServlet("/top")
public class TopSongsServlet extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    response.setContentType("application/json");

    final List<Song> songs = SongDataBase.topSongs();
    /*final List<Song> songs = Arrays.asList(new Song("The Beatles", "Yesterday", ""),
        new Song("Queen", "Bohemian Rhapsody", ""),
        new Song("Eagles", "Hotel California", ""),
        new Song("Little Big", "Go Bananas", ""),
        new Song("Hurts", "Miracle", ""),
        new Song("Black Veil Brides", "In The End", ""),
        new Song("Breaking Benjamin", "Ashes of Eden", ""),
        new Song("The HU ft. Papa Roach", "Wolf Totem", ""),
        new Song("The Weeknd", "Blinding Lights", ""),
        new Song("Panic! At The Disco", "Roaring 20s", ""));*/
    response.getWriter().write(gson.toJson(songs));
  }
}