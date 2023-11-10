package backend;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

class RecipeListTest {

    private RecipeList recipeList;
    private File databaseFile;
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;

    class FileMock extends File {
        public FileMock(String pathname) {
            super(pathname);
        }

        @Override
        public boolean exists() {
            return true;
        }
    }

    @BeforeEach
    public void setUp() {
        databaseFile = new File("test-recipes.json");
        recipeList = new RecipeList(databaseFile);

        Date date1 = new Date();
        Date date2 = new Date();
        Date date3 = new Date();

        recipe1 = new Recipe("Recipe 1", "Description 1", date1);
        recipe2 = new Recipe("Recipe 2", "Description 2", date2);
        recipe3 = new Recipe("Recipe 3", "Description 3", date3);
    }

    @Test
    void testAddRecipe() {
        File databaseFile = new File("test-recipes.json");
        RecipeList recipeList = new RecipeList(databaseFile);

        Recipe recipe = new Recipe("Test Recipe", "Test Instructions", new Date());
        recipeList.addRecipe(recipe);

        List<Recipe> recipes = recipeList.getRecipes();
        assertTrue(recipes.contains(recipe));

        // Clean up: remove the test database file
        databaseFile.delete();
    }

    @Test
    void testRemoveRecipe() {
        File databaseFile = new File("test-recipes.json");
        RecipeList recipeList = new RecipeList(databaseFile);

        Recipe recipe = new Recipe("Test Recipe", "Test Instructions", new Date());
        recipeList.addRecipe(recipe);

        recipeList.removeRecipe(recipe);

        List<Recipe> recipes = recipeList.getRecipes();
        assertFalse(recipes.contains(recipe));

        // Clean up: remove the test database file
        databaseFile.delete();
    }

    @Test
    void testUpdateJSON() {
        FileMock fileMock = new FileMock("test-recipes.json");
        RecipeList recipeList = new RecipeList(fileMock);
        long currentTime = System.currentTimeMillis();

        // JSONObject json = new JSONObject();
        // json.put("name", "Test Recipe");
        // json.put("instructions", "Test Instructions");
        // json.put("date", new Date(currentTime).toString());
        // String jsonString = json.toString();

        assertTrue(fileMock.exists());

        Recipe recipe = new Recipe("Test Recipe 1", "Test Instructions 1", new Date(currentTime));
        recipeList.addRecipe(recipe);

        recipeList.updateDatabase();
        assertTrue(fileMock.exists());

        // Iterate through the file and check that the JSON string is in the file
        String fileContents = "";
        try {
            java.util.Scanner scanner = new java.util.Scanner(fileMock);
            while (scanner.hasNextLine()) {
                fileContents += scanner.nextLine();
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }

        assertTrue(fileContents.contains("Test Recipe 1"));
        assertTrue(fileContents.contains("Test Instructions 1"));

        // Clean up: remove the test database file
        fileMock.delete();
    }

    @Test
    void testGetRecipesSortedByDate() {
        // Create a new recipe list and add two recipes with different dates
        File databaseFile = new File("test-recipes.json");
        RecipeList recipeList = new RecipeList(databaseFile);

        // Create two dates that have different times, recipe2 is earlier than recipe1
        long currentTime = System.currentTimeMillis();
        Recipe recipe1 = new Recipe("Recipe 1", "Test Instructions", new Date(currentTime - 1000));
        Recipe recipe2 = new Recipe("Recipe 2", "Test Instructions", new Date(currentTime));

        // Add the recipes to the list in reverse order
        recipeList.addRecipe(recipe2);
        recipeList.addRecipe(recipe1);

        // Sort the recipes by date
        recipeList.sortRecipesByDate();

        // Check that the first element is recipe2 and the second is recipe1
        List<Recipe> recipes = recipeList.getRecipes();
        assertEquals(recipe2.getTitle(), recipes.get(0).getTitle());
        assertEquals(recipe1, recipes.get(1));

        // Clean up: remove the test database file
        databaseFile.delete();
    }

    @Test
    void testScenarioDeleteRecipe() {
        // Create a new recipe list with three different recipes
        File databaseFile = new File("test-recipes.json");
        RecipeList recipeList = new RecipeList(databaseFile);
        long currentTime = System.currentTimeMillis();

        // Make sure that the JSON is updated when a recipe is deleted
        Recipe recipe1 = new Recipe("Recipe 1", "Test Instructions", new Date(currentTime - 1000));
        Recipe recipe2 = new Recipe("Recipe 2", "Test Instructions", new Date(currentTime));
        Recipe recipe3 = new Recipe("Recipe 3", "Test Instructions", new Date(currentTime + 1000));

        recipeList.addRecipe(recipe1);
        recipeList.addRecipe(recipe2);
        recipeList.addRecipe(recipe3);

        // Delete recipe3
        recipeList.removeRecipe(recipe3);

        RecipeList newRecipeList = new RecipeList(databaseFile);
        List<Recipe> recipes = newRecipeList.getRecipes();
        assertEquals(recipes.size(), 2);

        // Clean up: remove the test database file
        databaseFile.delete();
    }

    @Test
    void testScenarioUpdateDatabase() {
        File databaseFile = new File("test-recipes.json");
        RecipeList recipeList = new RecipeList(databaseFile);

        // Create two random dates that have different times
        Date date1 = new Date();
        Date date2 = new Date();

        Recipe recipe1 = new Recipe("Recipe 1", "Test Instructions", date1);
        Recipe recipe2 = new Recipe("Recipe 2", "Test Instructions", date2);

        recipeList.addRecipe(recipe2);
        recipeList.addRecipe(recipe1);

        // Save the recipes to the database file
        recipeList.updateDatabase();

        // Re-create the recipe list from the saved file
        RecipeList newRecipeList = new RecipeList(databaseFile);
        List<Recipe> newRecipes = newRecipeList.getRecipes();

        assertEquals(2, newRecipes.size());

        // Check that the first element is recipe2 and the second is recipe1
        assertEquals(recipe2.getTitle(), newRecipes.get(0).getTitle());
        assertEquals(recipe1.getTitle(), newRecipes.get(1).getTitle());

        // Clean up: remove the test database file
        databaseFile.delete();
    }

    @Test
    void testLoadRecipesFromFile() {
        // Add recipes to the recipeList
        recipeList.addRecipe(recipe1);
        recipeList.addRecipe(recipe2);
        recipeList.addRecipe(recipe3);

        // Save the recipes to the database file
        recipeList.updateDatabase();

        // Create a new RecipeList and load from the saved file
        RecipeList newRecipeList = new RecipeList(databaseFile);

        // Check that the loaded recipes match the added recipes
        List<Recipe> loadedRecipes = newRecipeList.getRecipes();
        List<Recipe> currentRecipes = recipeList.getRecipes(); // recipe4 was not added

        // Check that the loaded recipes match the added recipes
        assertEquals(currentRecipes.size(), loadedRecipes.size());
        assertEquals(currentRecipes.get(0).getTitle(), loadedRecipes.get(0).getTitle());
        assertEquals(currentRecipes.get(1).getTitle(), loadedRecipes.get(1).getTitle());
        assertEquals(currentRecipes.get(2).getTitle(), loadedRecipes.get(2).getTitle());

        // Clean up: remove the test database file
        databaseFile.delete();
    }
}
