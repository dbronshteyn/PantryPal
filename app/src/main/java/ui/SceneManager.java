package ui;

import javafx.scene.layout.BorderPane;

import java.io.File;

import middleware.Controller;


public class SceneManager extends BorderPane {

    public static final String FONT = "Arial";

    ListScene listScene;
    RecipeScene recipeScene;
    RecipeCreationScene recipeCreationScene;
    Controller controller;

    public SceneManager(Controller controller, File ingredientsAudioFile, File mealTypeAudioFile) {
        this.setStyle("-fx-background-color: #e7ffe6;");

        this.listScene = new ListScene(this, controller);
        this.recipeScene = new RecipeScene(this, controller);
        this.recipeCreationScene = new RecipeCreationScene(this, controller, ingredientsAudioFile, mealTypeAudioFile);
        this.setCenter(listScene);

        this.controller = controller;
    }

    public void displayRecipeDetails(String recipeID) {
        recipeScene.displayRecipeDetails(recipeID);
    }

    public void displayNewlyCreatedRecipe(String recipeID) {
        recipeScene.displayNewlyCreatedRecipe(recipeID);
    }

    public void displayRecipeList() {
        listScene.displayRecipeList();
    }

    public void displayRecipeCreationScene() {
        recipeCreationScene.displayRecipeCreationScene(controller.generateNewRecipeBuilder());
    }
}