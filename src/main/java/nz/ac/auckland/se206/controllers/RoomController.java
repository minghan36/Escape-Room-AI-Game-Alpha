package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Controller class for the room view. */
public class RoomController extends GameState{

  @FXML private Rectangle rectangleDoor;
  @FXML private Rectangle rectanglePillow;
  @FXML private Rectangle rectangleWindow;
  @FXML private Rectangle rectangleRed;
  @FXML private Rectangle rectangleGreen;
  @FXML private Rectangle rectangleBlue;
  @FXML private Label labelPasscode;
  @FXML private Label labelTimer;
  @FXML private Label labelNoteContent;
  @FXML private Label labelChat;
  @FXML private ImageView speechBubble;

  private static Timeline timeline;
  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {

    Thread timeThread = new Thread(()-> {
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

    if (!GameState.isRiddleResolved) {
      isGameMasterLoaded = true;
      speechBubble.setOpacity(1);
      labelChat.setText("SHIP AI LOADING...");
      App.setRoot("chat");
      return;
    }

    if (!GameState.isNoteFound) {
      showDialog(
          "Info",
          "Find the missing item!",
          "You resolved the riddle, now you know where the item is.");
    } else {
      showDialog("Info", "You Won!", "Good Job!");
    }
  }

  /**
   * Handles the click event on the vase.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickPillow(MouseEvent event) {
    System.out.println("pillow clicked");
    if (GameState.isRiddleResolved && !GameState.isNoteFound) {
      showDialog("Info", "Note Found", "RGBG");
      GameState.isNoteFound = true;
      labelNoteContent.setText("RGBG");
    }
  }

  @FXML
  public void clickWindow(MouseEvent event) {
    System.out.println("Window Clicked");
    if (isGameMasterLoaded){
    chatCompletionRequestWindow.addMessage(new ChatMessage("user", "With the following sentence as a guide. Aren't the stars beautiful. Acknowledge the beauty of space in at most four words."));
    ChatCompletionResult chatCompletionResult;
    try {
      chatCompletionResult = chatCompletionRequestWindow.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      speechBubble.setOpacity(1);
      labelChat.setText(result.getChatMessage().getContent());
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }
  }

  @FXML
  public void clickRed(MouseEvent event) {
    System.out.println("Red");
    if (GameState.isRiddleResolved && GameState.isNoteFound){
      labelPasscode.setText(labelPasscode.getText()+"R");
      if (labelPasscode.getText().length()==4){
        checkPasscode();
      }
    }
  }

  @FXML
  public void clickGreen(MouseEvent event) {
    System.out.println("Green");
    if (GameState.isRiddleResolved && GameState.isNoteFound){
      labelPasscode.setText(labelPasscode.getText()+"G");
      if (labelPasscode.getText().length()==4){
        checkPasscode();
      }
    }
  }

  @FXML
  public void clickBlue(MouseEvent event) {
    System.out.println("Blue");
    if (GameState.isRiddleResolved && GameState.isNoteFound){
      labelPasscode.setText(labelPasscode.getText()+"B");
      if (labelPasscode.getText().length()==4){
        checkPasscode();
      }
    }
  }

  @FXML void clickContent(){

  }

  public void startTimer() {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
                    if (seconds == 0) {
                      minutes--;
                      seconds = 59;
                    } else if (seconds > 0) {
                      seconds--;
                    }
                    if (isGameMasterLoaded && ((minutes*60 + seconds)%20 == 0)){

                    }
                    Platform.runLater(
                        new Runnable() {
                          @Override
                          public void run() {
                            labelTimer.setText(getTimeLeft());
                          }
                        });
                    if (minutes == 0 && seconds == 0){
                      try {
        App.setRoot("endscreen");
      } catch (IOException e) {
        e.printStackTrace();
      }
                    }
                  }
                }));

    timeline.setCycleCount(120);
    timeline.play();
  }

  private void checkPasscode(){
    if (labelPasscode.getText().equals(labelNoteContent.getText())){
      timeline.pause();
      isGameWon = true;
      try {
        App.setRoot("endscreen");
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      showDialog("Info", "That is not right", "Try again");
      labelPasscode.setText("");
    }
  }
}
