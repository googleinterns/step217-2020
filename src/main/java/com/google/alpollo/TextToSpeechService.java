package com.google.alpollo;

import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import java.io.IOException;

public class TextToSpeechService {
  private static TextToSpeechSettings settings;
  /**
   * Language code of the lyrics
   * TODO: define language code from the text or receive it as an argument.
   */
  private static final String LANGUAGE_CODE = "en-US";

  private TextToSpeechService() {};

  /**
   * Creates setting for TextToSpeech service. Adds special header to make it
   * work on Cloud Shell.
   * @param projectID
   * @throws IOException
   */
  private static void createTextToSpeechSettings(String projectID) throws IOException {
    if (projectID == null) {
      throw new IllegalStateException("Project ID wasn't defined.");
    }

    if (settings == null) {
      settings = TextToSpeechSettings.newBuilder().setHeaderProvider(
          FixedHeaderProvider.create("X-Goog-User-Project", projectID)).build();
    }
  }

  /**
   * Generates speech audio file from the text.
   *
   * @param text the text for convertion
   * @return ByteString of audio; {@code null} if something went wrong
   */
  public static ByteString convertTextToSpeech(String text, String projectID) throws IOException {
    createTextToSpeechSettings(projectID);

    try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings)) {
      SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

      VoiceSelectionParams voice =
          VoiceSelectionParams.newBuilder()
              .setLanguageCode(LANGUAGE_CODE)
              .setSsmlGender(SsmlVoiceGender.NEUTRAL)
              .build();

      AudioConfig audioConfig =
          AudioConfig.newBuilder()
              .setAudioEncoding(AudioEncoding.MP3)
              .build();

      SynthesizeSpeechResponse response =
          textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
      ByteString audioContents = response.getAudioContent();

      return audioContents;
    } catch (Exception e) {
      return null;
    }
  }
}
