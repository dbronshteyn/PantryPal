package ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;

import javax.sound.sampled.*;
import java.io.File;

import backend.Controller;
import backend.Recipe;

/**
 * The RecipeCreationScene class is responsible for the UI components of
 * creating a recipe by allowing the user to record audio for inputting
 * ingredients.
 */
class RecipeCreationScene extends VBox {

    SceneController sceneController;
    AudioRecorder audioRecorder;
    Button completedButton;

    public class RecipeCreationTopBar extends HBox {
        RecipeCreationTopBar() {
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 10, 10, 10));
            this.setSpacing(10);
            this.setStyle("-fx-background-color: #c6ecc6;");

            Button cancelButton = createStyledButton("Cancel");
            cancelButton.setOnAction(e -> sceneController.displayRecipeList(null));
            this.getChildren().add(cancelButton);

            Label title = new Label("Create a Recipe");
            title.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
            this.getChildren().add(title);
        }
    }

    /**
     * The AudioRecorder class allows the user to record inputted audio.
     */
    public class AudioRecorder {

        File audioFile;
        ToggleButton recordButton;
        AudioFormat audioFormat;
        TargetDataLine targetDataLine;

        /**
         * Constructor for the AudioRecorder class.
         *
         * @param audioFile    The file where we want to save the recorded audio.
         * @param recordButton The button that allows the user to record audio.
         */
        AudioRecorder(File audioFile, ToggleButton recordButton) {
            this.audioFile = audioFile;
            this.recordButton = recordButton;
            this.audioFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    44100,
                    16,
                    2,
                    4,
                    44100,
                    false);
        }

        /**
         * Allows the user to begin recording audio when the "Record" button is pressed.
         */
        public void recordAudio() {
            Thread recordingThread = new Thread(this::startRecording);
            recordingThread.start();
        }

        /**
         * Allows the user to stop recording audio when the "Stop" button is pressed.
         */
        public void stopRecordingAudio() {
            stopRecording();
        }

        /**
         * Initiates the process of recording audio.
         * Sets up the audio recording parameters and captures audio data from the
         * microphone.
         */
        private void startRecording() {
            try {
                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                targetDataLine.open(audioFormat);
                targetDataLine.start();
                AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * This method is used to stop the audio recording process.
         * Stops and closes the data line.
         */
        private void stopRecording() {
            targetDataLine.stop();
            targetDataLine.close();
        }
    }

    /**
     * Constructor for the RecipeCreationScene class.
     *
     * @param sceneController      The controller for managing scenes.
     * @param ingredientsAudioFile The file used to store recorded audio for the
     *                             input ingredients.
     */
    RecipeCreationScene(SceneController sceneController, Controller controller, File ingredientsAudioFile) {
        this.sceneController = sceneController;
        this.setSpacing(10);
        this.setPadding(new Insets(20, 20, 20, 20));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: #e7ffe6;");

        ToggleButton recordIngredientsButton = new ToggleButton("Record Ingredients");
        this.audioRecorder = new AudioRecorder(ingredientsAudioFile, recordIngredientsButton);
        recordIngredientsButton.setOnAction(e -> {
            if (recordIngredientsButton.isSelected()) {
                audioRecorder.recordAudio();
                recordIngredientsButton.setText("Stop Recording");
            } else {
                audioRecorder.stopRecordingAudio();
                recordIngredientsButton.setText("Record Ingredients");
            }
        });
        this.getChildren().add(createStyledToggleButton(recordIngredientsButton));

        Button completeButton = createStyledButton("Generate Recipe");
        completeButton.setOnAction(e -> {
            controller.createAndShowRecipe(ingredientsAudioFile);
        });
        this.getChildren().add(completeButton);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #a3d9a5; -fx-text-fill: #000000;");
        button.setFont(new Font("Arial", 14));
        button.setPadding(new Insets(10, 20, 10, 20));
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #8cc68c; -fx-text-fill: #000000;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #a3d9a5; -fx-text-fill: #000000;"));
        return button;
    }

    private ToggleButton createStyledToggleButton(ToggleButton toggleButton) {
        toggleButton.setStyle("-fx-background-color: #a3d9a5; -fx-text-fill: #000000;");
        toggleButton.setFont(new Font("Arial", 14));
        toggleButton.setPadding(new Insets(10, 20, 10, 20));
        toggleButton.setOnMouseEntered(e -> {
            if (!toggleButton.isSelected())
                toggleButton.setStyle("-fx-background-color: #8cc68c; -fx-text-fill: #000000;");
        });
        toggleButton.setOnMouseExited(e -> {
            if (!toggleButton.isSelected())
                toggleButton.setStyle("-fx-background-color: #a3d9a5; -fx-text-fill: #000000;");
        });
        toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                toggleButton.setStyle("-fx-background-color: #6fa06b; -fx-text-fill: #000000;");
            } else {
                toggleButton.setStyle("-fx-background-color: #a3d9a5; -fx-text-fill: #000000;");
            }
        });
        return toggleButton;
    }

    public void displayRecipeCreationScene() {
        sceneController.setTop(new RecipeCreationTopBar());
        sceneController.setCenter(this);
    }
}
