package com.google.alpollo.model;

/**
 * Class created to store only sentiment score and magnitude, leaving the other useless properties
 * of the Sentiment object.
 * Class is mainly used for converting its instances to JSON strings.
 */
public class SongSentiment {
  /** Each sentiment has a score, showing the overall positivity of the text, ranging from -1.0 to 1.0. */
  private final float score;
  /** Each sentiment has a magnitude, representing how strong the sentiment is, ranging from 0.0 to +inf. */
  private final float magnitude;

  public SongSentiment(float score, float magnitude) {
    this.score = score;
    this.magnitude = magnitude;
  }

  public float getScore() {
    return score;
  }

  public float getMagnitude() {
    return magnitude;
  }
}