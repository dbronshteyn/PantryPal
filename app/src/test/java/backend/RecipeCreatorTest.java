package backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import backend.RecipeCreator;

import java.io.File;
import java.io.IOException;


public class RecipeCreatorTest {

    private RecipeCreator recipeCreator;

    class ChatGPTMock extends ChatGPT {

        public ChatGPTMock() {
            super("");
        }

        @Override
        public String generateText(String prompt, int maxTokens) {
            assertEquals("Please provide a recipe with a title denoted with \"Title:\", a new line, and then a detailed recipe. Create a recipe with the following ingredients: Ingredient 1 and ingredient 2", prompt);
            assertNotNull(prompt);
            assertTrue(maxTokens > 0);
            return "Title: Test Title\n\nRecipe Body";
        }
    }

    class WhisperMock extends Whisper {

        public WhisperMock() {
            super("");
        }

        @Override
        public String transcribeAudio(File audioFile) throws IOException {
            assertNotNull(audioFile);
            if (!audioFile.getName().equals("ingredients.wav")) {
                throw new IOException("Invalid file");
            }
            return "Ingredient 1 and ingredient 2";
        }
    }

    @BeforeEach
    public void setUp() {
        recipeCreator = new RecipeCreator(new ChatGPTMock(), new WhisperMock());
    }

    @Test
    void testCreateRecipe() {
        try {
            Recipe recipe = recipeCreator.createRecipe(new File("ingredients.wav"));
            assertNotNull(recipe);
            assertEquals(recipe.getTitle(), "Test Title");
            assertEquals(recipe.getInstructions(), "Recipe Body");
        } catch (Exception e) {
            fail();
        }
    }

    
    @Test
    void testCreateRecipeWithNullFile() {
        assertThrows(Throwable.class, () -> {
            recipeCreator.createRecipe(null);
        });
    }

    @Test
    void testCreateRecipeWithInvalidFile() {
        assertThrows(IOException.class, () -> {
            recipeCreator.createRecipe(new File("nonexistent.wav"));
        });
    }

}