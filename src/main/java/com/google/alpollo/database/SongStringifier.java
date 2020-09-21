package com.google.alpollo.database;

import com.google.alpollo.model.Song;
import com.googlecode.objectify.stringifier.Stringifier;

/** Helps save map to database using objectify. */
public class SongStringifier implements Stringifier<Song> {
  @Override
  public String toString(Song song) {
    return song.getString();
  }

  @Override
  public Song fromString(String str) {
    return new Song(str);
  }
}