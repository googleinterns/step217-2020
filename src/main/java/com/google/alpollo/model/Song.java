package com.google.alpollo.model;

import com.googlecode.objectify.annotation.Index;

/** 
 * Represents the structure of the song. 
 */
public class Song {
  @Index private String artist;
  @Index private String name;

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
  public static String id(Song song) {
    // TODO: add hashcode instead of concatenation
    return song.artist + song.name;
  }
}