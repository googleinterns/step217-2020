package com.google.alpollo;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Id;

@Entity
public class SongHead extends Song {
  @Id
  private String id;

  public SongHead() { }

  public SongHead(Song song) {
    super(song);
    id = song.getArtist() + song.getName() + song.getAlbum();
  }

  public SongHead(String artist, String name, String album) {
    this(new Song(artist, name, album));
  }

}