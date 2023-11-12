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
 * JUnit testing for RecipeBuilder class
 */
class RecipeBuilderTest {

    private RecipeBuilder recipeBuilder;

    /**
     * Mock class for ChatGPT
     */
    class ChatGPTMock extends ChatGPT {

        public ChatGPTMock() {
            super("");
        }

        /**
         * Generates mock text.
         */
        @Override
        public String generateText(String prompt, int maxTokens) {
            assertTrue(maxTokens > 0);
            if (prompt.equals(
                    "Please provide a recipe with a title denoted with \"Title:\", a new line, and then a detailed recipe. Create a breakfast recipe with the following ingredients: Ingredient 1 and ingredient 2")) {
                return "Title: Test Title\n\nIngredient 1 and ingredient 2";
            }
            if (prompt.equals(
                    "Please provide a recipe with a title denoted with \"Title:\", a new line, and then a detailed recipe. Create a breakfast recipe with the following ingredients: I have eggs, cheese, and bread.")) {
                return "Title: Cheesy Egg Bread\n\n2 eggs, 3 cheese, 1 bread";
            }
            fail();
            return "";
        }
    }

    /**
     * Mock class for Whisper
     */
    class WhisperMock extends Whisper {

        public WhisperMock() {
            super("");
        }

        /**
         * Transcribes mock audio files.
         */
        @Override
        public String transcribeAudio(File audioFile) throws IOException {
            assertNotNull(audioFile);
            if (audioFile.getName().equals("throw-exception.wav")) {
                throw new IOException();
            }
            if (audioFile.getName().equals("breakfast-meal-type.wav")) {
                return "BREAKFAST";
            }
            if (audioFile.getName().equals("dinner-meal-type.wav")) {
                return "Dinner";
            }
            if (audioFile.getName().equals("invalid-meal-type.wav")) {
                return "Brunch";
            }
            if (audioFile.getName().equals("ingredients.wav")) {
                return "Ingredient 1 and ingredient 2";
            }
            if (audioFile.getName().equals("eggs-and-cheese.wav")) {
                return "I have eggs, cheese, and bread.";
            }
            fail();
            return "";
        }
    }

    /**
     * Sets up the RecipeBuilder for testing.
     */
    @BeforeEach
    public void setUp() {
        recipeBuilder = new RecipeBuilder(new ChatGPTMock(), new WhisperMock());
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
        recipeBuilder.getMealTypeElement().setValue("breakfast");
        recipeBuilder.getIngredientsElement().setValue("Ingredient 1 and ingredient 2");
        Recipe recipe = recipeBuilder.returnRecipe();
        assertNotNull(recipe);
        assertEquals("Test Title", recipe.getTitle());
        assertEquals("Ingredient 1 and ingredient 2", recipe.getInstructions());
    }

    /*
     * Integration test for recipe creation feature
     */
    // based on Story 2 BDD Scenario 1, Story 3 BDD Scenario 1
    // also tests Features 1 and 2 in the MS1 delivery document
    @Test
    void testCreateRecipeStoryScenarioOne() throws IOException {
        assertFalse(recipeBuilder.isCompleted());
        assertEquals("breakfast", recipeBuilder.getMealTypeElement().specify(new File("breakfast-meal-type.wav")));
        assertFalse(recipeBuilder.isCompleted());
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
        assertFalse(recipeBuilder.isCompleted());
        assertNull(recipeBuilder.getMealTypeElement().specify(new File("invalid-meal-type.wav")));
        assertFalse(recipeBuilder.isCompleted());
        assertFalse(recipeBuilder.getMealTypeElement().isSet());
    }
}