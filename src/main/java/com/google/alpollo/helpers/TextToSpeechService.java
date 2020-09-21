package com.google.alpollo.helpers;

import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import java.io.IOException;

/**
 * Contains methods for converting text to speech.
 */
public class TextToSpeechService {
  private static TextToSpeechSettings settings;
  private static final String defaultLanguageCode = "en-US";

  private TextToSpeechService() {};

  /**
   * Creates setting for TextToSpeech service. Adds special header to make it
   * work on Cloud Shell.
   * @param projectID
   * @throws IOException
   */
  private static TextToSpeechClient createTextToSpeechClient(String projectID) throws IOException {
    if (projectID == null) {
      throw new IllegalStateException("Project ID wasn't defined.");
    }

    if (settings == null) {
      settings = TextToSpeechSettings.newBuilder().setHeaderProvider(
          FixedHeaderProvider.create("X-Goog-User-Project", projectID)).build();
    }

    return TextToSpeechClient.create(settings);
  }

  /**
   * Generates speech audio file from the text.
   *
   * @param text the text for convertion
   * @return ByteString of audio; {@code null} if we couldn't synthesize speech from the text.
   */
  public static ByteString convertTextToSpeech(String text, String projectID) throws IOException {
    try (TextToSpeechClient textToSpeechClient = createTextToSpeechClient(projectID)) {
      String languageCode = DetectLanguageService.detectLanguage(text, projectID);

      if (languageCode == null) {
        languageCode = defaultLanguageCode;
      }

      VoiceSelectionParams voice =
          VoiceSelectionParams.newBuilder()
              .setLanguageCode(languageCode)
              .setSsmlGender(SsmlVoiceGender.NEUTRAL)
              .build();

      AudioConfig audioConfig =
          AudioConfig.newBuilder()
              .setAudioEncoding(AudioEncoding.MP3)
              .build();

      SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
      SynthesizeSpeechResponse response =
          textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

      return response.getAudioContent();
    } catch (IOException e) {
      return null;
    }
  }
}
