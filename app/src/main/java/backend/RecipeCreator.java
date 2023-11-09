package backend;

import java.io.File;
import java.io.IOException;

import java.util.Date;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;

public class RecipeCreator {

    private final int MAX_TOKENS = 300;
    private final String PROMPT = "Please provide a recipe with a title denoted with \"Title:\", a new line, and then a detailed recipe. Create a %s recipe with the following ingredients: %s";

    private ChatGPT chatGPT;

    public RecipeCreator(ChatGPT chatGPT) {
        this.chatGPT = chatGPT;
    }

    public Recipe createRecipe(String mealType, String ingredients) throws IOException {
        String prompt = String.format(PROMPT, mealType, ingredients);
        String response = this.chatGPT.generateText(prompt, MAX_TOKENS);
        List<String> responseLines = Arrays.asList(response.split("Title:")[1].split("\n"));
        String recipeTitle = responseLines.get(0).strip();
        String recipeBody = String.join("\n", responseLines.subList(1, responseLines.size())).strip();
        return new Recipe(recipeTitle, recipeBody, new Date());
    }
}
