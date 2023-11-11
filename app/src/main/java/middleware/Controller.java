package middleware;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

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
 * This class represents a controller that handles requests from the frontend.
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
     * Returns the recipe builder element titles.
     * 
     * @param recipeID
     * @return the recipe builder element titles
     */
    public static String getRecipeTitle(String recipeID) {
        return sendRequest("/get-recipe-title", "recipeID=" + recipeID, "GET");
    }

    /**
     * Returns the recipe builder element instructions.
     * 
     * @param recipeID
     * @return the recipe builder element instructions
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
     * Returns the recipe builder element meal type.
     * 
     * @return the recipe builder element meal type
     */
    public static List<String> getRecipeIDs() {
        String response = sendRequest("/get-recipe-ids", null, "GET");
        if (response.equals(".")) {
            return new ArrayList<>();
        }
        return Arrays.asList(response.split(","));
    }

    /**
     * Resets the recipe builder element.
     * 
     * @param recipeID
     * @param elementName
     */
    public static void resetRecipeCreatorElement(String recipeID, String elementName) {
        sendRequest("/reset-recipe-creator-element", "recipeID=" + recipeID + "&elementName=" + elementName,
                "GET");
    }

    /**
     * Specifies the recipe builder element.
     * 
     * @param recipeID
     * @param elementName
     * @param audioFile
     * @return the response from the server
     */
    public static String specifyRecipeCreatorElement(String recipeID, String elementName, File audioFile) {
        String hex = fileToHex(audioFile);
        return sendRequest("/specify-recipe-creator-element",
                "recipeID=" + recipeID + "&elementName=" + elementName + "&hex=" + hex, "GET");
    }

    /**
     * Returns true if the recipe builder is completed, false otherwise.
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
        sendRequest("/generate-recipe", "recipeID=" + recipeID, "GET");
    }

    /**
     * Removes the recipe with the specified ID.
     * 
     * @param recipeID
     */
    public static void removeRecipe(String recipeID) {
        sendRequest("/remove-recipe", "recipeID=" + recipeID, "GET");
    }

    /**
     * Saves the recipe with the specified ID.
     * 
     * @param recipeID
     */
    public static void saveRecipe(String recipeID) {
        sendRequest("/save-recipe", "recipeID=" + recipeID, "GET");
    }

    /**
     * Edits the recipe with the specified ID.
     * 
     * @param recipeID
     * @param newInstructions
     */
    public static void editRecipe(String recipeID, String newInstructions) {
        try {
            sendRequest("/edit-recipe",
                    "recipeID=" + recipeID + "&newInstructions=" + URLEncoder.encode(newInstructions, "UTF-8"), "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * Converts the specified file to a hex string.
     * 
     * @param file
     * @return the hex string
     */
    private static String fileToHex(File file) {
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            Files.write(Paths.get((new File("audio2.wav")).getAbsolutePath()), fileBytes);
            String out = "";
            for (byte b : fileBytes) {
                out += String.format("%02X", b);
            }
            return out;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
