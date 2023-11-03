package ui;

import backend.RecipeList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ListScene extends Application {

    private RecipeList recipeManager;

    @Override
    public void start(Stage primaryStage) {
        recipeManager = new RecipeList(); // Initialize your recipe manager here

        // Create UI components
        Button btnListRecipes = new Button("List All Recipes");
        btnListRecipes.setOnAction(e -> listRecipes());

        Button btnAddRecipe = new Button("Add New Recipe");
        // Add action to navigate to Add Recipe screen (not implemented in this example)
        btnAddRecipe.setOnAction(e -> addRecipe());

        Button btnExit = new Button("Exit");
        btnExit.setOnAction(e -> primaryStage.close());

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(btnListRecipes, btnAddRecipe, btnExit);

        // Scene
        Scene scene = new Scene(layout, 300, 250);

        // Stage
        primaryStage.setTitle("Recipe Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void listRecipes() {
        // Code to display the list of recipes

        // Example of how to get the list of recipes from the recipe manager

    }

    private void addRecipe() {
        // Code to display the add recipe form
    }

    public static void main(String[] args) {
        launch(args);
    }
}
