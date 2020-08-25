package com.google.alpollo;

import com.googlecode.objectify.annotation.Index;

/** Represents the structure of the song. */
public class Song {
  @Index
  private String artist;
  @Index
  private String name;
  @Index
  private String album;

  public Song() { }

  public Song(String artist, String name, String album) {
    this.artist = artist;
    this.name = name;
    this.album = album;
  }

  public Song(SongHead song) {
    this(song.getArtist(), song.getName(), song.getAlbum());
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