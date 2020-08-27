package com.google.alpollo;

import java.util.ArrayList;
import java.util.List;

/** Class that provides methods to work with the database. */
public class SongDataBase {
  /** Number of the songs that will be shown to the user. */
  private static final int TOP_SIZE = 10;

  /** Save request song to database. */
  public static void saveSongRequest(Song song) {
    OfyService.ofy().save().entity(new SongHead(song)).now();
  }

  /** Returns the list of at most 10 songs. */
  public static List<Song> topSongs() {
    List<Song> songs = new ArrayList<>();
    OfyService.ofy().load().type(SongHead.class).limit(TOP_SIZE).list()
        .forEach(song -> songs.add((Song) song));
    return songs;
  }
}