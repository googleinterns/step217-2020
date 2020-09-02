package com.google.alpollo.model;

/**
 * Class that contains only lyrics field.
 * Represents body request of the next servlets:
 * @see com.google.alpollo.SentimentServlet
 * @see com.google.alpollo.EntityServlet
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
