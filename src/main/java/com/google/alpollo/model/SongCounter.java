package com.google.alpollo.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Id;

/** Represents the structure of the song to save search counter in the database. */
@Entity
public class SongCounter {
  @Id private Long id;
  @Index private Song song;
  /** Number of times users searched for this song in the system. */
  @Index private int searchCounter = 0;

  /** Objectify requires no argument constructor. Do not use it. */
  private SongCounter() { }

  public SongCounter(Song song) {
    this.song = song;
    id = song.id();
  }

  public Song getSong() {
    return song;
  }

  /** Increments search counter by 1. */
  public void incrementSearchCounter() {
    searchCounter++;
  }

  public int getSearchCounter() {
    return searchCounter;
  }
}