package com.google.alpollo;

import com.google.alpollo.helpers.YouTubeService;
import com.google.alpollo.servlets.YouTubeServlet;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.mockito.stubbing.Answer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public final class YouTubeTest {
  private static final String TEST_RESOURCE_PATH = System.getProperty("user.dir") + "/src/main/webapp";
  private final Gson gson = new Gson();
  private static final String SEARCH_STRING = "dark";
  
  private ServletContext mockServletContext = mock(ServletContext.class);
  private HttpServletRequest request = mock(HttpServletRequest.class);
  private HttpServletResponse response = mock(HttpServletResponse.class);
  private YouTubeServlet youTubeServletUnderTest;
  private StringWriter responseWriter;

  @Before
  public void setUp() throws Exception {
    youTubeServletUnderTest = new YouTubeServlet() {
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
  public void checkSearchResult() throws Exception {
    when(request.getParameter("query")).thenReturn(SEARCH_STRING);
    youTubeServletUnderTest.doGet(request, response);

    String responseString = responseWriter.toString();
    List<String> actual = gson.fromJson(responseString, new TypeToken<List<String>>() {}.getType());
    List<String> expected = Arrays.asList("0KSOMA3QBU0", "Q0oIoR9mLwc", "129kuDCQtHs", "K3Qzzggn--s", "U5gT8hf0Z_M");
    
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void checkErrorEmptyQuery() throws Exception {
    when(request.getParameter("query")).thenReturn("");
    youTubeServletUnderTest.doGet(request, response);

    verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty query.");
  }
}