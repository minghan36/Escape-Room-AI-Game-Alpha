package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Controller class for the chat view. */
public class ChatController extends GameState {
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;
  @FXML private Circle circleOne;
  @FXML private Circle circleTwo;
  @FXML private Circle circleThree;
  @FXML private Label labelTimer;
  private Choice result = null;

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    // Thread for timer
    Thread timeThread =
        new Thread(
            () -> {
              startTimer();
            });
    timeThread.start();
    Thread computerThread =
        new Thread(
            () -> {
              chatCompletionRequestChat =
                  new ChatCompletionRequest()
                      .setN(1)
                      .setTemperature(1)
                      .setTopP(0.8)
                      .setMaxTokens(100);
              try {
                runGpt(
                    new ChatMessage("user", GptPromptEngineering.getRiddleWithGivenWord("pillow")));
              } catch (ApiProxyException e) {
                e.printStackTrace();
              }
              textToSpeech.speak(result.getChatMessage().getContent());
            });
    computerThread.start();
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    chatTextArea.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequestChat.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequestChat.execute();
      result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequestChat.addMessage(result.getChatMessage());
      Platform.runLater(
          new Runnable() {
            @Override
            public void run() {
              appendChatMessage(result.getChatMessage());
            }
          });
      setCircles(0);
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    String message = inputText.getText();
    if (message.trim().isEmpty()) {
      return;
    }
    setCircles(0.6);
    System.out.println("send clicked");
    inputText.clear();
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    Thread computerThread =
        new Thread(
            () -> {
              ChatMessage lastMsg;
              try {
                lastMsg = runGpt(msg);
                if (lastMsg.getRole().equals("assistant")
                    && lastMsg.getContent().startsWith("Correct")) {
                  GameState.isRiddleResolved = true;
                }
              } catch (ApiProxyException e) {
                e.printStackTrace();
              }
            });
    computerThread.start();
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    App.setRoot("room");
  }

  /**
   * Changes Opacity of circles
   *
   * @param arg The double value for the Opacity to be set to.
   */
  private void setCircles(double arg) {
    circleOne.setOpacity(arg);
    circleTwo.setOpacity(arg);
    circleThree.setOpacity(arg);
  }

  /** Creates a TimeLine that updates to time left in the game every second */
  public void startTimer() {
    Timeline timelineTime =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
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
                      textToSpeech.terminate();
                      try {
                        App.setRoot("endscreen");
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                    }
                  }
                }));

    timelineTime.setCycleCount((minutes * 60) + seconds - 1);
    timelineTime.play();
  }
}
