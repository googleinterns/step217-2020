package com.google.alpollo.model;

/**
 * Class used to store only sentiment score and magnitude.
 * It will be easier to send necessary info to front-end as a JSON.
 */
public class SongSentiment {
  private double score;
  private double magnitude;

  public SongSentiment(double score, double magnitude) {
    this.score = score;
    this.magnitude = magnitude;
  }
}