package backend;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.SceneController;

import java.util.ArrayList;
import java.util.Date;

public class Main extends Application {

    static final int WINDOW_WIDTH = 950;
    static final int WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneController root = new SceneController("ingredients.wav");
        primaryStage.setTitle("PantryPal");
        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();

        List<Recipe> testList = new ArrayList<>();
        testList.add(new Recipe("Pasta", "Boil water, add pasta, add sauce", new Date()));
        testList.add(new Recipe("Pizza", "Buy pizza, heat oven, put pizza in oven", new Date()));
        RecipeList recipes = new RecipeList(testList);
        root.displayRecipeList(recipes);
    }

    public static void main(String[] args) {
        launch(args);
    }
}