package com.google.alpollo.model;

import com.google.alpollo.helpers.AnalysisHelper;
import com.googlecode.objectify.annotation.Index;

/**
 * Class created to store only sentiment score and magnitude, leaving the other
 * useless properties of the Sentiment object. Class is mainly used for
 * converting its instances to JSON strings.
 */
public class SongSentiment {
  /** Each sentiment has a score, showing the overall positivity of the text, ranging from -1.0 to 1.0. */
  @Index private float score;
  /** Each sentiment has a magnitude, representing how strong the sentiment is, ranging from 0.0 to +inf. */
  @Index private float magnitude;
  /** Based on the score and magnitude of a sentiment, we can make up an interpretation of these values. */
  @Index private String interpretation;

  /** Objectify requires no argument constructor. Do not use it. */
  private SongSentiment() { }

  public SongSentiment(float score, float magnitude) {
    this.score = score;
    this.magnitude = magnitude;
    this.interpretation = AnalysisHelper.getInterpretation(score, magnitude);
  }

  public float getScore() {
    return score;
  }

  public float getMagnitude() {
    return magnitude;
  }

  public String getInterpretation() {
    return interpretation;
  }
}