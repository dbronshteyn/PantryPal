package backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.scene.image.Image;

import java.util.Base64;

/**
 * Represents a recipe with a unique ID, title, instructions, and creation date.
 */
public class Recipe {

    private String recipeID;
    private String title;
    private String instructions;
    private Date dateCreated;
    private String accountUsername;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    private String imageHex;
    private String mealType;

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
    public Recipe(String recipeID, String title, String instructions, Date dateCreated, String accountUsername,
            String imageHex, String mealType) {
        this.recipeID = recipeID;
        this.title = title;
        this.instructions = instructions;
        this.dateCreated = dateCreated;
        this.imageHex = imageHex;
        this.accountUsername = accountUsername;
        this.mealType = mealType;
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
        this.imageHex = jsonRecipe.getString("imageHex");
        this.mealType = jsonRecipe.getString("mealType");
        this.accountUsername = jsonRecipe.getString("accountUsername");
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

    public String getAccountUsername() {
        return this.accountUsername;
    }

    public String getMealType() {
        return this.mealType;
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

    public String getImageHex() {
        return this.imageHex;
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
        out.put("imageHex", this.imageHex);
        out.put("mealType", this.mealType);
        out.put("dateCreated", this.formatter.format(this.dateCreated));
        out.put("accountUsername", this.accountUsername);
        return out;
    }

    public String toHTML() throws IOException {

        // File store = new File("store.png");

        // HexUtils.hexToFile(this.imageHex, store);

        // byte[] fileBytes = Files.readAllBytes(Paths.get(store.getAbsolutePath()));
        // String base64String = Base64.getEncoder().encodeToString(fileBytes);

        // // Write base64String to test.txt
        // PrintWriter out = new PrintWriter("test.txt");
        // out.println(base64String);
        // out.close();

        // Temp, should not be hex
        String base64String = this.imageHex;

        String htmlInstructions = escapeHTML(this.instructions).replace("\n", "<br>");
        String imageTag = "<img src=\"data:image/png;base64," + base64String + "\" alt=\"Recipe Image\">";

        return "<html><body style=\"background-color: #e7ffe6;\"><h1>" + this.title + "</h1>" + imageTag + "<p>"
                + htmlInstructions
                + "</p></body></html>";
    }

    // taken from https://stackoverflow.com/a/25228492
    private static String escapeHTML(String s) {
        StringBuilder out = new StringBuilder(Math.max(16, s.length()));
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c > 127 || c == '"' || c == '\'' || c == '<' || c == '>' || c == '&') {
                out.append("&#");
                out.append((int) c);
                out.append(';');
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }
}