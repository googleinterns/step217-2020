package com.google.alpollo.model;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Stringify;
import com.google.alpollo.database.SongStringifier;

/** 
 * Represents the structure of user's search history.
 * Contains the list with all songs.
 */
@Entity
public class SearchHistory {
  @Id private String id;
  @Stringify(SongStringifier.class)
  /** Saves search counter for each song that was searched by user. */
  private HashMap<Song, Integer> history = new HashMap<>();
  /** Number of the songs that will be shown to the user. */
  private static final int TOP_SIZE = 10;

  /** Objectify requires no argument constructor. Do not use it. */
  private SearchHistory() { }

  public SearchHistory(String userId) {
    id = userId;
  }

  /** Saves new search request from authorized user. */
  public void addSearchRequest(Song song) {
    if (history.containsKey(song)) {
      history.put(song, history.get(song) + 1);
    } else {
      history.put(song, 1);
    }
  }

  /** Returns list with top 10 searched songs from authorized user. */
  public List<Song> getHistory() {
    // Sort by values and then add only top 10 songs to result list 
    List<Song> sortedSongs = new ArrayList<>();
    history.entrySet()
           .stream()
           .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
           .limit(TOP_SIZE)
           .forEachOrdered(x -> sortedSongs.add(x.getKey()));
    return sortedSongs;
  }
}