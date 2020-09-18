package com.google.alpollo;

import com.google.alpollo.model.SongCounter;
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

/** Servlet that returns a list of no more than 10 songs that the user most searched for. */
@WebServlet("/top")
public class SearchHistoryServlet extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    response.setContentType("application/json");

    UserService userService = UserServiceFactory.getUserService();
    String userId = userService.getCurrentUser().getUserId();

    if (userService.isUserLoggedIn()) {
      final List<SongCounter> songs = SongDataBase.searchHistory(userId);
      response.getWriter().write(gson.toJson(songs));
    } else {
      response.getWriter().write(gson.toJson("Please authorize :)"));
    }
  }
}