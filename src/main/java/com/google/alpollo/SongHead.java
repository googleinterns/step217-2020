package com.google.alpollo;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class SongHead extends Song {
  @Id
  private String id;

  SongHead() { }

  public SongHead(Song song) {
    super(song);
    // TODO: add hashcode instead of concatenation
    id = song.getArtist() + song.getName() + song.getAlbum();
  }
}