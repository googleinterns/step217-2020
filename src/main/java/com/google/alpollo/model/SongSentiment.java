package com.google.alpollo.model;

/**
 * Class created to store only sentiment score and magnitude, leaving the other useless properties
 * of the Sentiment object.
 * Class is mainly used for converting its instances to JSON strings.
 */
public class SongSentiment {
  private double score;
  private double magnitude;

  public SongSentiment(double score, double magnitude) {
    this.score = score;
    this.magnitude = magnitude;
  }
}