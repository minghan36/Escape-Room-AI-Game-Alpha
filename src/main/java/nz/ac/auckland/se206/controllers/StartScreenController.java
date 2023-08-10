package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;

public class StartScreenController extends GameState{
    
    @FXML 
    private Button buttonStart;

    @FXML
    private void pressStart() throws IOException{
        App.setRoot("room");
    }
}
