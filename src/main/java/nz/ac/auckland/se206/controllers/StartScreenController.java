package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

public class StartScreenController extends GameState{
    
    @FXML 
    private Button buttonStart;

    public void initialize(){
        chatCompletionRequestChat = new ChatCompletionRequest().setN(1).setTemperature(1).setTopP(0.8).setMaxTokens(100);
        chatCompletionRequestWindow = new ChatCompletionRequest().setN(1).setTemperature(1.3).setTopP(1).setMaxTokens(100);
        chatCompletionRequestEncourage = new ChatCompletionRequest().setN(1).setTemperature(1.25).setTopP(1).setMaxTokens(100);
    }

    @FXML
    private void pressStart() throws IOException{
        App.setRoot("room");
    }
}
