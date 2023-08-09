package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;

public class StartScreenController {
    
    @FXML 
    private Button buttonStart;

    @FXML
    private void pressStart() throws IOException{
        App.setRoot("room");
    }
}
