package com.google.alpollo.model;

/**
 * Class that contains only lyrics field.
 * Represents body request of the next servlets:
 * @see AutocompleteServlet
 *
 * Used only for deserialization.
 */
public final class AutocompleteSearchRequest {
    private String searchString;
    private String type;

    public AutocompleteSearchRequest(String searchString, String type) {
      this.searchString = searchString;
      this.type = type;
    }

    public String getSearchString() {
      return searchString;
    }

    public String getType() {
      return type;
    }
  }