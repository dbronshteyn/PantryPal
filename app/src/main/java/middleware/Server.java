package middleware;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.io.File;
import java.io.OutputStream;
import java.net.URI;
import java.util.HexFormat;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.URLEncoder;
import java.net.URLDecoder;

import backend.Recipe;
import backend.RecipeBuilder;
import backend.RecipeList;
import backend.ChatGPT;
import backend.Whisper;


public class Server {

    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";
    private static final String DATABASE_FILENAME = "database.json";

    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        HttpServer server = HttpServer.create(
            new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
            0
        );

        server.createContext("/", new RequestHandler(new File(DATABASE_FILENAME)));

        server.setExecutor(threadPoolExecutor);
        server.start();
    }
}


class RequestHandler implements HttpHandler {

    private static final String OPENAI_API_KEY = "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";
    private static final String SUCCESS_MESSAGE = "success";
    private static final String FAILURE_MESSAGE = "failure";

    RecipeList recipeList;
    Map<String, RecipeBuilder> recipeBuilders;
    Map<String, Recipe> temporaryRecipes;
    File audioFile;
    ChatGPT chatGPT;
    Whisper whisper;

    public RequestHandler(File databaseFile) {
        this.recipeList = new RecipeList(databaseFile);
        this.recipeBuilders = new HashMap<>();
        this.temporaryRecipes = new HashMap<>();
        this.audioFile = new File("audio.wav");
        this.chatGPT = new ChatGPT(OPENAI_API_KEY);
        this.whisper = new Whisper(OPENAI_API_KEY);
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            URI uri = httpExchange.getRequestURI();
            String path = uri.getPath();
            Map<String, String> query = this.parseQuery(uri.getQuery());

            String response = "";
            if (path.equals("/generate-new-recipe-builder")) {
                response = this.handleGenerateNewRecipeBuilder();
            } else if (path.equals("/get-recipe-title")) {
                response = this.handleGetRecipeTitle(query);
            } else if (path.equals("/get-recipe-instructions")) {
                response = this.handleGetRecipeInstructions(query);
            } else if (path.equals("/get-recipe-ids")) {
                response = this.handleGetRecipeIDs();
            } else if (path.equals("/reset-recipe-creator-element")) {
                response = this.handleResetRecipeCreatorElement(query);
            } else if (path.equals("/specify-recipe-creator-element")) {
                response = this.handleSpecifyRecipeCreatorElement(query);
            } else if (path.equals("/is-recipe-creator-completed")) {
                response = this.handleIsRecipeCreatorCompleted(query);
            } else if (path.equals("/generate-recipe")) {
                response = this.handleGenerateRecipe(query);
            } else if (path.equals("/remove-recipe")) {
                response = this.handleRemoveRecipe(query);
            } else if (path.equals("/save-recipe")) {
                response = this.handleSaveRecipe(query);
            } else if (path.equals("/edit-recipe")) {
                response = this.handleEditRecipe(query);
            } else {
                response = "Invalid path";
            }
            
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(response.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> parseQuery(String queryString) {
        Map<String, String> queryMap = new HashMap<>();
        if (queryString != null) {
            String[] queries = queryString.split("&");
            for (String query : queries) {
                queryMap.put(query.split("=")[0], query.split("=")[1]);
            }
        }
        return queryMap;
    }

    private String handleGenerateNewRecipeBuilder() {
        RecipeBuilder recipeBuilder = new RecipeBuilder(chatGPT, whisper);
        this.recipeBuilders.put(recipeBuilder.getRecipeID(), recipeBuilder);
        return recipeBuilder.getRecipeID();
    }

    private String handleGetRecipeTitle(Map<String, String> query) {
        String recipeID = query.get("recipeID");
        if (temporaryRecipes.containsKey(recipeID))
            return this.temporaryRecipes.get(recipeID).getTitle();
        return this.recipeList.getRecipeByID(recipeID).getTitle();
    }

    private String handleGetRecipeInstructions(Map<String, String> query) {
        String recipeID = query.get("recipeID");
        try {
            if (temporaryRecipes.containsKey(recipeID))
                return URLEncoder.encode(this.temporaryRecipes.get(recipeID).getInstructions(), "UTF-8");
            return URLEncoder.encode(this.recipeList.getRecipeByID(recipeID).getInstructions(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String handleGetRecipeIDs() {
        if (this.recipeList.getRecipeIDs().isEmpty()) {
            return ".";
        }
        return String.join(",", this.recipeList.getRecipeIDs());
    }

    private String handleResetRecipeCreatorElement(Map<String, String> query) {
        String recipeID = query.get("recipeID");
        String elementName = query.get("elementName");
        if (elementName.equals("mealType")) {
            this.recipeBuilders.get(recipeID).getMealTypeElement().reset();
        } else if (elementName.equals("ingredients")) {
            this.recipeBuilders.get(recipeID).getIngredientsElement().reset();
        } else {
            throw new IllegalArgumentException("Invalid element name");
        }
        return SUCCESS_MESSAGE;
    }

    private String handleSpecifyRecipeCreatorElement(Map<String, String> query) {
        try {  
            String hex = query.get("hex");
            Files.write(Paths.get(this.audioFile.getAbsolutePath()), HexFormat.of().parseHex(hex));
            String recipeID = query.get("recipeID");
            String elementName = query.get("elementName");
            if (elementName.equals("mealType")) {
                return this.recipeBuilders.get(recipeID).getMealTypeElement().specify(this.audioFile);
            } else if (elementName.equals("ingredients")) {
                return this.recipeBuilders.get(recipeID).getIngredientsElement().specify(this.audioFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return FAILURE_MESSAGE;
        }
        return FAILURE_MESSAGE;
    }

    private String handleIsRecipeCreatorCompleted(Map<String, String> query) {
        String recipeID = query.get("recipeID");
        return Boolean.toString(this.recipeBuilders.get(recipeID).isCompleted());
    }

    private String handleGenerateRecipe(Map<String, String> query) {
        String recipeID = query.get("recipeID");
        try {
            Recipe recipe = this.recipeBuilders.remove(recipeID).returnRecipe();
            this.temporaryRecipes.put(recipe.getRecipeID(), recipe);
        } catch (IOException e) {
            e.printStackTrace();
            return FAILURE_MESSAGE;
        }
        return SUCCESS_MESSAGE;
    }

    private String handleRemoveRecipe(Map<String, String> query) {
        String recipeID = query.get("recipeID");
        this.recipeList.removeRecipe(this.recipeList.getRecipeByID(recipeID));
        return SUCCESS_MESSAGE;
    }

    private String handleSaveRecipe(Map<String, String> query) {
        String recipeID = query.get("recipeID");
        this.recipeList.addRecipe(this.temporaryRecipes.remove(recipeID));
        return SUCCESS_MESSAGE;
    }

    private String handleEditRecipe(Map<String, String> query) {
        try {
            String recipeID = query.get("recipeID");
            String newInstructions = URLDecoder.decode(query.get("newInstructions"), "UTF-8");
            this.recipeList.getRecipeByID(recipeID).setInstructions(newInstructions);
            this.recipeList.updateDatabase();
            this.recipeList.sortRecipesByDate();
            return SUCCESS_MESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return FAILURE_MESSAGE;
        }
    }
}