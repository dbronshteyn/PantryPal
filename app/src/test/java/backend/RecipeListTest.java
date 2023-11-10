package backend;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

import java.io.IOException;

import java.util.Date;

class RecipeListTest {

    private RecipeList recipeList;
    private File databaseFile;

    @BeforeEach
    public void setUp() {
        databaseFile = new File("test-database.json");
        if (databaseFile.exists()) {
            databaseFile.delete();
        }
        recipeList = new RecipeList(databaseFile);
    }

    @Test
    void testAddRecipe() {
        Recipe recipe = new Recipe("Test Recipe", "Test Instructions", new Date());
        recipeList.addRecipe(recipe);

        List<Recipe> recipes = recipeList.getRecipes();

        assertEquals(1, recipes.size());
        assertTrue(recipes.contains(recipe));
    }

    @Test
    void testRemoveRecipe() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Instructions", new Date());
        Recipe recipe2 = new Recipe("Test Recipe 2", "Test Instructions 2", new Date());

        recipeList.getRecipes().add(recipe1);
        recipeList.getRecipes().add(recipe2);

        recipeList.removeRecipe(recipe2);

        assertEquals(1, recipeList.getRecipes().size());
        assertFalse(recipeList.getRecipes().contains(recipe2));
    }

    @Test
    void testSortRecipesByDate() {
        long currentTime = System.currentTimeMillis();
        Recipe recipe1 = new Recipe("Test Recipe", "Test Instructions", new Date(currentTime - 1000));
        Recipe recipe2 = new Recipe("Test Recipe 2", "Test Instructions 2", new Date(currentTime));
        Recipe recipe3 = new Recipe("Test Recipe 3", "Test Instructions 3", new Date(currentTime + 1000));

        recipeList.getRecipes().add(recipe1);
        recipeList.getRecipes().add(recipe2);
        recipeList.getRecipes().add(recipe3);

        recipeList.sortRecipesByDate();

        assertEquals(3, recipeList.getRecipes().size());
        assertEquals(recipe3, recipeList.getRecipes().get(0));
        assertEquals(recipe2, recipeList.getRecipes().get(1));
        assertEquals(recipe1, recipeList.getRecipes().get(2));
    }

    @Test
    void testUpdateDatabase() throws IOException {
        long currentTime = System.currentTimeMillis();
        Recipe recipe1 = new Recipe("Test Recipe", "Test Instructions", new Date(currentTime - 1000)); // oldest
        Recipe recipe2 = new Recipe("Test Recipe 2", "Test Instructions 2", new Date(currentTime)); // middle
        Recipe recipe3 = new Recipe("Test Recipe 3", "Test Instructions 3", new Date(currentTime + 1000)); // newest

        recipeList.getRecipes().add(recipe1);
        recipeList.getRecipes().add(recipe2);
        recipeList.getRecipes().add(recipe3);

        recipeList.updateDatabase();

        assertTrue(databaseFile.exists());
        String content = new String(Files.readAllBytes(Paths.get(this.databaseFile.getAbsolutePath())));
        JSONArray jsonRecipeList = new JSONArray(content);

        assertEquals(3, jsonRecipeList.length());
        JSONObject jsonRecipe1 = jsonRecipeList.getJSONObject(0);
        JSONObject jsonRecipe2 = jsonRecipeList.getJSONObject(1);
        JSONObject jsonRecipe3 = jsonRecipeList.getJSONObject(2);
        assertEquals("Test Recipe", jsonRecipe1.getString("title"));
        assertEquals("Test Instructions 2", jsonRecipe2.getString("instructions"));

        String dateCreated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date(currentTime + 1000));

        assertEquals(dateCreated, jsonRecipe3.getString("dateCreated"));
    }

    // @Test
    // void testGetRecipesSortedByDate() {
    // // Create a new recipe list and add two recipes with different dates
    // File databaseFile = new File("test-recipes.json");
    // RecipeList recipeList = new RecipeList(databaseFile);

    // // Create two dates that have different times, recipe2 is earlier than
    // recipe1
    // long currentTime = System.currentTimeMillis();
    // Recipe recipe1 = new Recipe("Recipe 1", "Test Instructions", new
    // Date(currentTime - 1000));
    // Recipe recipe2 = new Recipe("Recipe 2", "Test Instructions", new
    // Date(currentTime));

    // // Add the recipes to the list in reverse order
    // recipeList.addRecipe(recipe2);
    // recipeList.addRecipe(recipe1);

    // // Sort the recipes by date
    // recipeList.sortRecipesByDate();

    // // Check that the first element is recipe2 and the second is recipe1
    // List<Recipe> recipes = recipeList.getRecipes();
    // assertEquals(recipe2.getTitle(), recipes.get(0).getTitle());
    // assertEquals(recipe1, recipes.get(1));

    // // Clean up: remove the test database file
    // databaseFile.delete();
    // }

    // @Test
    // void testScenarioDeleteRecipe() {
    // // Create a new recipe list with three different recipes
    // File databaseFile = new File("test-recipes.json");
    // RecipeList recipeList = new RecipeList(databaseFile);
    // long currentTime = System.currentTimeMillis();

    // // Make sure that the JSON is updated when a recipe is deleted
    // Recipe recipe1 = new Recipe("Recipe 1", "Test Instructions", new
    // Date(currentTime - 1000));
    // Recipe recipe2 = new Recipe("Recipe 2", "Test Instructions", new
    // Date(currentTime));
    // Recipe recipe3 = new Recipe("Recipe 3", "Test Instructions", new
    // Date(currentTime + 1000));

    // recipeList.addRecipe(recipe1);
    // recipeList.addRecipe(recipe2);
    // recipeList.addRecipe(recipe3);

    // // Delete recipe3
    // recipeList.removeRecipe(recipe3);

    // RecipeList newRecipeList = new RecipeList(databaseFile);
    // List<Recipe> recipes = newRecipeList.getRecipes();
    // assertEquals(recipes.size(), 2);

    // // Clean up: remove the test database file
    // databaseFile.delete();
    // }

    // @Test
    // void testScenarioUpdateDatabase() {
    // File databaseFile = new File("test-recipes.json");
    // RecipeList recipeList = new RecipeList(databaseFile);

    // // Create two random dates that have different times
    // Date date1 = new Date();
    // Date date2 = new Date();

    // Recipe recipe1 = new Recipe("Recipe 1", "Test Instructions", date1);
    // Recipe recipe2 = new Recipe("Recipe 2", "Test Instructions", date2);

    // recipeList.addRecipe(recipe2);
    // recipeList.addRecipe(recipe1);

    // // Save the recipes to the database file
    // recipeList.updateDatabase();

    // // Re-create the recipe list from the saved file
    // RecipeList newRecipeList = new RecipeList(databaseFile);
    // List<Recipe> newRecipes = newRecipeList.getRecipes();

    // assertEquals(2, newRecipes.size());

    // // Check that the first element is recipe2 and the second is recipe1
    // assertEquals(recipe2.getTitle(), newRecipes.get(0).getTitle());
    // assertEquals(recipe1.getTitle(), newRecipes.get(1).getTitle());

    // // Clean up: remove the test database file
    // databaseFile.delete();
    // }

    // @Test
    // void testLoadRecipesFromFile() {
    // // Add recipes to the recipeList
    // recipeList.addRecipe(recipe1);
    // recipeList.addRecipe(recipe2);
    // recipeList.addRecipe(recipe3);

    // // Save the recipes to the database file
    // recipeList.updateDatabase();

    // // Create a new RecipeList and load from the saved file
    // RecipeList newRecipeList = new RecipeList(databaseFile);

    // // Check that the loaded recipes match the added recipes
    // List<Recipe> loadedRecipes = newRecipeList.getRecipes();
    // List<Recipe> currentRecipes = recipeList.getRecipes(); // recipe4 was not
    // added

    // // Check that the loaded recipes match the added recipes
    // assertEquals(currentRecipes.size(), loadedRecipes.size());
    // assertEquals(currentRecipes.get(0).getTitle(),
    // loadedRecipes.get(0).getTitle());
    // assertEquals(currentRecipes.get(1).getTitle(),
    // loadedRecipes.get(1).getTitle());
    // assertEquals(currentRecipes.get(2).getTitle(),
    // loadedRecipes.get(2).getTitle());

    // // Clean up: remove the test database file
    // databaseFile.delete();
    // }
}
