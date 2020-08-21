package com.google.alpollo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.IOException;
import com.google.cloud.language.v1.Sentiment;

@RunWith(JUnit4.class)
public final class AnalysisTest {
  private static final float TOLERANCE = 0.05f;
  private static final String LYRICS = "Days go by, but I don't seem to notice them\n" + "Just a roundabout of turns\n"
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

  @Test
  public void checkSentimentScore() throws IOException {
    Sentiment sentiment = AnalysisHelper.getSentiment(LYRICS);
    float actual = sentiment.getScore();
    float expected = -0.1f;
    
    Assert.assertEquals(expected, actual, TOLERANCE);
  }

  @Test
  public void checkSentimentMagnitude() throws IOException {
    Sentiment sentiment = AnalysisHelper.getSentiment(LYRICS);
    float actual = sentiment.getMagnitude();
    float expected = 0.7f;
    
    Assert.assertEquals(expected, actual, TOLERANCE);
  }
}