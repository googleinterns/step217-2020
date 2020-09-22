package com.google.alpollo.model;

import com.googlecode.objectify.annotation.Index;

/**
 * Represents the structure of the song.
 */
public class Song {
  @Index private String artist;
  @Index private String name;
  /** For counting hashcode. */
  private static final long BIG_PRIME_NUMBER = 2_147_483_647;
  /** To separate the artist name and the name of the song. */
  private static final String SEPARATOR = "±";

  /** Objectify requires no argument constructor. Do not use it. */
  private Song() { }

  public Song(String artist, String name) {
    this.artist = artist;
    this.name = name;
  }

  /** 
   * Objectify requires to write this constructor to save maps to database.
   * More detail we need to convert class Song to String and convert back because we use Song class as a keys.
   */
  public Song(String string) {
    String[] strings = string.split(SEPARATOR);
    if (strings.length == 2) {
      artist = strings[0];
      name = strings[1];
    }
  }

  /** Returns the name of the song's artist. */
  public String getArtist() {
    return artist;
  }

  /** Returns the name of the song. */
  public String getName() {
    return name;
  }

  /** Calculates and returns song id by each song. */
  public Long id() {
    return BIG_PRIME_NUMBER * artist.hashCode() + name.hashCode();
  }

  /** 
   * Objectify requires to write this constructor to save maps to database.
   * More detail we need to convert class Song to String and convert back because we use Song class as a keys.
   */
  public String getString() {
    return artist + SEPARATOR + name;
  }
}