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
  private static final int TEN = 10;
  private static final int MORE_THAN_TEN = 15;

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

  /** Check that at the begining database is empty. */
  @Test
  public void noRequests() {
    Assert.assertEquals(new ArrayList<SongCounter>(), SongDataBase.topSongs());
  }

  /** Compare songs from database with correct list. */
  private void checkList(List<Song> exceptedSongs) {
    ArrayList<Song> actualSongs = new ArrayList<>();
    SongDataBase.topSongs().forEach(songCounter -> actualSongs.add(songCounter.getSong()));
    Assert.assertEquals(exceptedSongs.retainAll(actualSongs), actualSongs.retainAll(exceptedSongs));
    Assert.assertTrue(exceptedSongs.size() <= TEN);
  }

  /** Add one song request and check that database returns list with this request. */
  @Test
  public void addOneRequest() {
    SongDataBase.saveSongRequest(SONG);
    ArrayList<Song> exceptedSongs = new ArrayList<>();
    exceptedSongs.add(SONG);
    checkList(exceptedSongs);
  }

  /** Add 10 requests and check that database returns list with them. */
  @Test
  public void addTenRequests() {
    ArrayList<Song> exceptedSongs = new ArrayList<>(); 
    for (int i = 0; i < TEN; i++) {
      Song song = new Song(Integer.toString(i), Integer.toString(i + TEN));
      exceptedSongs.add(song);
      SongDataBase.saveSongRequest(song);
    }
    checkList(exceptedSongs);
  }

  /** Add more than 10 requests and check that database returns only 10 most searched. */
  @Test
  public void addMoreThanTenRequests() {
    ArrayList<Song> exceptedSongs = new ArrayList<>(); 
    for (int i = 0; i < MORE_THAN_TEN; i++) {
      Song song = new Song(Integer.toString(i), Integer.toString(i + MORE_THAN_TEN));
      for (int j = 0; j <= i; j++) {
        SongDataBase.saveSongRequest(song);
      }
      // because last 10 songs will be serched more
      if (MORE_THAN_TEN - TEN <= i) {
        exceptedSongs.add(song);
      }
    }
    checkList(exceptedSongs);
  }

  /** Serach for one  song twice, for other once and check that fisrts song is in top10. */
  @Test
  public void changeOneCounter() {
    SongDataBase.saveSongRequest(SONG);
    for (int i = 0; i < TEN; i++) {
      Song song = new Song(Integer.toString(i), Integer.toString(i + TEN));
      SongDataBase.saveSongRequest(song);
    }
    SongDataBase.saveSongRequest(SONG);
    SongDataBase.topSongs().contains(SONG);
  }
}