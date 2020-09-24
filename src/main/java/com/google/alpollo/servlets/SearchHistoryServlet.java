package com.google.alpollo.servlets;

import com.google.alpollo.model.Song;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.alpollo.database.SongDataBase;

/** Servlet that returns a list of no more than 10 songs that the user most searched for. */
@WebServlet("/history")
public class SearchHistoryServlet extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    response.setContentType("application/json");

    UserService userService = UserServiceFactory.getUserService();
    
    if (userService.isUserLoggedIn()) {
      String userId = userService.getCurrentUser().getUserId();
      final List<Song> songs = SongDataBase.searchHistory(userId);
      response.getWriter().write(gson.toJson(songs));
    } else {
      response.getWriter().write(gson.toJson("Please authorize :)"));
    }
  }
}