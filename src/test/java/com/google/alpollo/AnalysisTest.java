package com.google.alpollo;

import com.google.alpollo.model.AutocompleteSearchRequest;
import com.google.alpollo.helpers.AnalysisHelper;
import com.google.alpollo.model.Lyrics;
import com.google.alpollo.model.SongSentiment;
import com.google.alpollo.servlets.AutocompleteServlet;
import com.google.alpollo.servlets.EntityServlet;
import com.google.alpollo.servlets.SentimentServlet;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import com.google.alpollo.model.SongEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.common.collect.Sets;
import org.mockito.stubbing.Answer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public final class AnalysisTest {
  private static final String TEST_RESOURCE_PATH = System.getProperty("user.dir") + "/src/main/webapp";
  private final Gson gson = new Gson();
  private static final float TOLERANCE = 0.05f;
  private static final Lyrics LYRICS_LONG = new Lyrics("Days go by, but I don't seem to notice them\n" + "Just a roundabout of turns\n"
      + "All these nights I lie awake and on my own\n" + "My pale fire hardly burns\n"
      + "Never fell in love with the one who loves me\n" + "But with the ones who love me not\n"
      + "Are we doomed to live in grief and misery?\n" + "Is it everything we got?\n" + "I'm the mountain\n"
      + "Rising high\n" + "It's the way that I survived\n" + "I'm the mountain\n" + "Tell my tale\n"
      + "The greatest story's now for sale\n" + "I'm the seaside\n" + "I'm the waves\n"
      + "I'm the one that makes you crave\n" + "I'm the valley\n" + "I'm the hills\n"
      + "Look at me I'm standing still\n" + "I'm the mountain\n" + "I'm the plain\n" + "Tell me now am I insane\n"
      + "I'm the spirit\n" + "I'm the source\n" + "I'm the root I am the doors\n" + "I'm the road\n" + "Long and hard\n"
      + "Running out of my heart\n" + "I'm the mountain\n" + "Climb me up\n" + "And we're never gonna stop\n"
      + "I'm the locker\n" + "I'm the key\n" + "I am who you want to be\n" + "I'm the reason\n" + "I'm the blame\n"
      + "I will never be the same\n" + "Mirror-mirror tell the truth of the old ones and the youth\n"
      + "Of the things we hide deep from ourselves\n" + "Mirror-mirror show me now what will I become and how\n"
      + "For now I'm just a mountain\n" + "I'm the mountain\n" + "Down the road that takes me to the Headley Grange\n"
      + "I see a figure of a young man\n" + "He's torn with doubts, mistakes, his selfishness and rage\n"
      + "But doing all the best he can\n" + "I'm not so blind to see\n" + "That this young man is me");
  private static final Lyrics LYRICS_SHORT = new Lyrics("I'm the mountain\n"
      + "Rising high\n" + "It's the way that I survived\n" + "I'm the mountain\n" + "Tell my tale\n"
      + "The greatest story's now for sale\n");
  
  private ServletContext mockServletContext = mock(ServletContext.class);
  private HttpServletRequest request = mock(HttpServletRequest.class);
  private HttpServletResponse response = mock(HttpServletResponse.class);
  private SentimentServlet sentimentServletUnderTest;
  private EntityServlet entityServletUnderTest;
  private AutocompleteServlet autocompleteServletUnderTest;
  private StringWriter responseWriter;

  private static final Lyrics LYRICS_WITH_METADATA = new Lyrics("Google has found me some nice results!");
  private static final String EMPTY_STRING = "";
  private static final String WIKI_LINK_GOOGLE = "https://en.wikipedia.org/wiki/Google";

  private static final double NEUTRAL_MAGNITUDE = 0;
  private static final double NON_NEUTRAL_MAGNITUDE = 10;
  private static final double NEUTRAL_SCORE = 0.1;
  private static final double NEGATIVE_SCORE = -2;
  private static final double POSITIVE_SCORE = 2;

  private static final String FULL_ARTIST_NAME = "Eminem";
  private static final String INCOMPLETE_ARTIST_NAME = "Emine";
  private static final String WRONG_ARTIST_NAME = "Emnem";
  private static final String INCOMPLETE_BAND_NAME = "Rammste";
  private static final String INCOMPLETE_SONG_NAME = "master o";
  private static final String SEARCH_STRING = "searchString";
  private static final String TYPE = "type";
  private static final String ARTIST = "ARTIST";
  private static final String SONG = "SONG";

  @Before
  public void setUp() throws Exception {
    sentimentServletUnderTest = new SentimentServlet() {
      @Override
      public ServletContext getServletContext() {
        return mockServletContext;
      }
    };

    entityServletUnderTest = new EntityServlet() {
      @Override
      public ServletContext getServletContext() {
        return mockServletContext;
      }
    };

    autocompleteServletUnderTest = new AutocompleteServlet() {
      @Override
      public ServletContext getServletContext() {
        return mockServletContext;
      }
    };

    when(mockServletContext.getResourceAsStream(anyString())).thenAnswer(
        (Answer<InputStream>) invocation -> new FileInputStream(TEST_RESOURCE_PATH + invocation.getArgument(0)));

    responseWriter = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
  }

  @Test
  public void checkSentimentScore() throws Exception {
    when(request.getReader()).thenReturn(
        new BufferedReader(new StringReader(gson.toJson(LYRICS_LONG))));
    sentimentServletUnderTest.doPost(request, response);

    String responseString = responseWriter.toString();
    SongSentiment sentiment = gson.fromJson(responseString, SongSentiment.class);
    float actual = sentiment.getScore();
    float expected = -0.1f;
    
    Assert.assertEquals(expected, actual, TOLERANCE);
  }

  @Test
  public void checkSentimentMagnitude() throws IOException {
    when(request.getReader()).thenReturn(
        new BufferedReader(new StringReader(gson.toJson(LYRICS_LONG))));
    sentimentServletUnderTest.doPost(request, response);

    String responseString = responseWriter.toString();
    SongSentiment sentiment = gson.fromJson(responseString, SongSentiment.class);
    float actual = sentiment.getMagnitude();
    float expected = 0.7f;
    
    Assert.assertEquals(expected, actual, TOLERANCE);
  }

  /**
   * If the lyrics are too short, the API might find less than 10 entities.
   */
  @Test
  public void top10SalientEntitiesWithLessThan10Entities() throws IOException {
    when(request.getReader()).thenReturn(
        new BufferedReader(new StringReader(gson.toJson(LYRICS_SHORT))));
    entityServletUnderTest.doPost(request, response);
    String responseString = responseWriter.toString();

    List<SongEntity> actual = gson.fromJson(responseString, new TypeToken<List<SongEntity>>(){}.getType());
    List<SongEntity> expected = Arrays.asList(
        new SongEntity("mountain", 0.8993416801095009, Sets.newHashSet("OTHER", "LOCATION"), EMPTY_STRING),
        new SongEntity("story", 0.05391406640410423, Sets.newHashSet("WORK_OF_ART"), EMPTY_STRING),
        new SongEntity("sale", 0.031783707439899445, Sets.newHashSet("OTHER"), EMPTY_STRING),
        new SongEntity("tale", 0.014960560016334057, Sets.newHashSet("WORK_OF_ART"), EMPTY_STRING));
    Assert.assertThat(actual, CoreMatchers.is(expected));
  }

  /**
   * If the lyrics are very long, the API might find more than 10 entities. We only need the top 10.
   * TODO: Test fails because one entity doesn't match.
   */
  @Ignore@Test
  public void top10SalientEntitiesWithMoreThan10Entities() throws IOException {
    when(request.getReader()).thenReturn(
        new BufferedReader(new StringReader(gson.toJson(LYRICS_LONG))));
    entityServletUnderTest.doPost(request, response);
    String responseString = responseWriter.toString();

    List<SongEntity> actual = gson.fromJson(responseString, new TypeToken<List<SongEntity>>(){}.getType());
    List<SongEntity> expected = Arrays.asList(
        new SongEntity("seaside", 0.3589962422847748, Sets.newHashSet("LOCATION"), EMPTY_STRING),
        new SongEntity("source", 0.3411712050437927, Sets.newHashSet("PERSON"), EMPTY_STRING),
        new SongEntity("mountain", 0.0662671010941267, Sets.newHashSet("OTHER", "LOCATION"), EMPTY_STRING),
        new SongEntity("ones", 0.03210622997721657, Sets.newHashSet("PERSON"), EMPTY_STRING),
        new SongEntity("one", 0.027577804401516914, Sets.newHashSet("PERSON", "UNRECOGNIZED"), EMPTY_STRING),
        new SongEntity("love", 0.01844923384487629, Sets.newHashSet("OTHER"), EMPTY_STRING),
        new SongEntity("fire", 0.01844923384487629, Sets.newHashSet("OTHER"), EMPTY_STRING),
        new SongEntity("roundabout", 0.01844923384487629, Sets.newHashSet("OTHER"), EMPTY_STRING),
        new SongEntity("nights", 0.01844923384487629, Sets.newHashSet("EVENT"), EMPTY_STRING),
        new SongEntity("root", 0.014146503061056137, Sets.newHashSet("OTHER"), EMPTY_STRING));
        
      Assert.assertThat(actual, CoreMatchers.is(expected));
  }

  /** Some entities might have metadata attached to them. Let's see if they're shown correctly. */
  @Test
  public void checkEntitiesWithMetadata() throws IOException {
    when(request.getReader()).thenReturn(
        new BufferedReader(new StringReader(gson.toJson(LYRICS_WITH_METADATA))));
    entityServletUnderTest.doPost(request, response);
    String responseString = responseWriter.toString();

    List<SongEntity> actual = gson.fromJson(responseString, new TypeToken<List<SongEntity>>(){}.getType());
    List<SongEntity> expected = Arrays.asList(
        new SongEntity("Google", 0.8838967680931091, Sets.newHashSet("ORGANIZATION"), WIKI_LINK_GOOGLE),
        new SongEntity("results", 0.11610323935747147, Sets.newHashSet("OTHER"), EMPTY_STRING));

      Assert.assertThat(actual, CoreMatchers.is(expected));
  }

  @Test
  public void sentimentIsNeutral() {
    String actual = AnalysisHelper.getInterpretation(NEUTRAL_SCORE, NEUTRAL_MAGNITUDE);
    String expected = AnalysisHelper.NEUTRAL;

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sentimentIsNegative() {
    String actual = AnalysisHelper.getInterpretation(NEGATIVE_SCORE, NON_NEUTRAL_MAGNITUDE);
    String expected = AnalysisHelper.NEGATIVE;

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sentimentIsPositive() {
    String actual = AnalysisHelper.getInterpretation(POSITIVE_SCORE, NON_NEUTRAL_MAGNITUDE);
    String expected = AnalysisHelper.POSITIVE;

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void sentimentIsMixed() {
    String actual = AnalysisHelper.getInterpretation(NEUTRAL_SCORE, NON_NEUTRAL_MAGNITUDE);
    String expected = AnalysisHelper.MIXED;

    Assert.assertEquals(expected, actual);
  }

  @Ignore@Test
  public void foundArtistWithFullName() throws IOException {
    final AutocompleteSearchRequest searchRequest = new AutocompleteSearchRequest(FULL_ARTIST_NAME, ARTIST);
    when(request.getReader()).thenReturn(
      new BufferedReader(new StringReader(gson.toJson(searchRequest))));
    autocompleteServletUnderTest.doPost(request, response);
    String responseString = responseWriter.toString();

    List<String> actual = gson.fromJson(responseString, new TypeToken<List<String>>(){}.getType());
    List<String> expected = Arrays.asList("Dr. Dre", "Michael Clarke", "Eminem", "Bad Meets Evil", "Vincent Vinel", "Figgkidd", "Peter Litvin");

    Assert.assertEquals(expected, actual);
  }

  @Ignore@Test
  public void foundArtistWithIncompleteName() throws IOException {
    final AutocompleteSearchRequest searchRequest = new AutocompleteSearchRequest(INCOMPLETE_ARTIST_NAME, ARTIST);
    when(request.getReader()).thenReturn(
      new BufferedReader(new StringReader(gson.toJson(searchRequest))));
    autocompleteServletUnderTest.doPost(request, response);
    String responseString = responseWriter.toString();

    List<String> actual = gson.fromJson(responseString, new TypeToken<List<String>>(){}.getType());
    List<String> expected = Arrays.asList("Dr. Dre", "Michael Clarke", "Eminem", "Emine Erdoğan",
        "Emine Gülbahar Hatun", "Emine Ülker Tarhan", "Mihai Eminescu", "Emine Şenlikoğlu", 
        "Cardinal Richelieu", "Emine Dzhaparova");

    Assert.assertEquals(expected, actual);
  }

  @Ignore@Test
  public void foundArtistWithWrongName() throws IOException {
    final AutocompleteSearchRequest searchRequest = new AutocompleteSearchRequest(WRONG_ARTIST_NAME, ARTIST);
    when(request.getReader()).thenReturn(
      new BufferedReader(new StringReader(gson.toJson(searchRequest))));
    autocompleteServletUnderTest.doPost(request, response);
    String responseString = responseWriter.toString();

    List<String> actual = gson.fromJson(responseString, new TypeToken<List<String>>(){}.getType());
    List<String> expected = Arrays.asList();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void artistIsABand() throws IOException {
    final AutocompleteSearchRequest searchRequest = new AutocompleteSearchRequest(INCOMPLETE_BAND_NAME, ARTIST);
    when(request.getReader()).thenReturn(
      new BufferedReader(new StringReader(gson.toJson(searchRequest))));
    autocompleteServletUnderTest.doPost(request, response);
    String responseString = responseWriter.toString();

    List<String> actual = gson.fromJson(responseString, new TypeToken<List<String>>(){}.getType());
    List<String> expected = Arrays.asList("Rammstein", "Otthein Rammstedt", "Rammstein", "Rammstedt");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void searchSongWithIncompleteName() throws IOException {
    final AutocompleteSearchRequest searchRequest = new AutocompleteSearchRequest(INCOMPLETE_SONG_NAME, SONG);
    when(request.getReader()).thenReturn(
      new BufferedReader(new StringReader(gson.toJson(searchRequest))));
    autocompleteServletUnderTest.doPost(request, response);
    String responseString = responseWriter.toString();

    List<String> actual = gson.fromJson(responseString, new TypeToken<List<String>>(){}.getType());
    List<String> expected = Arrays.asList("Master of the House (Kono Uchi no Shu)", "Master Of Tiramisu",
        "Master of Xone", "Master of No Mercy", "Master of the Dead", "Master of Time", "Master of Destiny",
        "Master of Sleep", "Master of Art", "Master of the Universe");

    Assert.assertEquals(expected, actual);
  }  

  public void duplicateEntitiesWithSameType() {
    List<SongEntity> duplicateList = Arrays.asList(
        new SongEntity("mountain", 0.03, Sets.newHashSet("OTHER"), EMPTY_STRING),
        new SongEntity("mountain", 0.04, Sets.newHashSet("OTHER"), EMPTY_STRING));
    List<SongEntity> actual = AnalysisHelper.getFilteredTopEntities(duplicateList);    
    List<SongEntity> expected = Arrays.asList(
      new SongEntity("mountain", 0.07, Sets.newHashSet("OTHER"), EMPTY_STRING));

    Assert.assertThat(actual, CoreMatchers.is(expected));
  }
}