package backend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;

/**
 * Represents a recipe with a unique ID, title, instructions, and creation date.
 */
public class Recipe {

    private String recipeID;
    private String title;
    private String instructions;
    private Date dateCreated;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    private String imageURL;

    /**
     * Constructs a new Recipe with the provided ID, title, instructions, and
     * creation
     * date.
     * 
     * @param recipeID     Unique ID of the recipe.
     * @param title        Title of the recipe.
     * @param instructions Instructions for preparing the recipe.
     * @param dateCreated  Date when the recipe was generated.
     */
    public Recipe(String recipeID, String title, String instructions, Date dateCreated, String imageURL) {
        this.recipeID = recipeID;
        this.title = title;
        this.instructions = instructions;
        this.dateCreated = dateCreated;
        this.imageURL = imageURL;
    }

    /**
     * Constructs a new Recipe from a JSON object.
     * 
     * @param jsonRecipe JSON object representing the recipe.
     */
    public Recipe(JSONObject jsonRecipe) {
        String dateString = jsonRecipe.getString("dateCreated");
        try {
            this.dateCreated = this.formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            this.dateCreated = new Date();
        }
        this.title = jsonRecipe.getString("title");
        this.instructions = jsonRecipe.getString("instructions");
        this.recipeID = jsonRecipe.getString("recipeID");

        this.imageURL = jsonRecipe.getString("imageURL");
    }

    /**
     * Returns the title of the recipe.
     * 
     * @return Title of the recipe.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the instructions for preparing the recipe.
     * 
     * @return Instructions for preparing the recipe.
     */
    public String getInstructions() {
        return this.instructions;
    }

    /**
     * Returns the creation date of the recipe.
     * 
     * @return Creation date of the recipe.
     */
    public Date getDateCreated() {
        return this.dateCreated;
    }

    /**
     * Returns the unique ID of the recipe.
     * 
     * @return Unique ID of the recipe.
     */
    public String getRecipeID() {
        return this.recipeID;
    }

    /**
     * Sets the instructions for preparing the recipe and updates the creation date.
     * 
     * @param instructions New instructions for preparing the recipe.
     */
    public void setInstructions(String instructions) {
        this.dateCreated = new Date();
        this.instructions = instructions;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    /**
     * Returns a string representation of the recipe, or its title.
     * 
     * @return Title of the recipe.
     */
    @Override
    public String toString() {
        return this.title;
    }

    /**
     * Returns a JSON object representing the recipe.
     * 
     * @return JSON object representing the recipe.
     */
    public JSONObject toJSON() {
        JSONObject out = new JSONObject();
        out.put("recipeID", this.recipeID);
        out.put("title", this.title);
        out.put("instructions", this.instructions);
        out.put("imageURL", this.imageURL); // I deleted the imagetoHex because I am currently stuck on retrieving the
                                            // hex representation from the server. Reverting changes.
        out.put("dateCreated", this.formatter.format(this.dateCreated));
        return out;
    }
}