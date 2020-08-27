package com.google.alpollo;

import java.util.ArrayList;
import java.util.List;

/** Class that provides methods to work with the database. */
public class SongDataBase {
  private static final int TOP_SIZE = 10;

  /** Save request song to database and increase the search counter. */
  public static void saveSongRequest(Song song) {
    String id = SongHead.getSongId(song);
    SongHead songHead = OfyService.ofy().load().type(SongHead.class).id(id).now();
    if (songHead == null) {
      songHead = new SongHead(song);
    }
    songHead.increaseSearchCounter();
    OfyService.ofy().save().entity(songHead).now();
  }

  /** Returns a list of up to 10 most searched songs. */
  public static List<Song> topSongs() {
    List<Song> songs = new ArrayList<>();
    OfyService.ofy().load().type(SongHead.class).order("-searchCounter").limit(TOP_SIZE).list()
        .forEach(song -> songs.add((Song) song));
    return songs;
  }
}