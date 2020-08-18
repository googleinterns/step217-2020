package com.google.alpollo;

import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Class provides methods to work with the database. */
public class SongDataBase {
  private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  /** Increase the counter of song's requests if it in the database, otherwise store a new song. */
  public static void updateSongCounter(Song song) {
    Entity songEntity = new Entity("Song");
    songEntity.setProperty("band", song.getBand());
    songEntity.setProperty("name", song.getName());
    songEntity.setProperty("album", song.getAlbum());
    // Always put 1 to counter. TODO: increase.
    songEntity.setProperty("counter", 1);

    datastore.put(songEntity);
  }

  /** Returns the list of the most requested songs. */
  public static List<Song> topSongs() {
    // All songs. TODO: take only 10 the most requested.
    final Query query = new Query("Song").addSort("counter", SortDirection.DESCENDING);
    final PreparedQuery results = datastore.prepare(query);

    ArrayList<Song> songs = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String band = (String) entity.getProperty("band");
      String name = (String) entity.getProperty("name");
      String album = (String) entity.getProperty("album");
      songs.add(new Song(band, name, album));
    }
    return songs;
  }
}