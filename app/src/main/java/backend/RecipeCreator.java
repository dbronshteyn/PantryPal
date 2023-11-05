package backend;

import java.io.File;
import java.io.IOException;

import java.util.Date;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;


public class RecipeCreator {

    private final int MAX_TOKENS = 300;
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
        List<String> responseLines = Arrays.asList(response.split("Title:")[1].split("\n"));
        String recipeTitle = responseLines.get(0).strip();
        String recipeBody = String.join("\n", responseLines.subList(1, responseLines.size())).strip();
        return new Recipe(recipeTitle, recipeBody, new Date());
    }
}
