package middleware;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.nio.file.Files;  
import java.nio.file.Paths;

/**
 * The Controller class is responsible for managing the recipes of this
 * application.
 * 
 * It initializes and interacts with a RecipeCreator, RecipeList and
 * SceneController to create and show recipes, as well as dealing with user
 * interactions.
 * It also deals with reading from and saving to the JSON file holding the
 * recipes.
 * 
 * The class relies on an API key to access the ChatGPT and Whisper services.
 */

public class Controller {
  
    public Controller() {
    }

    public String generateNewRecipeBuilder() {
        return this.sendRequest("/generate-new-recipe-builder", null, "GET");
    }

    public String getRecipeTitle(String recipeID) {
        return this.sendRequest("/get-recipe-title", "recipeID=" + recipeID, "GET");
    }

    public String getRecipeInstructions(String recipeID) {
        try {
            return URLDecoder.decode(this.sendRequest("/get-recipe-instructions", "recipeID=" + recipeID, "GET"), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getRecipeIDs() {
        String response = this.sendRequest("/get-recipe-ids", null, "GET");
        if (response.equals(".")) {
            return new ArrayList<>();
        }
        return Arrays.asList(response.split(","));
    }

    public void resetRecipeCreatorElement(String recipeID, String elementName) {
        this.sendRequest("/reset-recipe-creator-element", "recipeID=" + recipeID + "&elementName=" + elementName, "GET");
    }

    public String specifyRecipeCreatorElement(String recipeID, String elementName, File audioFile) {
        String hex = this.fileToHex(audioFile);
        return this.sendRequest("/specify-recipe-creator-element", "recipeID=" + recipeID + "&elementName=" + elementName + "&hex=" + hex, "GET");
    }

    public boolean isRecipeCreatorCompleted(String recipeID) {
        String response = this.sendRequest("/is-recipe-creator-completed", "recipeID=" + recipeID, "GET");
        return response.equals("true");
    }

    public void generateRecipe(String recipeID) {
        this.sendRequest("/generate-recipe", "recipeID=" + recipeID, "GET");
    }

    public void removeRecipe(String recipeID) {
        this.sendRequest("/remove-recipe", "recipeID=" + recipeID, "GET");
    }

    public void saveRecipe(String recipeID) {
        this.sendRequest("/save-recipe", "recipeID=" + recipeID, "GET");
    }

    public void editRecipe(String recipeID, String newInstructions) {
        try {
            this.sendRequest("/edit-recipe", "recipeID=" + recipeID + "&newInstructions=" + URLEncoder.encode(newInstructions, "UTF-8"), "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String sendRequest(String path, String query, String method) {
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

    private String fileToHex(File file) {
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
