package com.google.alpollo;

import java.util.ArrayList;
import java.util.List;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Class that provides methods to work with the database. */
public class SongDataBase {
  private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private static final String ARTIST_PROPERTY = "artist";
  private static final String NAME_PROPERTY = "name";
  private static final String ALBUM_PROPERTY = "album";
  private static final String COUNTER_PROPERTY = "counter";
  private static final String SONG_PROPERTY = "Song";

  /** Increase the counter of song's requests if it in the database, otherwise store a new song. */
  public static void saveSongRequest(Song song) {
    Entity songEntity = new Entity(SONG_PROPERTY);
    songEntity.setProperty(ARTIST_PROPERTY, song.getArtist());
    songEntity.setProperty(NAME_PROPERTY, song.getName());
    songEntity.setProperty(ALBUM_PROPERTY, song.getAlbum());
    // Always put 1 to counter. TODO: increase.
    songEntity.setProperty(COUNTER_PROPERTY, 1);

    datastore.put(songEntity);
  }

  /** Returns the list of the most requested songs. */
  public static List<Song> topSongs() {
    // All songs. TODO: take only 10 the most requested.
    final Query query = new Query(SONG_PROPERTY).addSort(COUNTER_PROPERTY, SortDirection.DESCENDING);
    final PreparedQuery results = datastore.prepare(query);

    List<Song> songs = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String artist = (String) entity.getProperty(ARTIST_PROPERTY);
      String name = (String) entity.getProperty(NAME_PROPERTY);
      String album = (String) entity.getProperty(ALBUM_PROPERTY);
      songs.add(new Song(artist, name, album));
    }
    return songs;
  }
}