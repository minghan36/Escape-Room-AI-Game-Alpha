package nz.ac.auckland.se206;

import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.speech.TextToSpeech;

/** Represents the state of the game. */
public class GameState {

  /** The different AI interactions that the user can face. All have different settings. */
  protected static ChatCompletionRequest chatCompletionRequestChat;

  protected static ChatCompletionRequest chatCompletionRequestWindow;
  protected static ChatCompletionRequest chatCompletionRequestEncourage;

  /** The Text to speech instance */
  protected static TextToSpeech textToSpeech = new TextToSpeech();

  /** Indicates whether the riddle has been resolved. */
  protected static boolean isRiddleResolved = false;

  /** Indicates whether the note has been found. */
  protected static boolean isNoteFound = false;

  /** Indicates whether the game has been won. */
  protected static boolean isGameWon = false;

  /** Indicates whether the Game Master has been loaded before. */
  protected static boolean isGameMasterLoaded = false;

  /** Indicates the time left in the round. */
  protected static int minutes = 2;

  protected static int seconds = 0;

  /**
   * Returns time left in the round.
   *
   * @return String of the time left in minutes:seconds format.
   */
  protected static String getTimeLeft() {
    if (seconds == 0) {
      return String.valueOf(minutes) + ":00";
    } else if (seconds < 10) {
      return String.valueOf(minutes) + ":0" + String.valueOf(seconds);
    } else {
      return String.valueOf(minutes) + ":" + String.valueOf(seconds);
    }
  }
}
