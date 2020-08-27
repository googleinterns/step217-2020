package com.google.alpollo;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class SongHead extends Song {
  @Id
  private String id;

  /** Number of times users searched for this song in the system. */
  @Index
  private int searchCounter = 0;

  SongHead() { }

  public SongHead(Song song) {
    super(song);
    id = getSongId(song);
  }

  /** Increases by 1 search counter. */
  void increaseSearchCounter() {
    searchCounter++;
  }

  /** Returns the id of the song. */
  static String getSongId(Song song) {
    // TODO: add hashcode instead of concatenation
    return song.getArtist() + song.getName() + song.getAlbum();
  }

}