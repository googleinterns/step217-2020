package com.google.alpollo;

import com.googlecode.objectify.annotation.Index;

/** 
 * Represents the structure of the song. 
 */
public class Song {
  @Index private String artist;
  @Index private String name;
  @Index private String album;

  /** Objectify requires no argument constructor. Do not use it. */
  private Song() { }

  public Song(String artist, String name, String album) {
    this.artist = artist;
    this.name = name;
    this.album = album;
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

  /** Calculates and returns song id by each song. */
  static String id(Song song) {
    // TODO: add hashcode instead of concatenation
    return song.artist + song.name + song.album;
  }
}