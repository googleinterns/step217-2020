package com.google.alpollo;

import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/** 
 * Represents the structure of the song. 
 * Please do not add fields because they will be saved in the search counter database. 
 */
@Entity
public class Song {
  @Id private String id;
  @Index private String artist;
  @Index private String name;
  @Index private String album;

  /** Objectify requires no arguments constructor. Do not use it. */
  private Song() { }

  public Song(String artist, String name, String album) {
    // TODO: add hashcode instead of concatenation
    this.id = artist + name + album;
    this.artist = artist;
    this.name = name;
    this.album = album;
  }

  /** Returns the song's id. */
  public String getId() {
    return id;
  }

  /** Returns the name of the song's artist. */
  public String getArtist() {
    return artist;
  }

  /** Returns the name of the song. */
  public String getName() {
    return name;
  }

  /** Returns the name of the song's album. */
  public String getAlbum() {
    return album;
  }
}