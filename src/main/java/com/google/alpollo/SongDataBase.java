package com.google.alpollo;

import java.util.List;
import com.google.alpollo.model.AnalysisInfo;
import com.google.alpollo.model.Song;
import com.google.alpollo.model.SongCounter;

/** Class that provides methods to work with the database. */
public class SongDataBase {
  /** Number of the songs that will be shown to the user. */
  private static final int TOP_SIZE = 10;

  /** Save request song to database and increment the search counter. */
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
  
  /** Save analysis info to database. */
  public static void saveAnalysisInfo(AnalysisInfo info) {
    info.setId();
    OfyService.ofy().save().entity(info).now();
  }

  /** Returns the analysis info by song id. */
  public static AnalysisInfo getAnanlysisInfo(Long id) {
    return OfyService.ofy().load().type(AnalysisInfo.class).id(id).now();
  }
}