package com.google.alpollo;

import java.util.List;
import java.util.ArrayList;

/** Class that provides methods to work with the database. */
public class SongDataBase {
  /** Number of the songs that will be shown to the user. */
  private static final int TOP_SIZE = 10;

  /** Save request song to database and increase the search counter. */
  public static void saveSongRequest(Song song) {
    SongCounter songCounter = OfyService.ofy().load().type(SongCounter.class).id(song.id()).now();
    if (songCounter == null) {
      songCounter = new SongCounter(song);
    }
    songCounter.incrementSearchCounter();
    OfyService.ofy().save().entity(songCounter).now();
  }

  /** Returns the list of the most requested songs. */
  public static List<SongCounter> topSongs() {
    return OfyService.ofy().load().type(SongCounter.class).order("-searchCounter").limit(TOP_SIZE).list();
  }
}