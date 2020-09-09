package com.google.alpollo;

import java.util.List;
import com.google.alpollo.model.SongInfo;
import com.google.alpollo.model.Song;
import com.google.alpollo.model.SongCounter;
import java.util.ArrayList;

/** Class that provides methods to work with the database. */
public class SongDataBase {
  /** Number of the songs that will be shown to the user. */
  private static final int TOP_SIZE = 10;

  /** Save request song to database and increase the search counter. */
  public static void saveSongRequest(Song song) {
    SongCounter songCounter = OfyService.ofy().load().type(SongCounter.class).id(Song.id(song)).now();
    if (songCounter == null) {
      songCounter = new SongCounter(song);
    }
    songCounter.increaseSearchCounter();
    OfyService.ofy().save().entity(songCounter).now();
  }

  /** Returns the list of the most requested songs. */
  public static List<SongCounter> topSongs() {
    return OfyService.ofy().load().type(SongCounter.class).order("-searchCounter").limit(TOP_SIZE).list();
  }

  /** Save whole song info to database. */
  public static void saveSongInfo(SongInfo songInfo) {
    songInfo.setId();
    OfyService.ofy().save().entity(songInfo).now();
  }

  /** Returns the song info by song id. */
  public static SongInfo getSongInfo(String id) {
    return OfyService.ofy().load().type(SongInfo.class).id(id).now();
  }
}