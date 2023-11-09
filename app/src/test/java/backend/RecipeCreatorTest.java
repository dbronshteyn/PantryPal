// package backend;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.fail;

// import backend.RecipeCreator;

// import java.io.File;
// import java.io.IOException;


// class RecipeCreatorTest {

//     private RecipeCreator recipeCreator;

//     private static final String API_KEY = "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";

//     class ChatGPTMock extends ChatGPT {

//         public ChatGPTMock() {
//             super("");
//         }

//         @Override
//         public String generateText(String prompt, int maxTokens) {
//             assertEquals("Please provide a recipe with a title denoted with \"Title:\", a new line, and then a detailed recipe. Create a recipe with the following ingredients: Ingredient 1 and ingredient 2", prompt);
//             assertNotNull(prompt);
//             assertTrue(maxTokens > 0);
//             return "Title: Test Title\n\nRecipe Body";
//         }
//     }

//     class WhisperMock extends Whisper {

//         public WhisperMock() {
//             super("");
//         }

//         @Override
//         public String transcribeAudio(File audioFile) throws IOException {
//             assertNotNull(audioFile);
//             if (!audioFile.getName().equals("ingredients.wav")) {
//                 throw new IOException("Invalid file");
//             }
//             return "Ingredient 1 and ingredient 2";
//         }
//     }

//     // @BeforeEach
//     // public void setUp() {
//     //     recipeCreator = new RecipeCreator(new ChatGPTMock(), new WhisperMock());
//     // }

//     // @Test
//     // void testCreateRecipe() {
//     //     try {
//     //         Recipe recipe = recipeCreator.createRecipe(new File("ingredients.wav"));
//     //         assertNotNull(recipe);
//     //         assertEquals(recipe.getTitle(), "Test Title");
//     //         assertEquals(recipe.getInstructions(), "Recipe Body");
//     //     } catch (Exception e) {
//     //         fail();
//     //     }
//     // }

    
//     // @Test
//     // void testCreateRecipeWithNullFile() {
//     //     assertThrows(Throwable.class, () -> {
//     //         recipeCreator.createRecipe(null);
//     //     });
//     // }

//     // @Test
//     // void testCreateRecipeWithInvalidFile() {
//     //     assertThrows(IOException.class, () -> {
//     //         recipeCreator.createRecipe(new File("nonexistent.wav"));
//     //     });
//     // }

//     /*
//      * Integration test for recipe creation feature
//      */
//     // @Test
//     // void testCreateRecipeStory() throws IOException {
//     //     ChatGPT chatGPT = new ChatGPT(API_KEY);
//     //     Whisper whisper = new Whisper(API_KEY);
//     //     RecipeCreator recipeCreator = new RecipeCreator(chatGPT, whisper);
//     //     Recipe recipe = recipeCreator.createRecipe(new File("../recipe-creator-story-test.wav"));
//     //     assertNotNull(recipe);
//     //     assertNotNull(recipe.getTitle()); // chatGPT isn't determintic, so we can't test for a specific title
//     //     assertTrue(recipe.getTitle().length() > 5);
//     //     assertNotNull(recipe.getInstructions());
//     //     assertTrue(recipe.getInstructions().length() > 100); // make sure recipe body is reasonably long
//     //     assertNotNull(recipe.getDateCreated());
//     // }
// }