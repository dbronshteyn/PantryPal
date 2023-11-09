package backend;

import java.util.Date;

/**
 * The Recipe class represents a recipe with a title, instructions, and the date it was created on.
 * It includes methods that can access and modify these fields, and employs a 'toString' method to display the recipe's title.
 */

public class Recipe {
    private String title;
    private String instructions;
    private Date dateCreated;
    public static final String[] MEAL_TYPES = {"breakfast", "lunch", "dinner"};


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

    public String getTitle() {
        return this.title;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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
}