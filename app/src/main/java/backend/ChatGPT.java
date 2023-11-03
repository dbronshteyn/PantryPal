package backend;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGPT {

    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";

    /*
     * This code was just so that we can create a ChatGPT object in the future if
     * needed. It is not necessary for the code to work. More to scale if needed.
     */
    boolean isInitialized = false;

    public ChatGPT() {
        this.isInitialized = true;
    }

    public String generateText(String prompt, int maxTokens) {

        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 1.0);

        HttpClient client = HttpClient.newHttpClient();
        String responseBody = "";

        // Create the request object
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString());

            // Process the response
            responseBody = response.body();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null; // or handle error appropriately
        }

        JSONObject responseJson = new JSONObject(responseBody);
        JSONArray choices = responseJson.getJSONArray("choices");
        return choices.getJSONObject(0).getString("text");
    }

}
