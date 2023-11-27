package middleware;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class represents a controller that handles requests from the frontend
 * and
 * sends them to the backend.
 */
public class Controller {

    /**
     * Generates a new recipe builder and returns the ID of the recipe builder.
     * 
     * @return
     */
    public static String generateNewRecipeBuilder() {
        return sendRequest("/generate-new-recipe-builder", null, "GET");
    }

    /**
     * Returns the recipe title.
     * 
     * @param recipeID
     * @return the recipe title
     */
    public static String getRecipeTitle(String recipeID) {
        return sendRequest("/get-recipe-title", "recipeID=" + recipeID, "GET");
    }

    /**
     * Returns the detailed instructions for a given recipe.
     * 
     * @param recipeID
     * @return the instructions
     */
    public static String getRecipeInstructions(String recipeID) {
        try {
            return URLDecoder.decode(sendRequest("/get-recipe-instructions", "recipeID=" + recipeID, "GET"),
                    "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the image URL for a given recipe.
     * 
     * @param recipeID
     * @return the image URL
     */
    public static String getRecipeImageURL(String recipeID) {
        try {
            System.out.println("Controller worked");
            System.out.println(sendRequest("/get-recipe-image-url", "recipeID=" + recipeID, "GET"));
            return URLDecoder.decode(sendRequest("/get-recipe-image-url", "recipeID=" + recipeID, "GET"),
                    "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns all the recipe IDs.
     * 
     * @return list of recipe IDs
     */
    public static List<String> getRecipeIDs() {
        String response = sendRequest("/get-recipe-ids", null, "GET");
        if (response.equals(".")) {
            return new ArrayList<>();
        }
        return Arrays.asList(response.split(","));
    }

    /**
     * Resets an element in the recipe builder back to null.
     * 
     * @param recipeID
     * @param elementName
     */
    public static void resetRecipeCreatorElement(String recipeID, String elementName) {
        sendRequest("/reset-recipe-creator-element", "recipeID=" + recipeID + "&elementName=" + elementName,
                "PUT");
    }

    /**
     * Sets a recipe builder element by providing an audio file.
     * 
     * @param recipeID
     * @param elementName
     * @param audioFile
     * @return the new value of the element if it was set, otherwise null
     */
    public static String specifyRecipeCreatorElement(String recipeID, String elementName, File audioFile) {
        String hex = fileToHex(audioFile);
        String response = sendRequest("/specify-recipe-creator-element",
                "recipeID=" + recipeID + "&elementName=" + elementName + "&hex=" + hex, "POST");
        if (response.equals("invalid")) {
            return null;
        }
        return response;
    }

    /**
     * Returns true if the recipe is ready to be created, false otherwise.
     * 
     * @param recipeID
     * @return true if the recipe builder is completed, false otherwise
     */
    public static boolean isRecipeCreatorCompleted(String recipeID) {
        String response = sendRequest("/is-recipe-creator-completed", "recipeID=" + recipeID, "GET");
        return response.equals("true");
    }

    /**
     * Generates a recipe from the recipe builder.
     * 
     * @param recipeID
     */
    public static void generateRecipe(String recipeID) {
        sendRequest("/generate-recipe", "recipeID=" + recipeID, "PUT");
    }

    /**
     * Removes the recipe with the specified ID.
     * 
     * @param recipeID
     */
    public static void removeRecipe(String recipeID) {
        sendRequest("/remove-recipe", "recipeID=" + recipeID, "DELETE");
    }

    /**
     * Saves the recipe with the specified ID to the server's filesystem.
     * 
     * @param recipeID
     */
    public static void saveRecipe(String recipeID) {
        sendRequest("/save-recipe", "recipeID=" + recipeID, "GET");
    }

    /**
     * Edits the instructions of the recipe with the specified ID.
     * 
     * @param recipeID
     * @param newInstructions
     */
    public static void editRecipe(String recipeID, String newInstructions) {
        try {
            sendRequest("/edit-recipe",
                    "recipeID=" + recipeID + "&newInstructions=" + URLEncoder.encode(newInstructions, "UTF-8"), "PUT");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String addAccount(String username, String password) {
        String response = sendRequest("/add-account", "username=" + username + "&password=" + password, "POST");
        if (response.equals("created")) {
            return username;
        }
        return null;
    }

    /**
     * Sends a request to the server and returns the response.
     * 
     * @param path
     * @param query
     * @param method
     * @return the response from the server
     */
    private static String sendRequest(String path, String query, String method) {
        try {
            String urlString = "http://localhost:8100" + path;
            if (query != null) {
                urlString += "?" + query;
            }
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts the specified file to a hex string, useful for encoding
     * the audio file before it gets sent to the server.
     * 
     * @param file
     * @return the hex string
     */
    private static String fileToHex(File file) {
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            return HexFormat.of().formatHex(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
