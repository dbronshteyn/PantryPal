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

    @AfterEach
    public void tearDown() {
        if (databaseFile.exists()) {
            databaseFile.delete();
        }
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

    /*
     * Integration tests
     */
    // based on Story 5 Scenario 1
    // also tests Features 4 and 5 in the MS1 delivery document
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

    @Test
    void testLoadRecipesFromFile() throws IOException {
        FileWriter fw = new FileWriter(databaseFile);
        fw.write("[{\"instructions\":\"Test Instructions\",\"dateCreated\":\"2023-11-09T21:12:01-08:00\",\"title\":\"Test Recipe\"},{\"instructions\":\"Test Instructions 2\",\"dateCreated\":\"2023-11-09T21:12:02-08:00\",\"title\":\"Test Recipe 2\"},{\"instructions\":\"Test Instructions 3\",\"dateCreated\":\"2023-11-09T21:12:03-08:00\",\"title\":\"Test Recipe 3\"}]");
        fw.flush();
        fw.close();
        recipeList.loadRecipesFromFile();
        assertEquals(3, recipeList.getRecipes().size());
        assertEquals("Test Recipe", recipeList.getRecipes().get(0).getTitle());
        assertEquals("Test Instructions 2", recipeList.getRecipes().get(1).getInstructions());
        String dateString = recipeList.getRecipes().get(2).getDateCreated().toString();
        assertTrue(dateString.equals("Thu Nov 09 21:12:03 PST 2023") || dateString.equals("Fri Nov 10 05:12:03 UTC 2023"));
    }

    @Test
    void testLoadRecipesFromFileTwo() throws IOException {
        recipeList.loadRecipesFromFile();
        assertEquals(0, recipeList.getRecipes().size());
    }

    // based on Story 5 BDD Scenario 1
    @Test
    void testSaveRecipeStoryScenarioOne() {
        Date date = new Date();
        Recipe recipe = new Recipe("Test title", "Test instructions", date);
        recipeList.addRecipe(recipe);
        recipeList.updateDatabase();
        assertEquals(1, recipeList.getRecipes().size());
        assertEquals(recipe, recipeList.getRecipes().get(0));
        recipeList = new RecipeList(databaseFile);
        assertEquals(1, recipeList.getRecipes().size());
        assertEquals("Test title", recipeList.getRecipes().get(0).getTitle());
        assertEquals("Test instructions", recipeList.getRecipes().get(0).getInstructions());
        assertEquals(date.toString(), recipeList.getRecipes().get(0).getDateCreated().toString());
    }

    // tests Feature 7 in the MS1 delivery document
    @Test
    void testRemoveRecipe() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Instructions", new Date());
        Recipe recipe2 = new Recipe("Test Recipe 2", "Test Instructions 2", new Date());

        recipeList.addRecipe(recipe1);
        recipeList.addRecipe(recipe2);

        recipeList.removeRecipe(recipe2);

        assertEquals(1, recipeList.getRecipes().size());
        assertFalse(recipeList.getRecipes().contains(recipe2));
    }
}
