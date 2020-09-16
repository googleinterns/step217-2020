package com.google.alpollo.servlets;

import java.io.IOException;
import java.util.ArrayList;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.jayway.jsonpath.JsonPath;

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

      GenericUrl url = new GenericUrl("https://kgsearch.googleapis.com/v1/entities:search");
      url.put("query", artistName);
      url.put("limit", "10");
      url.put("indent", "true");
      url.put("key", ConfigHelper.getSensitiveData(this.getServletContext(), 
          ConfigHelper.SENSITIVE_DATA.API_KEY));

      HttpRequest autocompleteRequest = requestFactory.buildGetRequest(url);
      HttpResponse autocompleteResponse = autocompleteRequest.execute();
      JsonObject responseObject = (JsonObject) JsonParser.parseString(autocompleteResponse.parseAsString());
      JsonArray elements = (JsonArray) responseObject.get("itemListElement");
    } catch (JsonSyntaxException | JsonIOException | IOException autoException) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          autoException.getMessage());
    }
  }
}