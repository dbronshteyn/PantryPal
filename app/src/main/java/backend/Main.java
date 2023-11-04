package backend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.SceneController;

public class Main extends Application {

    static final int WINDOW_WIDTH = 950;
    static final int WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneController root = new SceneController(primaryStage);
        primaryStage.setTitle("PantryPal");
        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}