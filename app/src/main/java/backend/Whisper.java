package backend;

import java.io.*;
import java.net.*;
import org.json.JSONException;
import org.json.JSONObject;

public class Whisper {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN = "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";
    private static final String MODEL = "whisper-1";

    public Whisper() {
        // Constructor, if needed for future enhancements.
    }

    /*
     * You should only be calling this method "transcribeAudio" in the following
     * example:
     * 
     *  Whisper whisper = new Whisper()
     * File audioFile = new File("path/to/your/audiofile.mp3");
     * 
     * try {
     * String transcription = whisper.transcribeAudio(audioFile);
     * System.out.println("Transcription Result: " + transcription);
     * } catch (Exception e) {
     * System.err.println("An error occurred during transcription.");
     * e.printStackTrace();
     * }
     */
    public String transcribeAudio(File audioFile) throws IOException, JSONException {
        HttpURLConnection connection = null;
        try {
            URL url = new URI(API_ENDPOINT).toURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String boundary = "Boundary-" + System.currentTimeMillis();
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

            OutputStream outputStream = connection.getOutputStream();
            writeParameterToOutputStream(outputStream, "model", MODEL, boundary);
            writeFileToOutputStream(outputStream, audioFile, boundary);
            outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return handleSuccessResponse(connection);
            } else {
                return handleErrorResponse(connection);
            }
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URI for API endpoint", e);
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void writeParameterToOutputStream(OutputStream outputStream, String parameterName, String parameterValue,
            String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n").getBytes());
        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    private void writeFileToOutputStream(OutputStream outputStream, File file, String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
                ("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n").getBytes());
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    private String handleSuccessResponse(HttpURLConnection connection) throws IOException, JSONException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        JSONObject responseJson = new JSONObject(response.toString());
        return responseJson.getString("text");
    }

    private String handleErrorResponse(HttpURLConnection connection) throws IOException, JSONException {
        StringBuilder errorResponse = new StringBuilder();
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
        }
        return errorResponse.toString();
    }
}
