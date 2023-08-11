package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  private static Scene scene;
  private static Parent room = null;
  private static Parent chat = null;
  public static void main(final String[] args) {
    launch();
  }

  public static void setRoot(String fxml) throws IOException {
    if (fxml.equals("room") && room != null) {
      scene.setRoot(room);
      return;
    } else if (fxml.equals("chat") && chat != null){
      scene.setRoot(chat);
      return;
    }
    //Uses threads to load the roots of the different views.
    Thread sceneThread =
        new Thread(
            () -> {
              Parent root;
              try {
                root = loadFxml(fxml);
                //Sets the roots once loaded, so they can be called in the future.
                if (fxml.equals("room") && room == null) {
                  room = root;
                } else if (fxml.equals("chat") && chat == null){
                  chat = root;
                }
                Platform.runLater(
                    new Runnable() {
                      @Override
                      public void run() {
                        scene.setRoot(root);
                      }
                    });
              } catch (IOException e) {
                e.printStackTrace();
                return;
              }
            });
    sceneThread.start();
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    Parent root = loadFxml("startscreen");
    scene = new Scene(root, 600, 470);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
  }
}
