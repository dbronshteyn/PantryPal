package backend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

import org.json.*;

import ui.SceneController;

/**
 *  The Controller class is responsible for managing the recipes of this application.
 * 
 *  It initializes and interacts with a RecipeCreator, RecipeList and SceneController to create and show recipes, as well as dealing with user interactions.
 *  It also deals with reading from and saving to the JSON file holding the recipes.
 * 
 *  The class relies on an API key to access the ChatGPT and Whisper services.
 */

public class Controller {

    private static final String API_KEY = "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";

    RecipeCreator recipeCreator;
    RecipeList recipeList;
    SceneController sceneController;
    File databaseFile;

    Controller(File databaseFile) {
        this.recipeCreator = new RecipeCreator(new ChatGPT(API_KEY), new Whisper(API_KEY));
        this.recipeList = new RecipeList(); // or load from file if it exists
        this.databaseFile = databaseFile;
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

    /**
     * Saves a recipe into the json file (which acts as our database)
     * 
     * @param recipe the recipe to be added
     */
    public void saveJSON(Recipe recipe) {
        JSONObject jsonRecipe = new JSONObject();

        jsonRecipe.put("title", recipe.getTitle());        
        jsonRecipe.put("instructions", recipe.getInstructions());
        jsonRecipe.put("dateCreated", recipe.getDateCreated().toString());

        try {
            String content = new String(Files.readAllBytes(Paths.get(databaseFile.toURI())));
            JSONObject fileJSONObject = new JSONObject(content);
            JSONArray recipeList = fileJSONObject.getJSONArray("recipeList");
            FileWriter fw = new FileWriter(databaseFile, false);
            
            recipeList.put(jsonRecipe);
            fw.write(fileJSONObject.toString(1));
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
