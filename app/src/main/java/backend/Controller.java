package backend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.nio.file.Files;

import org.json.*;

import ui.SceneController;

/**
 * The Controller class is responsible for managing the recipes of this
 * application.
 * 
 * It initializes and interacts with a RecipeCreator, RecipeList and
 * SceneController to create and show recipes, as well as dealing with user
 * interactions.
 * It also deals with reading from and saving to the JSON file holding the
 * recipes.
 * 
 * The class relies on an API key to access the ChatGPT and Whisper services.
 */

public class Controller {
    RecipeList recipeList;
    SceneController sceneController;

    private static final String API_KEY = "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";

  
    Controller(File databaseFile) {
        this.recipeList = new RecipeList(databaseFile);
        this.sceneController = null;
    }

    public void initialize(SceneController sceneController) {
        this.sceneController = sceneController;
        this.sceneController.displayRecipeList(this.recipeList);
    }

    public RecipeBuilder generateNewRecipeBuilder() {
        ChatGPT chatGPT = new ChatGPT(API_KEY);
        Whisper whisper = new Whisper(API_KEY);
        RecipeBuilder recipeBuilder = new RecipeBuilder(chatGPT, whisper);
        return recipeBuilder;
    }

    public void saveEdits(Recipe recipe, String newInstructions) {
        recipe.setInstructions(newInstructions);
        this.recipeList.updateDatabase();
        this.recipeList.sortRecipesByDate();
        this.sceneController.displayRecipeList(recipeList);
        this.sceneController.displayRecipeDetails(recipe);
    }

    public void removeRecipe(Recipe recipe) {
        this.recipeList.removeRecipe(recipe);
        this.sceneController.displayRecipeList(this.recipeList);
    }

    public RecipeList getRecipeList() {
        return this.recipeList;
    }
}
