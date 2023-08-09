package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;

/** Controller class for the room view. */
public class RoomController {

  @FXML private Rectangle rectangleDoor;
  @FXML private Rectangle rectanglePillow;
  @FXML private Rectangle rectangleWindow;
  @FXML private Rectangle rectangleRed;
  @FXML private Rectangle rectangleGreen;
  @FXML private Rectangle rectangleBlue;
  @FXML private Label labelPasscode;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // Initialization code goes here
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
      showDialog("Info", "Riddle", "You need to resolve the riddle!");
      App.setRoot("chat");
      return;
    }

    if (!GameState.isNoteFound) {
      showDialog(
          "Info", "Find the missing item!", "You resolved the riddle, now you know where the item is.");
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
    System.out.println("vase clicked");
    if (GameState.isRiddleResolved && !GameState.isNoteFound) {
      showDialog("Info", "Key Found", "You found a note under the pillow!");
      GameState.isNoteFound = true;
    }
  }

  @FXML
  public void clickWindow(MouseEvent event){
    System.out.println("Window Clicked");
    showDialog("Info", "Window", "Isn't it beautiful!");
  }

  @FXML
  public void clickRed(MouseEvent event){
    System.out.println("Red");
  }

  @FXML
  public void clickGreen(MouseEvent event){
    System.out.println("Green");
  }

  @FXML
  public void clickBlue(MouseEvent event){
    System.out.println("Blue");
  }
}
