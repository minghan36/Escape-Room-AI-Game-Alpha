package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Controller class for the room view. */
public class RoomController extends GameState {

  @FXML private Rectangle rectangleDoor;
  @FXML private Rectangle rectanglePillow;
  @FXML private Rectangle rectangleWindow;
  @FXML private Rectangle rectangleRed;
  @FXML private Rectangle rectangleGreen;
  @FXML private Rectangle rectangleBlue;
  @FXML private Circle circle;
  @FXML private Label labelPasscode;
  @FXML private Label labelTimer;
  @FXML private Label labelNoteContent;
  @FXML private Label labelChat;
  @FXML private ImageView speechBubble;
  private static Timeline timelineTime;
  private static Timeline timelineEncourage;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // Thread for the timer displayed to the player.
    Thread timeThread =
        new Thread(
            () -> {
              startTimer();
            });
    timeThread.start();
  }

   /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickDoor(MouseEvent event) throws IOException {
    System.out.println("door clicked");
    // Loads the chat view while displaying message to the player if the riddle has not been solved.
    if (!isRiddleResolved) {
      isGameMasterLoaded = true;
      if (!isGameMasterLoaded){
      displayMessage("SHIP AI LOADING...");
      }
      App.setRoot("chat");
      return;
    }
    if (!GameState.isNoteFound) {
      displayMessage("You have solved the Riddle. Now find the item! (Pillow)");
    }
  }

  /**
   * Handles the click event on the pillow.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickPillow(MouseEvent event) {
    System.out.println("pillow clicked");
    rectanglePillow.setDisable(true);
    // Displays the note containing hints on how to exit the room, once the riddle has been solved.
    if (GameState.isRiddleResolved && !GameState.isNoteFound) {
      showDialog("Info", "Note Found", "RGBG");
      GameState.isNoteFound = true;
      labelNoteContent.setText("RGBG");
      // Thread that encourages the player every 20 seconds begins.
      Thread encourageThread =
          new Thread(
              () -> {
                startEncouraging();
              });
      encourageThread.start();
    }
    rectanglePillow.setDisable(false);
  }

  /**
   * Handles the click event on the Window.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickWindow(MouseEvent event) {
    System.out.println("Window Clicked");
    rectangleWindow.setDisable(true);
    // Game Master comments on the view outside the window if loaded.
    if (isGameMasterLoaded) {
      chatCompletionRequestWindow.addMessage(
          new ChatMessage(
              "user",
              "With the following sentence as a guide. Aren't the stars beautiful. Acknowledge the"
                  + " beauty of space in at most four words."));
      ChatCompletionResult chatCompletionResult;
      try {
        chatCompletionResult = chatCompletionRequestWindow.execute();
        Choice result = chatCompletionResult.getChoices().iterator().next();
        displayMessage(result.getChatMessage().getContent());
      } catch (ApiProxyException e) {
        e.printStackTrace();
      }
    }
    rectangleWindow.setDisable(false);
  }

  /**
   * Handles the click event on the circle.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickCircle() {
    // removes the pop up message displayed. Speech Bubble and text disappear.
    removeMessage();
  }

  /**
   * Handles the click event on the red light.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickRed(MouseEvent event) {
    System.out.println("Red");
    // Adds the letter "R" to the passcode and checks if it is correct if the length of the passcode
    // is now equal to four.
    if (GameState.isRiddleResolved && GameState.isNoteFound) {
      labelPasscode.setText(labelPasscode.getText() + "R");
      if (labelPasscode.getText().length() == 4) {
        checkPasscode();
      }
    }
  }

  /**
   * Handles the click event on the green light.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickGreen(MouseEvent event) {
    System.out.println("Green");
    // Adds the letter "G" to the passcode and checks if it is correct if the length of the passcode
    // is now equal to four.
    if (GameState.isRiddleResolved && GameState.isNoteFound) {
      labelPasscode.setText(labelPasscode.getText() + "G");
      if (labelPasscode.getText().length() == 4) {
        checkPasscode();
      }
    }
  }

  /**
   * Handles the click event on the blue light.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickBlue(MouseEvent event) {
    System.out.println("Blue");
    // Adds the letter "B" to the passcode and checks if it is correct if the length of the passcode
    // is now equal to four.
    if (GameState.isRiddleResolved && GameState.isNoteFound) {
      labelPasscode.setText(labelPasscode.getText() + "B");
      if (labelPasscode.getText().length() == 4) {
        checkPasscode();
      }
    }
  }

  /**
   * Handles the click event on the bottom left label containing the contents of the note.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickContent(MouseEvent event) {}

  /** Creates a TimeLine that counts down from 120 every second. */
  public void startTimer() {
    timelineTime =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
                    // Counts down the timer.
                    if (seconds == 0) {
                      minutes--;
                      seconds = 59;
                    } else if (seconds > 0) {
                      seconds--;
                    }
                    // Displays the current time to the player.
                    Platform.runLater(
                        new Runnable() {
                          @Override
                          public void run() {
                            labelTimer.setText(getTimeLeft());
                          }
                        });
                    // Calls endscreen if the time reaches 0.
                    if (minutes == 0 && seconds == 0) {
                      try {
                        App.setRoot("endscreen");
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                    }
                  }
                }));

    timelineTime.setCycleCount(120);
    timelineTime.play();
  }

  /**
   * Creates a Timeline that encourages the players
   *
   * @param event the mouse event
   */
  public void startEncouraging() {
    timelineEncourage =
        new Timeline(
            new KeyFrame(
                Duration.seconds(20), // Runs every 20 seconds.
                new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
                    // Calls the API only if the GameMaster has been loaded.
                    if (isGameMasterLoaded) {
                      chatCompletionRequestEncourage.addMessage(
                          new ChatMessage(
                              "user",
                              "You are an AI game master of an escape room. Encourage the player"
                                  + " with at most five words."));
                      ChatCompletionResult chatCompletionResult;
                      try {
                        chatCompletionResult = chatCompletionRequestEncourage.execute();
                        Choice result = chatCompletionResult.getChoices().iterator().next();
                        Platform.runLater(
                            new Runnable() {
                              // Displays as a speech bubble to the user.
                              @Override
                              public void run() {
                                displayMessage(result.getChatMessage().getContent());
                              }
                            });
                      } catch (ApiProxyException e) {
                        e.printStackTrace();
                      }
                    }
                  }
                }));

    timelineEncourage.setCycleCount(4);
    timelineEncourage.play();
  }

  /**
   * Method comparing the inputted passcode to the correct password.
   *
   * @param event the mouse event
   */
  private void checkPasscode() {
    // If passcode is correct, pauses the timer and displays the endscreen.
    if (labelPasscode.getText().equals(labelNoteContent.getText())) {
      timelineTime.pause();
      isGameWon = true;
      timelineEncourage.pause();
      try {
        App.setRoot("endscreen");
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      showDialog(
          "Info",
          "That is not right",
          "Try again"); // Pop up letting player know the passcode was not right.
      labelPasscode.setText("");
    }
  }

  /**
   * Makes speech bubble visible
   *
   * @param message the message that is to be displayed
   */
  public void displayMessage(String message) {
    speechBubble.setOpacity(1);
    circle.setOpacity(1);
    labelChat.setText(message);
  }

  /** Hides speech bubble */
  public void removeMessage() {
    speechBubble.setOpacity(0);
    circle.setOpacity(0);
    labelChat.setText("");
  }
}
