package com.google.alpollo;

/** Represents the structure of the song. */
public class Song {
  private final String band;
  private final String name;
  private final String album;

  public Song(String band, String name, String album) {
    this.band = band;
    this.name = name;
    this.album = album;
  }

  /** Returns the band name of the song. */
  public String getBand() {
    return band;
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