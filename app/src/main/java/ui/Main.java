package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

import middleware.Controller;

/**
 * This class represents the main class that runs the application.
 */
public class Main extends Application {

    static final int WINDOW_WIDTH = 950;
    static final int WINDOW_HEIGHT = 600;

    /**
     * Starts the application.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        File ingredientsAudioFile = new File("ingredients.wav");
        File mealTypeAudioFile = new File("mealType.wav");

        Controller controller = new Controller();
        SceneManager sceneManager = new SceneManager(controller, ingredientsAudioFile, mealTypeAudioFile);

        primaryStage.setTitle("PantryPal");
        primaryStage.setScene(new Scene(sceneManager, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();

        sceneManager.displayRecipeList();
    }

    /**
     * Runs the application.
     * 
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}