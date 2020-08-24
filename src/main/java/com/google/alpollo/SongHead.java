package com.google.alpollo;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class SongHead extends Song {
  @Id
  private final String id;

  public SongHead(Song song) {
    super(song);
    // Id is a long string. TODO: add hash code to save id.
    id = song.getArtist() + song.getName() + song.getAlbum();
  }

  public SongHead(String artist, String name, String album) {
    this(new Song(artist, name, album));
  }
}