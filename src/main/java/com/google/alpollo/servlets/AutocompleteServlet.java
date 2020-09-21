package com.google.alpollo.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

@WebServlet("/autocomplete")
public class AutocompleteServlet extends HttpServlet {
  private final Gson gson = new Gson();
  private final static String SEARCH_STRING = "searchString";
  private final static String TYPE = "type";
  private final static String LIMIT = "10";

  private enum SearchType {
    ARTIST, SONG;
  };

  /**
   * Making a POST request to this servlet with a search string and the type
   * searched for as parameters will make a call to the Knowledge Graph Search API
   * and it will return a list of possible search results.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<String> typeList;
    try {
      String searchString = request.getParameter(SEARCH_STRING);
      SearchType type = SearchType.valueOf(request.getParameter(TYPE));
      switch (type) {
        case ARTIST:
          typeList = Arrays.asList("Person", "MusicGroup");
          break;
        case SONG:
          typeList = Arrays.asList("MusicRecording");
          break;
        default:
          typeList = new ArrayList<>();
      }

      HttpTransport httpTransport = new NetHttpTransport();
      HttpRequestFactory requestFactory = httpTransport.createRequestFactory();

      GenericUrl url = new GenericUrl("https://kgsearch.googleapis.com/v1/entities:search");
      url.put("query", searchString);
      url.put("limit", LIMIT);
      url.put("types", typeList);
      url.put("key", ConfigHelper.getSensitiveData(this.getServletContext(), ConfigHelper.SENSITIVE_DATA.API_KEY));

      HttpRequest autocompleteRequest = requestFactory.buildGetRequest(url);
      HttpResponse autocompleteResponse = autocompleteRequest.execute();
      configJsonPathWithGson();
      JsonObject responseObject = (JsonObject) JsonParser.parseString(autocompleteResponse.parseAsString());
      JsonArray elements = (JsonArray) responseObject.get("itemListElement");

      List<String> results = new ArrayList<>();
      for (Object element : elements) {
        results.add(JsonPath.read(element, "$.result.name").toString().replace("\"", ""));
      }

      String json = gson.toJson(results);
      response.setContentType("application/json");
      response.getWriter().println(json);
    } catch (JsonSyntaxException | JsonIOException | IOException autoException) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, autoException.getMessage());
    } catch (IllegalArgumentException enumException) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Type not supported.");
    }
  }

  private void configJsonPathWithGson() {
    Configuration.setDefaults(new Configuration.Defaults() {
      private final JsonProvider jsonProvider = new GsonJsonProvider();
      private final MappingProvider mappingProvider = new GsonMappingProvider();
        
      @Override
      public JsonProvider jsonProvider() {
          return jsonProvider;
      }
  
      @Override
      public MappingProvider mappingProvider() {
          return mappingProvider;
      }
      
      @Override
      public Set<Option> options() {
          return EnumSet.noneOf(Option.class);
      }
    });
  }
}