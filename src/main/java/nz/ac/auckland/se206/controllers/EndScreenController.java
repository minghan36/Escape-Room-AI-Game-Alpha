package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.GameState;

public class EndScreenController extends GameState{
    
    @FXML private Label labelEnd;

    public void initialize(){
        labelEnd.setText(labelEnd.getText()+" "+getTimeLeft());
    }
}
