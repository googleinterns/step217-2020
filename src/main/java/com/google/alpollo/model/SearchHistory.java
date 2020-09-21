package com.google.alpollo.model;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
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
  @Index private HashSet<SongCounter> history = new HashSet<>();

  /** Objectify requires no argument constructor. Do not use it. */
  private SearchHistory() { }

  public SearchHistory(String userId) {
    id = userId;
  }

  public void addSearchRequest(SongCounter song) {
    history.add(song);
  }

  public List<SongCounter> getHistory() {
    return new ArrayList<>(history);
  }
}