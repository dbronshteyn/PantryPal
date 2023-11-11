package backend;

import java.io.File;
import java.io.IOException;

import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class RecipeBuilder {

    public class ResettableElement {
        private String value;
        private String[] allowedValues;

        public ResettableElement(String[] allowedValues) {
            this.value = null;
            this.allowedValues = allowedValues;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void reset() {
            this.value = null;
        }

        public boolean isSet() {
            return this.value != null;
        }

        public String specify(File audioFile) throws IOException {
            String transcribedText = whisper.transcribeAudio(audioFile);
            if (this.allowedValues != null) {
                for (String allowedValue : this.allowedValues) {
                    if (transcribedText.toLowerCase().contains(allowedValue)) {
                        this.value = allowedValue;
                        return this.value;
                    }
                }
                return null;
            }
            this.value = transcribedText;
            return this.value;
        }
    }

    static final String[] MEAL_TYPES = {"breakfast", "lunch", "dinner"};
    static final int MAX_TOKENS = 300;
    static final String PROMPT = "Please provide a recipe with a title denoted with \"Title:\", a new line, and then a detailed recipe. Create a %s recipe with the following ingredients: %s";

    private ChatGPT chatGPT;
    private Whisper whisper;

    private ResettableElement mealType;
    private ResettableElement ingredients;

    private String recipeID;


    public RecipeBuilder(ChatGPT chatGPT, Whisper whisper) {
        this.chatGPT = chatGPT;
        this.whisper = whisper;
        this.mealType = new ResettableElement(MEAL_TYPES);
        this.ingredients = new ResettableElement(null);
        this.recipeID = UUID.randomUUID().toString();
    }

    public boolean isCompleted() {
        return this.mealType.isSet() && this.ingredients.isSet();
    }

    public Recipe returnRecipe() throws IOException {
        String prompt = String.format(PROMPT, this.mealType.getValue(), this.ingredients.getValue());
        String response = this.chatGPT.generateText(prompt, MAX_TOKENS);
        List<String> responseLines = Arrays.asList(response.split("Title:")[1].split("\n"));
        String recipeTitle = responseLines.get(0).strip();
        String recipeBody = String.join("\n", responseLines.subList(1, responseLines.size())).strip();
        return new Recipe(this.recipeID, recipeTitle, recipeBody, new Date());
    }

    public ResettableElement getMealTypeElement() {
        return this.mealType;
    }

    public ResettableElement getIngredientsElement() {
        return this.ingredients;
    }

    public String getRecipeID() {
        return this.recipeID;
    }
}
