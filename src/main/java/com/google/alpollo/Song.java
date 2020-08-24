package com.google.alpollo;

/** Represents the structure of the song. */
public class Song {
  private final String artist;
  private final String name;
  private final String album;

  public Song(String artist, String name, String album) {
    this.artist = artist;
    this.name = name;
    this.album = album;
  }

  public Song(Song song) {
    this(song.artist, song.name, song.album);
  }

  /** Returns artists's name of the song. */
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