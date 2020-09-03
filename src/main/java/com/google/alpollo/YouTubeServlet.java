package com.google.alpollo;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet that returns 5 the most popular videos for the query.
 */
@WebServlet("/youtube")
public class YouTubeServlet extends HttpServlet {
  private static final String MUSIC_TOPIC_ID = "/m/04rlf";

  /**
   * Accesses YouTube and retrieves a collection of videos relating to the given query/words found
   * in the request. Returns only the snippet for each video and returns only videos, no playlists
   * or channels allowed.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    response.setContentType("application/json");
    String query = request.getParameter("query");
    if (query.equals("")){
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty query.");
      return;
    }

    YouTube youtubeService = null;
    try {
      youtubeService = YouTubeService.getService(ConfigHelper.getSensitiveData(this.getServletContext(), ConfigHelper.SENSITIVE_DATA.API_KEY));
    } catch (Exception e) {
      response.getWriter().println(gson.toJson(e));
    }
    if (youtubeService == null) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "YouTube service is null.");
      return;
    }

    // Define and execute the API request
    YouTube.Search.List vidRequest = youtubeService.search().list("snippet");
    SearchListResponse vidResponse =
        vidRequest.setMaxResults(5L).setQ(query).setTopicId(MUSIC_TOPIC_ID).setOrder("viewCount").setType("video").execute();
    List<String> videoIds = vidResponse.getItems().stream().map(searchResult -> searchResult.getId().getVideoId()).collect(Collectors.toList());

    response.getWriter().println(gson.toJson(videoIds));
  }
}