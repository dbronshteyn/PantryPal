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
    Controller controller;
    RecipeScene recipeScene;
    RecipeCreationScene recipeCreationScene;
    AccountCreationScene accountCreationScene;
    LoginScene loginScene;

    private String sort = "most-recent";

    /**
     * Constructs a new SceneManager with the provided controller and audio files.
     * 
     * @param ingredientsAudioFile
     * @param mealTypeAudioFile
     */
    public SceneManager(Controller controller, File ingredientsAudioFile, File mealTypeAudioFile) {
        this.controller = controller;

        this.setStyle("-fx-background-color: #e7ffe6;");
        this.listScene = new ListScene(this, controller);
        this.recipeScene = new RecipeScene(this, controller);
        this.recipeCreationScene = new RecipeCreationScene(this, controller, ingredientsAudioFile, mealTypeAudioFile);
        this.accountCreationScene = new AccountCreationScene(this, controller);
        this.loginScene = new LoginScene(this, controller);

        this.setCenter(listScene);
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return this.sort;
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
        listScene.displayRecipeList(sort);
    }

    /**
     * Displays the recipe creation scene.
     */
    public void displayRecipeCreationScene() {
        recipeCreationScene.displayRecipeCreationScene(controller.generateNewRecipeBuilder());
    }

    public void displayLoginScene() {
        loginScene.displayLoginScene();
    }

    /**
     * Displays the account creation scene.
     */
    public void displayAccountCreationScene() {
        accountCreationScene.displayAccountCreationScene();
    }
}