package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import nz.ac.auckland.se206.GameState;

public class EndScreenController extends GameState {
  /** Controller class for the End Screen */
  @FXML private Label labelEndTime;

  @FXML private Label labelEndTitle;
  @FXML private Label labelEndBody;

  // Initiallizes the view of the end screen.
  public void initialize() {
    labelEndTime.setText(labelEndTime.getText() + " " + getTimeLeft());
    // Applies a "red" colour to the text if the player has lost. Changes the label contents
    // accordingly.
    if (!isGameWon) {
      labelEndTime.setTextFill(Paint.valueOf("#890e00"));
      labelEndTitle.setTextFill(Paint.valueOf("#890e00"));
      labelEndBody.setTextFill(Paint.valueOf("#890e00"));
      labelEndTitle.setText("TIME IS UP!");
      labelEndBody.setText("THE GUARD IS BACK, YOU CAN NO LONGER ESCAPE!");
    }
  }
}
