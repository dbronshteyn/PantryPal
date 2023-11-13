package backend;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

import java.io.IOException;

import java.util.Date;

/**
 * Unit testing for RecipeList class
 */
class RecipeListTest {

    private RecipeList recipeList;
    private File databaseFile;

    /**
     * Sets up the RecipeListTest.
     */
    @BeforeEach
    public void setUp() {
        databaseFile = new File("test-database.json");
        if (databaseFile.exists()) {
            databaseFile.delete();
        }
        recipeList = new RecipeList(databaseFile);
    }

    /**
     * Tears down the RecipeListTest database file.
     */
    @AfterEach
    public void tearDown() {
        if (databaseFile.exists()) {
            databaseFile.delete();
        }
    }

    /**
     * Tests that the RecipeList properly adds a recipe.
     */
    @Test
    void testAddRecipe() {
        Recipe recipe = new Recipe("", "Test Recipe", "Test Instructions", new Date());
        recipeList.addRecipe(recipe);

        List<Recipe> recipes = recipeList.getRecipes();

        assertEquals(1, recipes.size());
        assertTrue(recipes.contains(recipe));
    }

    /**
     * Tests that the RecipeList returns recipe IDs.
     */
    @Test
    void testGetRecipeIDs() {
        recipeList.getRecipes().add(new Recipe("id 1", "Test Recipe", "Test Instructions", new Date()));
        recipeList.getRecipes().add(new Recipe("id 2", "Test Recipe 2", "Test Instructions 2", new Date()));
        recipeList.getRecipes().add(new Recipe("id 3", "Test Recipe 3", "Test Instructions 3", new Date()));

        List<String> recipeIDs = recipeList.getRecipeIDs();

        assertEquals(3, recipeIDs.size());
        assertEquals("id 1", recipeIDs.get(0));
        assertEquals("id 2", recipeIDs.get(1));
        assertEquals("id 3", recipeIDs.get(2));
    }

    /**
     * Tests that the RecipeList returns recipes by ID.
     */
    @Test
    void testGetRecipeByID() {
        Recipe recipe1 = new Recipe("id 1", "Test Recipe", "Test Instructions", new Date());
        Recipe recipe2 = new Recipe("id 2", "Test Recipe 2", "Test Instructions 2", new Date());
        Recipe recipe3 = new Recipe("id 3", "Test Recipe 3", "Test Instructions 3", new Date());

        recipeList.getRecipes().add(recipe1);
        recipeList.getRecipes().add(recipe2);
        recipeList.getRecipes().add(recipe3);

        assertEquals(recipe1, recipeList.getRecipeByID("id 1"));
        assertEquals(recipe2, recipeList.getRecipeByID("id 2"));
        assertEquals(recipe3, recipeList.getRecipeByID("id 3"));
    }

    /**
     * Tests that the RecipeList is sorted in order of newest recipe to oldest
     * recipe.
     */
    @Test
    void testSortRecipesByDate() {
        long currentTime = System.currentTimeMillis();
        Recipe recipe1 = new Recipe("", "Test Recipe", "Test Instructions", new Date(currentTime - 1000));
        Recipe recipe2 = new Recipe("", "Test Recipe 2", "Test Instructions 2", new Date(currentTime));
        Recipe recipe3 = new Recipe("", "Test Recipe 3", "Test Instructions 3", new Date(currentTime + 1000));

        recipeList.getRecipes().add(recipe1);
        recipeList.getRecipes().add(recipe2);
        recipeList.getRecipes().add(recipe3);

        recipeList.sortRecipesByDate();

        assertEquals(3, recipeList.getRecipes().size());
        assertEquals(recipe3, recipeList.getRecipes().get(0));
        assertEquals(recipe2, recipeList.getRecipes().get(1));
        assertEquals(recipe1, recipeList.getRecipes().get(2));
    }

