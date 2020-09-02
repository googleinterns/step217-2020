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
  /** Based on the score and magnitude of a sentiment, we can make up an interpretation of these values. */
  private final String interpretation;

  public SongSentiment(float score, float magnitude, String interpretation) {
    this.score = score;
    this.magnitude = magnitude;
    this.interpretation = interpretation;
  }

  public float getScore() {
    return score;
  }

  public float getMagnitude() {
    return magnitude;
  }
}