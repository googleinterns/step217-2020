package com.google.alpollo;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.language.v1.Entity;

public class SongInfo {
  private String songName;
  private int score;
  private List<Entity> entityList = new ArrayList<>();

  public SongInfo(String songName, int score, List<Entity> entityList) {
    this.songName = songName;
    this.score = score;
    this.entityList = entityList;
  }   

	public String getSongName() {
    return songName;
  }

  public int getSentimentScore() {
    return score;
  }

  public List<Entity> getEntityList() {
    return entityList;
  }
}