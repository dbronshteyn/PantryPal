package backend;

import java.io.File;
import java.io.IOException;

import ui.SceneController;

/**
 *  The Controller class is responsible for managing the recipes of this application.
 * 
 *  It initializes and interacts with a RecipeCreator, RecipeList and SceneController to create and show recipes, as well as dealing with user interactions.
 * 
 *  The class relies on an API key to access the ChatGPT and Whisper services.
 */

public class Controller {

    private static final String API_KEY = "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";

    RecipeCreator recipeCreator;
    RecipeList recipeList;
    SceneController sceneController;
    Whisper whisper;

    Controller() {
        this.recipeCreator = new RecipeCreator(new ChatGPT(API_KEY));
        this.whisper = new Whisper(API_KEY);
        this.recipeList = new RecipeList(); // or load from file if it exists
    }

    public void initialize(SceneController sceneController) {
        this.sceneController = sceneController;
        this.sceneController.displayRecipeList(this.recipeList);
    }

    public void createAndShowRecipe(String mealType, String ingredients) {
        try {
            Recipe recipe = this.recipeCreator.createRecipe(mealType, ingredients);
            this.sceneController.displayNewlyCreatedRecipe(recipe, this.recipeList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Whisper getWhisper() {
        return this.whisper;
    }
}
