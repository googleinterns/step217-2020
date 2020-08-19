package com.google.alpollo.model;

/**
 * Class created to store only sentiment score and magnitude, leaving the other useless properties
 * of the Sentiment object.
 * Class is mainly used for converting its instances to JSON strings.
 */
public class SongSentiment {
  private final float score;
  private final float magnitude;

  public SongSentiment(float score, float magnitude) {
    this.score = score;
    this.magnitude = magnitude;
  }
}