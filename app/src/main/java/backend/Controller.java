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

    private static final String API_KEY = "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";

    RecipeCreator recipeCreator;
    RecipeList recipeList;
    SceneController sceneController;
    Whisper whisper;
    File databaseFile;

  
    Controller(File databaseFile) {
        this.recipeCreator = new RecipeCreator(new ChatGPT(API_KEY));
        this.whisper = new Whisper(API_KEY);
        this.recipeList = new RecipeList(); // or load from file if it exists
        this.databaseFile = databaseFile;
        loadRecipesFromJsonFile(); // Load recipes from the JSON file

        this.sceneController = null; // Initialize sceneController, it will be set later

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

    private void loadRecipesFromJsonFile() {
        try {
            if (databaseFile.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(databaseFile.toURI())));
                JSONObject fileJSONObject = new JSONObject(content);
                JSONArray recipeListArray = fileJSONObject.getJSONArray("recipeList");

                // Initialize the RecipeList from the JSON data
                this.recipeList = new RecipeList(recipeListArray);
            } else {
                this.recipeList = new RecipeList(); // Create an empty RecipeList
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Whisper getWhisper() {
        return this.whisper;
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

        // Format the dateCreated field in ISO 8601 format
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        String iso8601Date = iso8601Format.format(recipe.getDateCreated());
        jsonRecipe.put("dateCreated", iso8601Date);

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
