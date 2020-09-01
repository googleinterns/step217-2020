package com.google.alpollo;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

/** Represents the structure of the song to save search counter in the database. */
@Entity
public class SongCounter extends Song {
  /** Number of times users searched for this song in the system. */
  @Index private int searchCounter = 0;

  private SongCounter() { }

  public SongCounter(Song song) {
    super(song);
  }

  /** Increases by 1 search counter. */
  void increaseSearchCounter() {
    searchCounter++;
  }
}