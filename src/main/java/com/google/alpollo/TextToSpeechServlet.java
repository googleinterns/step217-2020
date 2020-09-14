package com.google.alpollo;

import com.google.alpollo.model.Lyrics;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/text-to-speech")
public class TextToSpeechServlet extends HttpServlet {
  private final Gson gson = new Gson();

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String lyrics = gson.fromJson(request.getReader(), Lyrics.class).getLyrics();
    try {
      ByteString audioContents = TextToSpeechService.convertTextToSpeech(lyrics);

      if (audioContents == null) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            "Couldn't generate audio from the text.");
        return;
      }

      byte[] audio = audioContents.toByteArray();
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(audio));
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
