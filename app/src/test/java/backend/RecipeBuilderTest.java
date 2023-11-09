package backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import backend.RecipeBuilder;

import java.io.File;
import java.io.IOException;


class RecipeBuilderTest {

    private RecipeBuilder recipeBuilder;

    class ChatGPTMock extends ChatGPT {

        public ChatGPTMock() {
            super("");
        }

        @Override
        public String generateText(String prompt, int maxTokens) {
            assertEquals("Please provide a recipe with a title denoted with \"Title:\", a new line, and then a detailed recipe. Create a breakfast recipe with the following ingredients: Ingredient 1 and ingredient 2", prompt);
            assertTrue(maxTokens > 0);
            return "Title: Test Title\n\nIngredient 1 and ingredient 2";
        }
    }

    class WhisperMock extends Whisper {

        public WhisperMock() {
            super("");
        }

        @Override
        public String transcribeAudio(File audioFile) throws IOException {
            assertNotNull(audioFile);
            if (audioFile.getName().equals("throw-exception.wav")) {
                throw new IOException();
            }
            if (audioFile.getName().equals("breakfast-meal-type.wav")) {
                return "BREAKFAST";
            }
            if (audioFile.getName().equals("invalid-meal-type.wav")) {
                return "Brunch";
            }
            if (audioFile.getName().equals("ingredients.wav")) {
                return "Ingredient 1 and ingredient 2";
            }
            fail();
            return "";
        }
    }

    @BeforeEach
    public void setUp() {
        recipeBuilder = new RecipeBuilder(new ChatGPTMock(), new WhisperMock());
    }

    @Test
    void testIsCompleted() {
        assertFalse(recipeBuilder.isCompleted());
        recipeBuilder.getIngredientsElement().setValue("Ingredient 1 and ingredient 2");
        assertFalse(recipeBuilder.isCompleted());
        recipeBuilder.getMealTypeElement().setValue("breakfast");
        assertTrue(recipeBuilder.isCompleted());
    }

    @Test
    void testSpecifyOne() throws IOException {
        assertEquals("breakfast", recipeBuilder.getMealTypeElement().specify(new File("breakfast-meal-type.wav")));
        assertEquals("breakfast", recipeBuilder.getMealTypeElement().getValue());
    }

    @Test
    void testSpecifyTwo() {
        try {
            recipeBuilder.getMealTypeElement().specify(new File("throw-exception.wav"));
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testSpecifyThree() throws IOException {
        assertNull(recipeBuilder.getMealTypeElement().specify(new File("invalid-meal-type.wav")));
        assertFalse(recipeBuilder.getMealTypeElement().isSet());
    }

    @Test
    void testSpecifyFour() throws IOException {
        assertEquals("Ingredient 1 and ingredient 2", recipeBuilder.getIngredientsElement().specify(new File("ingredients.wav")));
        assertEquals("Ingredient 1 and ingredient 2", recipeBuilder.getIngredientsElement().getValue());
    }

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
    @Test
    void testCreateRecipeStoryScenarioOne() throws IOException {
        ///
    }

    @Test
    void testCreateRecipeStoryScenarioTwo() throws IOException {
        ///
    }

    // create integration tests for all stories
}