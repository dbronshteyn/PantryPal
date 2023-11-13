package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;


/**
 * This class represents the main class that runs the application.
 */
public class Main extends Application {

    static final int WINDOW_WIDTH = 950;
    static final int WINDOW_HEIGHT = 600;

    /**
     * Starts the UI.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        File ingredientsAudioFile = new File("ingredients.wav");
        File mealTypeAudioFile = new File("mealType.wav");

        SceneManager sceneManager = new SceneManager(ingredientsAudioFile, mealTypeAudioFile);

        primaryStage.setTitle("PantryPal");
        primaryStage.setScene(new Scene(sceneManager, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();

        // displays the home screen at startup
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