package backend;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import java.io.File;

import com.google.gson.reflect.TypeToken;

public class RecipeList {

    private List<Recipe> recipes;
    private File databaseFile;

    public RecipeList(File databaseFile) {
        this.recipes = new ArrayList<>();
        this.databaseFile = databaseFile;
        this.loadRecipesFromFile();
        this.sortRecipesByDate();
    }

    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
        this.updateDatabase();
        this.sortRecipesByDate();
    }

    public void removeRecipe(Recipe recipe) {
        this.recipes.remove(recipe);
        this.updateDatabase();
    }

    /*
     * Returns an unmodifiable list of recipes sorted by date created in descending
     * 
     * Daniel's reasoning. I want this list to not be modifiable by other classes
     * just in case. This is the reason that I am relying on the
     * Collections.unmodifiableList class so that I don't return a pointer directly
     * to out list. Let me know if you have any questions.
     */
    public List<Recipe> getRecipes() {
        return Collections.unmodifiableList(this.recipes);
    }

    public void sortRecipesByDate() {
        // Sort the list with a custom comparator that compares the dates
        Collections.sort(this.recipes, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe r1, Recipe r2) {
                // Sort in descending order so the most recent dates come first
                return r2.getDateCreated().compareTo(r1.getDateCreated());
            }
        });
    }

    /**
     * Saves all recipes into the json file (which acts as our database)
     * 
     * @param recipe the recipe to be added
     */
    public void updateDatabase() {

        // to unit test this, use dependency inversion and take a FileWriter object as a method parameter
        // then mock the FileWriter object and make sure it writes the correct things
        // same for loadRecipesFromFile()

        JSONArray jsonRecipeList = new JSONArray();
        for (Recipe recipe : this.recipes) {
            jsonRecipeList.put(recipe.toJSON());
        }
        try {
            FileWriter fw = new FileWriter(this.databaseFile);
            fw.write(jsonRecipeList.toString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRecipesFromFile() {
        if (this.databaseFile.exists()) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(this.databaseFile.getAbsolutePath())));
                JSONArray jsonRecipeList = new JSONArray(content);
                for (int i = 0; i < jsonRecipeList.length(); i++) {
                    JSONObject jsonRecipe = jsonRecipeList.getJSONObject(i);              
                    Recipe recipe = new Recipe(jsonRecipe);
                    this.recipes.add(recipe);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
