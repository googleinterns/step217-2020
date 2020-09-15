package com.google.alpollo.model;

import com.google.alpollo.servlets.EntityServlet;
import com.google.alpollo.servlets.SentimentServlet;

/**
 * Class that contains only lyrics field.
 * Represents body request of the next servlets:
 * @see SentimentServlet
 * @see EntityServlet
 *
 * Used only for deserialization.
 */
public final class Lyrics {
  private final String lyrics;

  public Lyrics(String lyrics) {
    this.lyrics = lyrics;
  }

  public String getLyrics() {
    return lyrics;
  }
}
