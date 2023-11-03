package backend;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;

/*
 * This class facilitates calling to Whisper and ChatGPT objects and returns the response from ChatGPT.
 */

public class OpenAIManager {

    private ChatGPT chatGPT;
    private Whisper whisper;
    private File audioFile;
    private int maxTokens = 100;
    private String prompt = "";
    private String response = "";

    public OpenAIManager(String audioFilePath, int maxTokens) {
        this.chatGPT = new ChatGPT();
        this.whisper = new Whisper();
        audioFile = new File(audioFilePath);
        this.maxTokens = maxTokens;

    }

    public void getTranscribeText() throws JSONException, IOException {
        prompt = this.whisper.transcribeAudio(audioFile);
    }

    public void getChatGPTResponse(String prompt, int maxTokens) {
        response = this.chatGPT.generateText(prompt, maxTokens);
    }

    public String getResponse() throws JSONException, IOException {
        getTranscribeText();
        getChatGPTResponse(prompt, maxTokens);
        return response;
    }
}
