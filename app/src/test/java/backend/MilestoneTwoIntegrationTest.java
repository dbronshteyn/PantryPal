package backend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;


class MilestoneTwoIntegrationTest {

    private ChatGPTMock chatGPTMock;
    private WhisperMock whisperMock;
    private DallEMock dallEMock;
    private RecipeBuilder recipeBuilder;
    private RecipeList recipeList;
    private AccountList accountList;
    private File recipeDatabaseFile;
    private File accountDatabaseFile;

    @BeforeEach
    public void setUp() {
        this.chatGPTMock = new ChatGPTMock();
        this.whisperMock = new WhisperMock();
        this.dallEMock = new DallEMock();
        recipeDatabaseFile = new File("test-recipe-database.json");
        if (recipeDatabaseFile.exists()) {
            recipeDatabaseFile.delete();
        }
        accountDatabaseFile = new File("test-account-database.json");
        if (accountDatabaseFile.exists()) {
            accountDatabaseFile.delete();
        }
        accountList = new AccountList(accountDatabaseFile);
        recipeList = new RecipeList(recipeDatabaseFile);
    }

    @AfterEach
    public void tearDown() {
        if (recipeDatabaseFile.exists()) {
            recipeDatabaseFile.delete();
        }
        if (accountDatabaseFile.exists()) {
            accountDatabaseFile.delete();
        }
    }

    @Test
    void testCaitlinStartsUsingPantryPalTwo() throws IOException, InterruptedException, URISyntaxException {
        assertTrue(accountList.addAccount("Caitlin", "password123"));
        assertEquals(0, recipeList.getRecipeIDs("Caitlin", "most-recent", "all").size());
        assertTrue(accountList.attemptLogin("Caitlin", "password123"));
        assertEquals(0, recipeList.getRecipeIDs("Caitlin", "most-recent", "all").size());

        RecipeBuilder builder = new RecipeBuilder(chatGPTMock, whisperMock, dallEMock);
        whisperMock.setMockScenario("breakfast.wav", "breakfast");
        builder.getMealTypeElement().specify(new File("breakfast.wav"));
        whisperMock.setMockScenario("eggs-and-bacon.wav", "Eggs and bacon");
        builder.getIngredientsElement().specify(new File("eggs-and-bacon.wav"));
        chatGPTMock.setMockScenario("Please provide a recipe with a title denoted with \"Title:\", a new line, and then a detailed recipe. Create a breakfast recipe with the following ingredients: Eggs and bacon", "Title: Eggs and bacon\nFry bacon and add eggs");
        dallEMock.setMockScenario("Eggs and bacon", "hex of eggs and bacon");
        Recipe recipe = builder.returnRecipe("Caitlin");
        assertEquals("hex of eggs and bacon", recipe.getImageHex());
        recipeList.addRecipe(recipe);
    }

    /*
     * Integration test for Iteration 1 scenario-based system test
     * entitled "Our own test scenario"
     * 
     * Covers user stories 1, 2, 3
     */
    @Test
    void testOurOwnTestScenario() throws IOException, InterruptedException, URISyntaxException {
        // user story 2 scenario 1
        assertTrue(accountList.addAccount("Caitlin", "password123"));
        assertEquals(0, recipeList.getRecipeIDs("Caitlin", "most-recent", "all").size());

        // user story 2 scenario 3
        assertFalse(accountList.attemptLogin("Caitlin", "chefcaitlin"));

        // user story 1 scenario 3
        assertFalse(accountList.addAccount("Caitlin", "chefcaitlin123"));

        // user story 2 scenario 2
        assertTrue(accountList.attemptLogin("Caitlin", "password123"));
        RecipeBuilder builder = new RecipeBuilder(chatGPTMock, whisperMock, dallEMock);
        whisperMock.setMockScenario("dinner.wav", "dinner");
        builder.getMealTypeElement().specify(new File("dinner.wav"));
        whisperMock.setMockScenario("salmon-and-rice.wav", "salmon and rice");
        builder.getIngredientsElement().specify(new File("salmon-and-rice.wav"));
        chatGPTMock.setMockScenario("Please provide a recipe with a title denoted with \"Title:\", a new line, and then a detailed recipe. Create a dinner recipe with the following ingredients: salmon and rice", "Title: Salmon and rice\nCook salmon and rice");
        dallEMock.setMockScenario("Salmon and rice", "hex of salmon and rice");
        Recipe recipe = builder.returnRecipe("Caitlin");
        assertEquals("hex of salmon and rice", recipe.getImageHex());
        recipeList.addRecipe(recipe);

        // user story 3 scenario 2
        assertEquals(1, recipeList.getRecipeIDs("Caitlin", "most-recent", "all").size());
        assertEquals("hex of salmon and rice", recipeList.getRecipeByID(recipeList.getRecipeIDs("Caitlin", "most-recent", "all").get(0)).getImageHex());
    }
}