package backend;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.SceneController;

import java.util.ArrayList;
import java.util.Date;

import java.io.File;

public class Main extends Application {

    static final int WINDOW_WIDTH = 950;
    static final int WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) throws Exception {
        File ingredientsAudioFile = new File("ingredients.wav");
        File databaseFile = new File("database.json");
        Controller controller = new Controller(databaseFile);
        SceneController sceneController = new SceneController(controller, ingredientsAudioFile);

        primaryStage.setTitle("PantryPal");
        primaryStage.setScene(new Scene(sceneController, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
        controller.initialize(sceneController);
    }

    public static void main(String[] args) {
        launch(args);
    }
}