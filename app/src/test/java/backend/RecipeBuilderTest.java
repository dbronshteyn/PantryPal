package backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

/**
 * Unit testing for RecipeBuilder class
 */
class RecipeBuilderTest {

    private RecipeBuilder recipeBuilder;
    private ChatGPTMock chatGPTMock;
    private WhisperMock whisperMock;

    /**
     * Sets up the RecipeBuilder for testing.
     */
    @BeforeEach
    public void setUp() {
        this.chatGPTMock = new ChatGPTMock();
        this.whisperMock = new WhisperMock();
        recipeBuilder = new RecipeBuilder(this.chatGPTMock, this.whisperMock);
    }

    /**
     * Tests the isCompleted method.
     */
    @Test
    void testIsCompleted() {
        assertFalse(recipeBuilder.isCompleted());
        recipeBuilder.getIngredientsElement().setValue("Ingredient 1 and ingredient 2");
        assertFalse(recipeBuilder.isCompleted());
        recipeBuilder.getMealTypeElement().setValue("breakfast");
        assertTrue(recipeBuilder.isCompleted());
    }

    /**
     * Tests the getMealTypeElement method.
     * 
     * @throws IOException
     */
    @Test
    void testSpecifyOne() throws IOException {
        whisperMock.setMockScenario("breakfast-meal-type.wav", "BREAKFAST");
        assertEquals("breakfast", recipeBuilder.getMealTypeElement().specify(new File("breakfast-meal-type.wav")));
        assertEquals("breakfast", recipeBuilder.getMealTypeElement().getValue());
    }

    /**
     * Tests the getMealTypeElement method.
     */
    @Test
    void testSpecifyTwo() {
        try {
            recipeBuilder.getMealTypeElement().specify(new File("throw-exception.wav"));
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Tests the getMealTypeElement method.
     * 
     * @throws IOException
     */
    @Test
    void testSpecifyThree() throws IOException {
        whisperMock.setMockScenario("invalid-meal-type.wav", "Brunch");
        assertNull(recipeBuilder.getMealTypeElement().specify(new File("invalid-meal-type.wav")));
        assertFalse(recipeBuilder.getMealTypeElement().isSet());
    }

    /**
     * Tests the getIngredientsElement method.
     * 
     * @throws IOException
     */
    @Test
    void testSpecifyFour() throws IOException {
        whisperMock.setMockScenario("ingredients.wav", "Ingredient 1 and ingredient 2");
        assertEquals("Ingredient 1 and ingredient 2",
                recipeBuilder.getIngredientsElement().specify(new File("ingredients.wav")));
        assertEquals("Ingredient 1 and ingredient 2", recipeBuilder.getIngredientsElement().getValue());
    }

    /**
     * Tests the getIngredientsElement method.
     * 
     * @throws IOException
     */
    @Test
    void testReturnRecipe() throws IOException {
        chatGPTMock.setMockScenario("Please provide a recipe with a title denoted with \"Title:\", a new line, and then a detailed recipe. Create a breakfast recipe with the following ingredients: Ingredient 1 and ingredient 2", 
                "Title: Test Title\n\nIngredient 1 and ingredient 2");
        recipeBuilder.getMealTypeElement().setValue("breakfast");
        recipeBuilder.getIngredientsElement().setValue("Ingredient 1 and ingredient 2");
        Recipe recipe = recipeBuilder.returnRecipe();
        assertNotNull(recipe);
        assertEquals("Test Title", recipe.getTitle());
        assertEquals("Ingredient 1 and ingredient 2", recipe.getInstructions());
    }

    // based on Story 2 BDD Scenario 1, Story 3 BDD Scenario 1
    // also tests Features 1 and 2 in the MS1 delivery document
    @Test
    void testCreateRecipeStoryScenarioOne() throws IOException {
        whisperMock.setMockScenario("breakfast-meal-type.wav", "BREAKFAST");
        chatGPTMock.setMockScenario("Please provide a recipe with a title denoted with \"Title:\", a new line, and then a detailed recipe. Create a breakfast recipe with the following ingredients: I have eggs, cheese, and bread.", 
                "Title: Cheesy Egg Bread\n\n2 eggs, 3 cheese, 1 bread");
        assertFalse(recipeBuilder.isCompleted());
        assertEquals("breakfast", recipeBuilder.getMealTypeElement().specify(new File("breakfast-meal-type.wav")));
        assertFalse(recipeBuilder.isCompleted());

        whisperMock.setMockScenario("eggs-and-cheese.wav", "I have eggs, cheese, and bread.");
        assertEquals("I have eggs, cheese, and bread.",
                recipeBuilder.getIngredientsElement().specify(new File("eggs-and-cheese.wav")));
        assertTrue(recipeBuilder.isCompleted());
        Recipe recipe = recipeBuilder.returnRecipe();
        assertNotNull(recipe);
        assertEquals("Cheesy Egg Bread", recipe.getTitle());
        assertEquals("2 eggs, 3 cheese, 1 bread", recipe.getInstructions());
    }

    // based on Story 3 BDD Scenario 2
    @Test
    void testCreateRecipeStoryScenarioTwo() throws IOException {
        whisperMock.setMockScenario("invalid-meal-type.wav", "Brunch");
        assertFalse(recipeBuilder.isCompleted());
        assertNull(recipeBuilder.getMealTypeElement().specify(new File("invalid-meal-type.wav")));
        assertFalse(recipeBuilder.isCompleted());
        assertFalse(recipeBuilder.getMealTypeElement().isSet());
    }
}