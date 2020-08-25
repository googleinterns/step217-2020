package com.google.alpollo;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Id;

@Entity
public class SongHead {
  @Id
  private String id;
  @Index
  private String artist;
  @Index
  private String name;
  @Index
  private String album;

  public SongHead(String artist, String name, String album) {
    this.artist = artist;
    this.name = name;
    this.album = album;
    id = artist + name + album;
  }

  public String getArtist() {
    return artist;
  }

  public String getName() {
    return name;
  }

  public String getAlbum() {
    return album;
  }
}