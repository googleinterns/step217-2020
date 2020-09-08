package com.google.alpollo;

import java.util.List;
import com.google.alpollo.model.AnalysisInfo;

/** Class that provides methods to work with the database. */
public class SongDataBase {
  /** Number of the songs that will be shown to the user. */
  private static final int TOP_SIZE = 10;

  /** Save request song to database. */
  public static void saveSongRequest(Song song) {
    OfyService.ofy().save().entity(song).now();
  }

  /** Returns the list of at most 10 songs. */
  public static List<Song> topSongs() {
    return OfyService.ofy().load().type(Song.class).limit(TOP_SIZE).list();
  }

  /** Save whole song info to database. */
  public static void saveSongInfo(AnalysisInfo info) {
    OfyService.ofy().save().entity(info).now();
  }

  /** Returns the song info by song id. */
  public static AnalysisInfo getSongInfo(String id) {
    return OfyService.ofy().load().type(AnalysisInfo.class).id(id).now();
  }
}