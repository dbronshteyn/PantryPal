package backend;

import java.io.File;
import java.io.IOException;

import java.util.Date;

import org.json.JSONException;


public class RecipeCreator {

    private final int MAX_TOKENS = 100;
    private final String PROMPT = "Please provide a recipe with a title denoted with \"Title:\", a new line, and then a detailed recipe. Create a recipe with the following ingredients: %s";
    private static final String API_KEY = "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";

    private ChatGPT chatGPT;
    private Whisper whisper;

    public RecipeCreator() {
        this.chatGPT = new ChatGPT(API_KEY);
        this.whisper = new Whisper(API_KEY);
    }

    public Recipe createRecipe(File ingredientsAudioFile) throws IOException {
        String ingredients = this.whisper.transcribeAudio(ingredientsAudioFile);
        String prompt = String.format(PROMPT, ingredients);
        String response = this.chatGPT.generateText(prompt, MAX_TOKENS);
        String recipeTitle = response.split("Title:")[1].split("\n")[0];
        String recipeBody = response.split("Title:")[1].split("\n")[1];
        return new Recipe(recipeTitle, recipeBody, new Date());
    }
}
