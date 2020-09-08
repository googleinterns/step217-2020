package com.google.alpollo;

import com.googlecode.objectify.annotation.Index;

/** 
 * Represents the structure of the song. 
 */
public class Song {
  @Index private String artist;
  @Index private String name;
  private final long BIG_PRIME_NUMBER = 2_147_483_647;

  /** Objectify requires no argument constructor. Do not use it. */
  private Song() { }

  public Song(String artist, String name) {
    this.artist = artist;
    this.name = name;
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
  protected Long id() {
    return BIG_PRIME_NUMBER * artist.hashCode() + name.hashCode();
  }
}