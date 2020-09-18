package com.google.alpollo.database;

import java.util.List;

import com.google.alpollo.database.OfyService;
import com.google.alpollo.model.AnalysisInfo;
import com.google.alpollo.model.Song;
import com.google.alpollo.model.SongCounter;
import com.google.alpollo.model.SearchHistory;

/** Class that provides methods to work with the database. */
public class SongDataBase {
  /** Number of the songs that will be shown to the user. */
  private static final int TOP_SIZE = 10;

  /** Save request song to database, increment the search counter and returns SongCounter with correct searchCounter. */
  public static SongCounter saveSongRequest(Song song) {
    SongCounter songCounter = OfyService.ofy().load().type(SongCounter.class).id(song.id()).now();
    if (songCounter == null) {
      songCounter = new SongCounter(song);
    }
    songCounter.incrementSearchCounter();
    saveInUserHistory(song);
    OfyService.ofy().save().entity(songCounter).now();
    return songCounter;
  }

  /** Returns the list of the most requested songs. */
  public static List<SongCounter> topSongs() {
    return OfyService.ofy().load().type(SongCounter.class).order("-searchCounter").limit(TOP_SIZE).list();
  }

  /** Save analysis info to database. */
  public static void saveAnalysisInfo(AnalysisInfo info) {
    // Song that we want to save with analysis
    SongCounter newSong = saveSongRequest(info.getSong());

    List<SongCounter> songCounters = topSongs();
    // Song that we already have in database and it is on the last place in the top of songs
    SongCounter lessSearchedSongFromTop = songCounters.get(songCounters.size() - 1);

    if (lessSearchedSongFromTop.getSearchCounter() < newSong.getSearchCounter() || songCounters.size() <= TOP_SIZE) {
      OfyService.ofy().delete().type(AnalysisInfo.class).id(lessSearchedSongFromTop.getSong().id());
      // Initialize id, because frontend will send SongInfo object without it.
      info.initializeId();
      OfyService.ofy().save().entity(info).now();
    }
  }

  /** Returns the analysis info by song id. */
  public static AnalysisInfo getAnanlysisInfo(Long id) {
    return OfyService.ofy().load().type(AnalysisInfo.class).id(id).now();
  }

  /** Save search request to user history. */
  public static void saveInUserHistory(Song song) {
  //   SongCounter songCounter = OfyService.ofy().load().type(SearchHistory.class).id(song.id()).now();
  //   if (songCounter == null) {
  //     songCounter = new SongCounter(song);
  //   }
  //   songCounter.incrementSearchCounter();
  //   saveInUserHistory(song);
  }

  /** Returns the list of the most requested songs from the user. */
  public static List<SongCounter> searchHistory(String userId) {
    return OfyService.ofy().load().type(SearchHistory.class).id(userId).now().getHistory();
  }
}
