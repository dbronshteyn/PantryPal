package ui;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Date;
import java.io.File;

import ui.SceneManager;
import backend.Controller;


public class Main extends Application {

    static final int WINDOW_WIDTH = 950;
    static final int WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) throws Exception {
        File ingredientsAudioFile = new File("ingredients.wav");
        File mealTypeAudioFile = new File("mealType.wav");
        File databaseFile = new File("database.json");
      
        Controller controller = new Controller(databaseFile);
        SceneManager sceneManager = new SceneManager(controller, ingredientsAudioFile, mealTypeAudioFile);
        
        primaryStage.setTitle("PantryPal");
        primaryStage.setScene(new Scene(sceneManager, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
        
        sceneManager.displayRecipeList();
    }

    public static void main(String[] args) {
        launch(args);
    }
}