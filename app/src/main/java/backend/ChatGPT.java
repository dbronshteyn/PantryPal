package backend;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The ChatGPT class provides an interface to interact with ChatGPT.
 * 
 * It allows you to submit a prompt to ChatGPT and generates a response.
 * 
 * This class requires an API key to use the OpenAI service.
 */

public class ChatGPT {

    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String MODEL = "text-davinci-003";

    String apiKey;

    public ChatGPT(String apiKey) {
        this.apiKey = apiKey;
    }

    public String generateText(String prompt, int maxTokens) throws IOException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 1.0);

        HttpClient client = HttpClient.newHttpClient();
        String responseBody = "";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + this.apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new IOException("Error sending ChatGPT request");
        }

        JSONObject responseJson = new JSONObject(responseBody);
        JSONArray choices = responseJson.getJSONArray("choices");
        return choices.getJSONObject(0).getString("text");
    }

}
