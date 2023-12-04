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
}
