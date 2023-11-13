package backend;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

/**
 * Mock class for ChatGPT
 */
class ChatGPTMock extends ChatGPT {

    String mockInput;
    String mockOutput;

    public ChatGPTMock() {
        super("");
    }

    public void setMockScenario(String input, String output) {
        this.mockInput = input;
        this.mockOutput = output;
    }

    /**
     * Generates mock text.
     */
    @Override
    public String generateText(String prompt, int maxTokens) {
        assertTrue(maxTokens > 0);
        assertEquals(mockInput, prompt);
        return mockOutput;
    }
}

/**
 * Mock class for Whisper
 */
class WhisperMock extends Whisper {

    String mockInput;
    String mockOutput;

    public WhisperMock() {
        super("");
    }

    public void setMockScenario(String input, String output) {
        this.mockInput = input;
        this.mockOutput = output;
    }

    /**
     * Transcribes mock audio files.
     */
    @Override
    public String transcribeAudio(File audioFile) throws IOException {
        assertNotNull(audioFile);
        if (audioFile.getName().equals("throw-exception.wav")) {
            throw new IOException();
        }
        assertEquals(mockInput, audioFile.getName());
        return mockOutput;
    }
}