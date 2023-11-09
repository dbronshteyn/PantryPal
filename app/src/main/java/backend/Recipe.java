package backend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

/**
 * The Recipe class represents a recipe with a title, instructions, and the date it was created on.
 * It includes methods that can access and modify these fields, and employs a 'toString' method to display the recipe's title.
 */

public class Recipe {
    private String title;
    private String instructions;
    private Date dateCreated;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");


    /**
     * Constructs a new Recipe with the provided title, instructions, and creation date.
     * @param title             Title of the recipe.
     * @param instructions      Instructions for preparing the recipe.
     * @param dateCreated       Date when the recipe was generated.
     */

    public Recipe(String title, String instructions, Date dateCreated) {
        this.title = title;
        this.instructions = instructions;
        this.dateCreated = dateCreated;
    }

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
    }

    public String getTitle() {
        return this.title;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setInstructions(String instructions) {
        this.dateCreated = new Date();
        this.instructions = instructions;
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

    public JSONObject toJSON() {
        JSONObject out = new JSONObject();
        out.put("title", this.title);
        out.put("instructions", this.instructions);
        out.put("dateCreated", this.formatter.format(this.dateCreated));
        return out;
    }
}