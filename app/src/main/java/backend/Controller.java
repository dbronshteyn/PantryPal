package backend;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

    RecipeList recipeList;
    Map<String, RecipeBuilder> recipeBuilders;
    Map<String, Recipe> temporaryRecipes;

  
    public Controller(File databaseFile) {
        this.recipeList = new RecipeList(databaseFile);
        this.recipeBuilders = new HashMap<>();
        this.temporaryRecipes = new HashMap<>();
    }

    public String generateNewRecipeBuilder() {
        ChatGPT chatGPT = new ChatGPT(API_KEY);
        Whisper whisper = new Whisper(API_KEY);
        RecipeBuilder recipeBuilder = new RecipeBuilder(chatGPT, whisper);
        this.recipeBuilders.put(recipeBuilder.getRecipeID(), recipeBuilder);
        return recipeBuilder.getRecipeID();
    }

    public String getRecipeTitle(String recipeID) {
        if (temporaryRecipes.containsKey(recipeID))
            return this.temporaryRecipes.get(recipeID).getTitle();
        return this.recipeList.getRecipeByID(recipeID).getTitle();
    }

    public String getRecipeInstructions(String recipeID) {
        if (temporaryRecipes.containsKey(recipeID))
            return this.temporaryRecipes.get(recipeID).getInstructions();
        return this.recipeList.getRecipeByID(recipeID).getInstructions();
    }

    public List<String> getRecipeIDs() {
        return this.recipeList.getRecipeIDs();
    }

    public void resetRecipeCreatorElement(String recipeID, String elementName) {
        if (elementName.equals("mealType")) {
            this.recipeBuilders.get(recipeID).getMealTypeElement().reset();
        } else if (elementName.equals("ingredients")) {
            this.recipeBuilders.get(recipeID).getIngredientsElement().reset();
        } else {
            throw new IllegalArgumentException("Invalid element name");
        }
    }

    public String specifyRecipeCreatorElement(String recipeID, String elementName, File audioFile) {
        try {
            if (elementName.equals("mealType")) {
                return this.recipeBuilders.get(recipeID).getMealTypeElement().specify(audioFile);
            } else if (elementName.equals("ingredients")) {
                return this.recipeBuilders.get(recipeID).getIngredientsElement().specify(audioFile);
            } else {
                throw new IllegalArgumentException("Invalid element name");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isRecipeCreatorCompleted(String recipeID) {
        return this.recipeBuilders.get(recipeID).isCompleted();
    }

    public void generateRecipe(String recipeID) {
        try {
            Recipe recipe = this.recipeBuilders.remove(recipeID).returnRecipe();
            this.temporaryRecipes.put(recipe.getRecipeID(), recipe);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeRecipe(String recipeID) {
        this.recipeList.removeRecipe(this.recipeList.getRecipeByID(recipeID));
    }

    public void saveRecipe(String recipeID) {
        this.recipeList.addRecipe(this.temporaryRecipes.remove(recipeID));
    }

    public void editRecipe(String recipeID, String newInstructions) {
        this.recipeList.getRecipeByID(recipeID).setInstructions(newInstructions);
        this.recipeList.updateDatabase();
        this.recipeList.sortRecipesByDate();
    }
}
