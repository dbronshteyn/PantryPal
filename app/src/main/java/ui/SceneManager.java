package ui;

import javafx.scene.layout.BorderPane;

import java.io.File;

import middleware.Controller;

/**
 * This class represents the scene manager that manages the scenes of the
 * application.
 */
public class SceneManager extends BorderPane {

    public static final String FONT = "Arial";

    ListScene listScene;
    RecipeScene recipeScene;
    RecipeCreationScene recipeCreationScene;
    Controller controller;

    /**
     * Constructs a new SceneManager with the provided controller and audio files.
     * 
     * @param controller
     * @param ingredientsAudioFile
     * @param mealTypeAudioFile
     */
    public SceneManager(Controller controller, File ingredientsAudioFile, File mealTypeAudioFile) {
        this.setStyle("-fx-background-color: #e7ffe6;");

        this.listScene = new ListScene(this, controller);
        this.recipeScene = new RecipeScene(this, controller);
        this.recipeCreationScene = new RecipeCreationScene(this, controller, ingredientsAudioFile, mealTypeAudioFile);
        this.setCenter(listScene);

        this.controller = controller;
    }

    /**
     * Displays the recipe details.
     * 
     * @param recipeID
     */
    public void displayRecipeDetails(String recipeID) {
        recipeScene.displayRecipeDetails(recipeID);
    }

    /**
     * Displays the new recipe.
     * 
     * @param recipeID
     */
    public void displayNewlyCreatedRecipe(String recipeID) {
        recipeScene.displayNewlyCreatedRecipe(recipeID);
    }

    /**
     * Displays the recipe list.
     */
    public void displayRecipeList() {
        listScene.displayRecipeList();
    }

    /**
     * Displays the recipe creation scene.
     */
    public void displayRecipeCreationScene() {
        recipeCreationScene.displayRecipeCreationScene(Controller.generateNewRecipeBuilder());
    }
}