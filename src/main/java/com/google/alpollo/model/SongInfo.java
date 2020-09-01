package com.google.alpollo.model;

import java.util.List;
import com.google.alpollo.Song;
import com.google.alpollo.model.SongEntity;

/**
 * Class used for storing data related to the Natural Language API analysis.
 * Extending the Song class, it provides all the info related to a specific song.
 * 
 * We extend from Song because we do not always need the entire song data. Sasha mostly
 * needs to work with the "superficial" data (artist name, song name etc.) while Budi has to 
 * work with the analysis results.
 */
public class SongInfo extends Song {
  /** Score tells us the overall positivty of the song, ranging from -1.0 to 1.0. */
  private final float score;
  /** Magnitude tells us how strong the main sentiment of the song is, ranging from 0.0 to +inf. */
  private final float magnitude;
  /** TopSalientEntities is a list containing the 10 most important words, given the song context. */
  private final List<SongEntity> topSalientEntities;
  private final String lyrics;
  /** After analyzing the entities, we'll have a list of recommended YouTube links. */
  private final List<String> youTubeLinks;

  public SongInfo(String artist, String name, String album, float score, float magnitude,
      List<SongEntity> topSalientEntities, String lyrics, List<String> youTubeLinks) {
    super(artist, name, album);
    this.score = score;
    this.magnitude = magnitude;
    this.topSalientEntities = topSalientEntities;
    this.lyrics = lyrics;
    this.youTubeLinks = youTubeLinks;
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