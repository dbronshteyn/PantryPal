package backend;

import java.io.File;
import java.io.IOException;

import ui.SceneController;

public class Controller {

    RecipeCreator recipeCreator;
    RecipeList recipeList;
    SceneController sceneController;

    Controller() {
        this.recipeCreator = new RecipeCreator();
        this.recipeList = new RecipeList(); // or load from file if it exists
    }

    public void initialize(SceneController sceneController) {
        this.sceneController = sceneController;
        this.sceneController.displayRecipeList(this.recipeList);
    }

    public void createAndShowRecipe(File ingredientsAudioFile) {
        try {
            Recipe recipe = this.recipeCreator.createRecipe(ingredientsAudioFile);
            this.sceneController.displayRecipeDetails(recipe);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
