package com.google.alpollo;

import com.google.alpollo.model.SongSentiment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import com.google.alpollo.model.SongEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.mockito.stubbing.Answer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public final class AnalysisTest {
  private static final String CONFIG_FILE_PATH = System.getProperty("user.dir") + "/src/main/webapp/WEB-INF/config.json";
  private final Gson gson = new Gson();
  private static final float TOLERANCE = 0.05f;
  private static final String LYRICS_LONG = "Days go by, but I don't seem to notice them\n" + "Just a roundabout of turns\n"
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
      + "But doing all the best he can\n" + "I'm not so blind to see\n" + "That this young man is me";
  private static final String LYRICS_SHORT = "I'm the mountain\n"
      + "Rising high\n" + "It's the way that I survived\n" + "I'm the mountain\n" + "Tell my tale\n"
      + "The greatest story's now for sale\n";
  
  private ServletContext mockServletContext = mock(ServletContext.class);
  private HttpServletRequest request = mock(HttpServletRequest.class);
  private HttpServletResponse response = mock(HttpServletResponse.class);
  private SentimentServlet sentimentServletUnderTest;
  private EntityServlet entityServletUnderTest;
  private StringWriter responseWriter;


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

    when(mockServletContext.getResourceAsStream(anyString())).thenAnswer(
        (Answer<InputStream>) invocation -> new FileInputStream(CONFIG_FILE_PATH));

    responseWriter = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
  }

  @Test
  public void checkSentimentScore() throws Exception {
    when(request.getParameter("lyrics")).thenReturn(LYRICS_LONG);
    sentimentServletUnderTest.doPost(request, response);

    String responseString = responseWriter.toString();
    SongSentiment sentiment = gson.fromJson(responseString, SongSentiment.class);
    float actual = sentiment.getScore();
    float expected = -0.1f;
    
    Assert.assertEquals(expected, actual, TOLERANCE);
  }

  @Test
  public void checkSentimentMagnitude() throws IOException {
    when(request.getParameter("lyrics")).thenReturn(LYRICS_LONG);
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
    when(request.getParameter("lyrics")).thenReturn(LYRICS_SHORT);
    entityServletUnderTest.doPost(request, response);

    String responseString = responseWriter.toString();
    List<SongEntity> actual = gson.fromJson(responseString, new TypeToken<List<SongEntity>>(){}.getType());

    List<SongEntity> expected = Arrays.asList(new SongEntity("mountain", 0.84), 
        new SongEntity("mountain", 0.06), new SongEntity("story", 0.05), new SongEntity("sale", 0.03), 
        new SongEntity("tale", 0.01));
    Assert.assertEquals(expected, actual);
  }

  /**
   * If the lyrics are very long, the API might find more than 10 entities. We only need the top 10.
   */
  @Test
  public void top10SalientEntitiesWithMoreThan10Entities() throws IOException {
      when(request.getParameter("lyrics")).thenReturn(LYRICS_LONG);
    entityServletUnderTest.doPost(request, response);

    String responseString = responseWriter.toString();
    List<SongEntity> actual = gson.fromJson(responseString, new TypeToken<List<SongEntity>>(){}.getType());
    List<SongEntity> expected = Arrays.asList(new SongEntity("seaside", 0.36), 
        new SongEntity("source", 0.34), new SongEntity("mountain", 0.05), new SongEntity("ones", 0.03), 
        new SongEntity("one", 0.03), new SongEntity("roundabout", 0.02), new SongEntity("love", 0.02),
        new SongEntity("nights", 0.02), new SongEntity("fire", 0.02), new SongEntity("root", 0.01));
        
    Assert.assertEquals(expected, actual);
  }
}