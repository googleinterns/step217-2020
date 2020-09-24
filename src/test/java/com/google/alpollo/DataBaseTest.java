package com.google.alpollo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.alpollo.model.Song;
import com.google.alpollo.model.SongCounter;
import com.google.alpollo.model.AnalysisInfo;
import com.google.alpollo.SongDataBase;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cache.AsyncCacheFilter;
import com.googlecode.objectify.util.Closeable;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.After;

@RunWith(JUnit4.class)
public final class DataBaseTest {
  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private Closeable session;

  private static final Song SONG = new Song("Drake", "Toosie Slide");
  /** We save 10 songs to database and to test it we use this constant. This number is maximum. */
  private static final int NUM_SONGS_DATABASE = 10;
  /** 
   * We save 10 songs to database and this constant provides us to check adding more then 10 songs, 
   * in this case 15, but we can change. 
   */
  private static final int NUM_OF_ACCEPTED_SONGS = NUM_SONGS_DATABASE + 5;

  @BeforeClass
  public static void beforeClass() throws Exception {
    ObjectifyService.setFactory(new ObjectifyFactory());
    ObjectifyService.register(SongCounter.class);
    ObjectifyService.register(AnalysisInfo.class);
  }

  @Before
  public void before() throws Exception {
    this.session = ObjectifyService.begin();
    this.helper.setUp();
  }

  @After
  public void after() {
    AsyncCacheFilter.complete();
    this.session.close();
    this.helper.tearDown();
  }

  /** Check that at the beginning database is empty. */
  @Test
  public void noRequests() {
    Assert.assertEquals(new ArrayList<SongCounter>(), SongDataBase.topSongs());
  }

  /** Compare songs from database with correct list. */
  private void checkList(List<Song> expectedSongs) {
    ArrayList<Song> actualSongs = new ArrayList<>();
    SongDataBase.topSongs().forEach(songCounter -> actualSongs.add(songCounter.getSong()));
    Assert.assertEquals(expectedSongs.retainAll(actualSongs), actualSongs.retainAll(expectedSongs));
    Assert.assertTrue(expectedSongs.size() <= NUM_SONGS_DATABASE);
  }

  /** Add one song request and check that database returns list with this request. */
  @Test
  public void addOneRequest() {
    SongDataBase.saveSongRequest(SONG);
    ArrayList<Song> expectedSongs = new ArrayList<>();
    expectedSongs.add(SONG);
    checkList(expectedSongs);
  }

  /** Add maximum requests and check that database returns list with them. */
  @Test
  public void addMaximumOfRequests() {
    ArrayList<Song> expectedSongs = new ArrayList<>(); 
    for (int i = 0; i < NUM_SONGS_DATABASE; i++) {
      Song song = new Song(Integer.toString(i), Integer.toString(i));
      expectedSongs.add(song);
      SongDataBase.saveSongRequest(song);
    }
    checkList(expectedSongs);
  }

  /** Add more than maximum requests and check that database returns only maximum most searched. */
  @Test
  public void addMoreThanMaximumOfRequests() {
    ArrayList<Song> expectedSongs = new ArrayList<>(); 
    for (int i = 0; i < NUM_OF_ACCEPTED_SONGS; i++) {
      Song song = new Song(Integer.toString(i), Integer.toString(i));
      for (int j = 0; j <= i; j++) {
        SongDataBase.saveSongRequest(song);
      }
      // because last 10 songs will be searched more
      if (NUM_OF_ACCEPTED_SONGS - NUM_SONGS_DATABASE <= i) {
        expectedSongs.add(song);
      }
    }
    checkList(expectedSongs);
  }

  /** Search for one song twice, for other once and check that fisrt song is in top10. */
  @Test
  public void changeOneCounter() {
    SongDataBase.saveSongRequest(SONG);
    for (int i = 0; i < NUM_SONGS_DATABASE; i++) {
      Song song = new Song(Integer.toString(i), Integer.toString(i));
      SongDataBase.saveSongRequest(song);
    }
    SongDataBase.saveSongRequest(SONG);
    SongDataBase.topSongs().contains(SONG);
  }
}