package backend;

import java.io.File;
import java.io.IOException;

import ui.SceneController;

public class Controller {

    private static final String API_KEY = "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";

    RecipeCreator recipeCreator;
    RecipeList recipeList;
    SceneController sceneController;

    Controller() {
        this.recipeCreator = new RecipeCreator(new ChatGPT(API_KEY), new Whisper(API_KEY));
        this.recipeList = new RecipeList(); // or load from file if it exists
    }

    public void initialize(SceneController sceneController) {
        this.sceneController = sceneController;
        this.sceneController.displayRecipeList(this.recipeList);
    }

    public void createAndShowRecipe(File ingredientsAudioFile) {
        try {
            Recipe recipe = this.recipeCreator.createRecipe(ingredientsAudioFile);
            this.sceneController.displayNewlyCreatedRecipe(recipe, this.recipeList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
