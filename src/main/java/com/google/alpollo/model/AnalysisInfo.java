package com.google.alpollo.model;

import java.util.List;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Class used for storing data related to the Natural Language API analysis.
 * It provides all the info related to a specific song.
 */
@Entity
public class AnalysisInfo {
  @Id private Long id;
  /** All the info in this class is attached to a specific parent song. */
  @Index private Song song;
  /** Score tells us the overall positivty of the song, ranging from -1.0 to 1.0. */
  @Index private float score;
  /** Magnitude tells us how strong the main sentiment of the song is, ranging from 0.0 to +inf. */
  @Index private float magnitude;
  /** TopSalientEntities is a list containing the 10 most important words, given the song context. */
  @Index private List<SongEntity> topSalientEntities;
  @Index private String lyrics;
  /** After analyzing the entities, we'll have a list of recommended YouTube video IDs. */
  @Index private List<String> youTubeIds;

  /** Objectify requires no argument constructor. Do not use it. */
  private AnalysisInfo() { }
  
  public AnalysisInfo(Song song, float score, float magnitude,
      List<SongEntity> topSalientEntities, String lyrics, List<String> youTubeIds) {
    this.id = song.id();
    this.song = song;
    this.score = score;
    this.magnitude = magnitude;
    this.topSalientEntities = topSalientEntities;
    this.lyrics = lyrics;
    this.youTubeIds = youTubeIds;
  }

  public void initializeId() {
    this.id = this.song.id();
  }

  public Song getSong() {
    return song;
  }
}