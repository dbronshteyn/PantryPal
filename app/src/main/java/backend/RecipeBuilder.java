package backend;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * This class represents a Recipe Builder that allows the user to create a
 * recipe with a title and a detailed recipe.
 * The Recipe Builder has a meal type and ingredients that can be specified by
 * the user. The Recipe Builder generates a recipe
 * using the specified meal type and ingredients.
 */
public class RecipeBuilder {

    /**
     * This class represents a resettable element that can be set to a value, reset
     * to null, and checked if it is set.
     * The resettable element can also specify a value from an audio file if the
     * value is one of the allowed values.
     */
    public class ResettableElement {

        /**
         * The current value of the resettable element.
         */
        private String value;

        /**
         * The allowed values for the resettable element.
         */
        private String[] allowedValues;

        /**
         * Constructs a resettable element with the specified allowed values.
         * 
         * @param allowedValues the allowed values for the resettable element
         */
        public ResettableElement(String[] allowedValues) {
            this.value = null;
            this.allowedValues = allowedValues;
        }

        /**
         * Returns the current value of the resettable element.
         * 
         * @return the current value of the resettable element
         */
        public String getValue() {
            return this.value;
        }

        /**
         * Sets the value of the resettable element to the specified value.
         * 
         * @param value the value to set the resettable element to
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Resets the value of the resettable element to null.
         */
        public void reset() {
            this.value = null;
        }

        /**
         * Returns true if the resettable element is set to a value, false otherwise.
         * 
         * @return true if the resettable element is set to a value, false otherwise
         */
        public boolean isSet() {
            return this.value != null;
        }

        /**
         * Specifies the value of the resettable element from the specified audio file
         * if the value is one of the allowed values.
         * 
         * @param audioFile the audio file to transcribe and specify the value from
         * @return the specified value if it is one of the allowed values, null
         *         otherwise
         * @throws IOException if there is an error transcribing the audio file
         */
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

    // allowed meal types
    static final String[] MEAL_TYPES = { "breakfast", "lunch", "dinner" };

    // max tokens for ChatGPT response
    static final int MAX_TOKENS = 300;

    // prompt for ChatGPT
    static final String PROMPT = "Please provide a recipe with a title denoted with \"Title:\", a new line, and then a detailed recipe. Create a %s recipe with the following ingredients: %s";

    private ChatGPT chatGPT;
    private Whisper whisper;
    private ResettableElement mealType;
    private ResettableElement ingredients;
    private String recipeID;

    /**
     * Constructs a Recipe Builder with the specified ChatGPT and Whisper.
     * Initializes the meal type and ingredients resettable elements and generates a
     * recipe ID.
     * 
     * @param chatGPT the ChatGPT used to generate the recipe
     * @param whisper the Whisper used to transcribe audio files
     */
    public RecipeBuilder(ChatGPT chatGPT, Whisper whisper) {
        this.chatGPT = chatGPT;
        this.whisper = whisper;
        this.mealType = new ResettableElement(MEAL_TYPES);
        this.ingredients = new ResettableElement(null);
        this.recipeID = UUID.randomUUID().toString();
    }

    /**
     * Returns true if the recipe is ready to be generated, false otherwise
     * 
     * @return true if the meal type and ingredients resettable elements are set to
     *         values, false otherwise
     */
    public boolean isCompleted() {
        return this.mealType.isSet() && this.ingredients.isSet();
    }

    /**
     * Returns the produced Recipe object
     * 
     * @return the generated recipe
     * @throws IOException if there is an error generating the recipe
     */
    public Recipe returnRecipe() throws IOException {
        String prompt = String.format(PROMPT, this.mealType.getValue(), this.ingredients.getValue());
        String response = this.chatGPT.generateText(prompt, MAX_TOKENS);
        List<String> responseLines = Arrays.asList(response.split("Title:")[1].split("\n"));
        String recipeTitle = responseLines.get(0).strip();
        String recipeBody = String.join("\n", responseLines.subList(1, responseLines.size())).strip();
        return new Recipe(this.recipeID, recipeTitle, recipeBody, new Date());
    }

    /**
     * Returns the meal type resettable element.
     * 
     * @return the meal type resettable element
     */
    public ResettableElement getMealTypeElement() {
        return this.mealType;
    }

    /**
     * Returns the ingredients resettable element.
     * 
     * @return the ingredients resettable element
     */
    public ResettableElement getIngredientsElement() {
        return this.ingredients;
    }

    /**
     * Returns the ID of the recipe.
     * 
     * @return the ID of the recipe
     */
    public String getRecipeID() {
        return this.recipeID;
    }
}
