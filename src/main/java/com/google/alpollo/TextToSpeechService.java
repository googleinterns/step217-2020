package com.google.alpollo;

import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;

public class TextToSpeechService {
  /**
   * Language code of the lyrics
   * TODO: define language code from the text or receive it as an argument.
   */
  private static final String LANGUAGE_CODE = "en-US";

  /**
   * Generates speech audio file from the text.
   *
   * @param text the text for convertion
   * @return ByteString of audio; {@code null} if something went wrong
   */
  public static ByteString convertTextToSpeech(String text) {
    try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
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
