package com.google.alpollo.model;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;

/** 
 * Represents the structure of user's searc history.
 * Contains the list with all songs.
 */
@Entity
public class SearchHistory {
  @Id private String id;
  @Index private HashMap<Song, Integer> history = new HashMap<>();
  /** Number of the songs that will be shown to the user. */
  private static final int TOP_SIZE = 10;

  /** Objectify requires no argument constructor. Do not use it. */
  private SearchHistory() { }

  public SearchHistory(String userId) {
    id = userId;
  }

  /** Saves new  search request from autorized user. */
  public void addSearchRequest(Song song) {
    if (history.containsKey(song)) {
      history.put(song, history.get(song) + 1);
    } else {
      history.put(song, 1);
    }
  }

  /** Returns list with top 10 searched songs from autorized user. */
  public List<Song> getHistory() {
    // sort by values and then add only top 10 songs to result list 
    List<Song> sortedSongs = new ArrayList<>();
    history.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .limit(TOP_SIZE)
        .forEachOrdered(x -> sortedSongs.add(x.getKey()));
    return sortedSongs;
  }
}