    /**
     * Based on Story 5 Scenario 1
     * Also tests Features 4 and 5 in the MS1 delivery document
     * 
     * @throws IOException
     */
    @Test
    void testUpdateDatabase() throws IOException {
        long currentTime = System.currentTimeMillis();
        Recipe recipe1 = new Recipe("id 1", "Test Recipe", "Test Instructions", new Date(currentTime - 1000)); // oldest
        Recipe recipe2 = new Recipe("id 2 ", "Test Recipe 2", "Test Instructions 2", new Date(currentTime)); // middle
        Recipe recipe3 = new Recipe("id 3", "Test Recipe 3", "Test Instructions 3", new Date(currentTime + 1000)); // newest

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
        assertEquals("id 1", jsonRecipe1.getString("recipeID"));
        assertEquals("Test Recipe", jsonRecipe1.getString("title"));
        assertEquals("Test Instructions 2", jsonRecipe2.getString("instructions"));

        String dateCreated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date(currentTime + 1000));

        assertEquals(dateCreated, jsonRecipe3.getString("dateCreated"));
    }

    /**
     * Tests that the RecipeList properly loads recipes from the database file.
     * 
     * @throws IOException
     */
    @Test
    void testLoadRecipesFromFile() throws IOException {
        FileWriter fw = new FileWriter(databaseFile);
        fw.write(
                "[{\"instructions\":\"Test Instructions\",\"dateCreated\":\"2023-11-11T00:55:14-08:00\",\"title\":\"Test Recipe\",\"recipeID\":\"id 1\"},{\"instructions\":\"Test Instructions 2\",\"dateCreated\":\"2023-11-11T00:55:15-08:00\",\"title\":\"Test Recipe 2\",\"recipeID\":\"id 2 \"},{\"instructions\":\"Test Instructions 3\",\"dateCreated\":\"2023-11-11T00:55:16-08:00\",\"title\":\"Test Recipe 3\",\"recipeID\":\"id 3\"}]");
        fw.flush();
        fw.close();
        recipeList.loadRecipesFromFile();
        assertEquals(3, recipeList.getRecipes().size());
        assertEquals("Test Recipe", recipeList.getRecipes().get(0).getTitle());
        assertEquals("Test Instructions 2", recipeList.getRecipes().get(1).getInstructions());
        String dateString = recipeList.getRecipes().get(2).getDateCreated().toString();

        // depends on the timezone you run the code from
        assertTrue(
                dateString.equals("Sat Nov 11 00:55:16 PST 2023") || dateString.equals("Sat Nov 11 08:55:16 UTC 2023"));
    }

    /**
     * Tests that the RecipeList properly loads recipes from the database file when
     * the file does not exist.
     * 
     * @throws IOException
     */
    @Test
    void testLoadRecipesFromFileTwo() throws IOException {
        recipeList.loadRecipesFromFile();
        assertEquals(0, recipeList.getRecipes().size());
    }

    /**
     * Tests that the RecipeList properly loads recipes from the database file when
     * the file is empty.
     * 
     * Based on Story 5 BDD Scenario 1
     */
    @Test
    void testSaveRecipeStoryScenarioOne() {
        Date date = new Date();
        Recipe recipe = new Recipe("", "Test title", "Test instructions", date);
        recipeList.getRecipes().add(recipe);
        recipeList.updateDatabase();
        assertEquals(1, recipeList.getRecipes().size());
        assertEquals(recipe, recipeList.getRecipes().get(0));
        recipeList = new RecipeList(databaseFile);
        assertEquals(1, recipeList.getRecipes().size());
        assertEquals("Test title", recipeList.getRecipes().get(0).getTitle());
        assertEquals("Test instructions", recipeList.getRecipes().get(0).getInstructions());
        assertEquals(date.toString(), recipeList.getRecipes().get(0).getDateCreated().toString());
    }

    /**
     * Tests that the RecipeList properly loads recipes from the database file.
     * Based on Story 5 BDD Scenario 2
     */
    @Test
    void testRemoveRecipe() {
        Recipe recipe1 = new Recipe("", "Test Recipe", "Test Instructions", new Date());
        Recipe recipe2 = new Recipe("", "Test Recipe 2", "Test Instructions 2", new Date());

        recipeList.getRecipes().add(recipe1);
        recipeList.getRecipes().add(recipe2);

        recipeList.removeRecipe(recipe2);

        assertEquals(1, recipeList.getRecipes().size());
        assertFalse(recipeList.getRecipes().contains(recipe2));
    }
}
