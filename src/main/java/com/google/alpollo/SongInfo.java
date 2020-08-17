package com.google.alpollo;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.language.v1.Entity;

/**
 * This class will be used to store all the results of the analysis.
 * For now, it's just gonna store the name, score and entityList resulted from the analysis
 * for easier and more elegant use.
 * In the future it could include albumName, genre, lyrics.
 */

public class SongInfo {
  private String songName;
  private double score;
  private List<Entity> entityList = new ArrayList<>();

  public SongInfo(String songName, double score, List<Entity> entityList) {
    this.songName = songName;
    this.score = score;
    this.entityList = entityList;
  }   

	public String getSongName() {
    return songName;
  }

  public double getSentimentScore() {
    return score;
  }

  public List<Entity> getEntityList() {
    return entityList;
  }
}