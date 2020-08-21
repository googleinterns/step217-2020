package com.google.alpollo;

import java.util.List;
import com.google.alpollo.model.SongEntity;

/**
 * Class used for storing data related to the Natural Language API analysis.
 * Extending the Song class, it provides all the info related to a specific song.
 */
public class SongInfo extends Song{
  /** Score tells us the overall positivty of the song, ranging from -1.0 to 1.0.*/
  private final float score;
  /** Magnitude tells us how strong the main sentiment of the song is, ranging from 0.0 to +inf. */
  private final float magnitude;
  /** TopSalientEntities is a list containing the 10 most important words, given the song context.*/
  private final List<SongEntity> topSalientEntities;

  public SongInfo(String artist, String name, String album, float score, float magnitude,
      List<SongEntity> topSalientEntities) {
    super(artist, name, album);
    this.score = score;
    this.magnitude = magnitude;
    this.topSalientEntities = topSalientEntities;
  }

  public float getScore() {
    return score;
  }

  public float getMagnitude() {
    return magnitude;
  }

  public List<SongEntity> getTopSalientEntities() {
    return topSalientEntities;
  }
}