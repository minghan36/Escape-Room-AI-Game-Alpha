package nz.ac.auckland.se206;

import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

/** Represents the state of the game. */
public class GameState {


  protected static ChatCompletionRequest chatCompletionRequestChat;
  protected static ChatCompletionRequest chatCompletionRequestWindow;
  protected static ChatCompletionRequest chatCompletionRequestEncourage;
  /** Indicates whether the riddle has been resolved. */
  protected static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  protected static boolean isNoteFound = false;


  protected static boolean isGameWon = false;

  protected static boolean isGameMasterLoaded = false;

  protected static int minutes = 2;

  protected static int seconds = 0;


  protected static String getTimeLeft(){
    if (seconds == 0){
      return String.valueOf(minutes) + ":00";
    } else if (seconds<10){
      return String.valueOf(minutes) + ":0" + String.valueOf(seconds);
    }else {
      return String.valueOf(minutes) + ":" + String.valueOf(seconds);
    }
  }

}
