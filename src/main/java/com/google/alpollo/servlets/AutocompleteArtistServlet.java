package com.google.alpollo.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.alpollo.helpers.ConfigHelper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet("/autocomplete-artist")
public class AutocompleteArtistServlet extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String artistName;
    try {
      artistName = gson.fromJson(request.getReader(), String.class);

      HttpTransport httpTransport = new NetHttpTransport();
      HttpRequestFactory requestFactory = httpTransport.createRequestFactory();

      JSONParser parser = new JSONParser();

      GenericUrl url = new GenericUrl("https://kgsearch.googleapis.com/v1/entities:search");
      url.put("query", artistName);
      url.put("limit", "5");
      url.put("types", Arrays.asList("MusicGroup", "Person"));
      url.put("indent", "true");
      url.put("key", ConfigHelper.getSensitiveData(this.getServletContext(), ConfigHelper.SENSITIVE_DATA.API_KEY));

      HttpRequest autocompleteRequest = requestFactory.buildGetRequest(url);
      HttpResponse autocompleteResponse = autocompleteRequest.execute();
      JSONObject responseObject = (JSONObject) parser.parse(autocompleteResponse.parseAsString());
      JSONArray elements = (JSONArray) responseObject.get("itemListElement");

      List<String> artists = new ArrayList<>();
      for (Object element : elements) {
        artists.add(JsonPath.read(element, "$.result.name").toString());
      }

      String json = gson.toJson(artists);
      response.setContentType("application/json");
      response.getWriter().println(json);
    } catch (JsonSyntaxException | JsonIOException | IOException | ParseException autoException) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, autoException.getMessage());
    }
  }
}