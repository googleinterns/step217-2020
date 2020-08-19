package com.google.alpollo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    response.getWriter().write(gson.toJson(songs));
  }
}