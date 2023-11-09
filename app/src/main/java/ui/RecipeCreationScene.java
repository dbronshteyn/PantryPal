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

import java.io.IOException;

import backend.Controller;
import backend.Recipe;

/**
 * The RecipeCreationScene class is responsible for the UI components of
 * creating a recipe by allowing the user to record audio for inputting
 * ingredients.
 */
class RecipeCreationScene extends VBox {

    SceneController sceneController;
    Button completedButton;
    String transcribedIngredients;
    String transcribedMealType;
    AudioRecorder mealTypeAudioRecorder;
    AudioRecorder ingredientsAudioRecorder;

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
                44100,
                16,
                1,
                true,
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
    RecipeCreationScene(SceneController sceneController, Controller controller, File ingredientsAudioFile, File mealTypeAudioFile) {
        this.sceneController = sceneController;
        this.setSpacing(10);
        this.setPadding(new Insets(20, 20, 20, 20));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: #e7ffe6;");

        Button completeButton = createStyledButton("Generate Recipe");
        ToggleButton recordIngredientsButton = new ToggleButton("Record Ingredients");
        ToggleButton recordMealTypeButton = new ToggleButton("Record Meal Type");
        this.transcribedIngredients = null;
        this.transcribedMealType = null;

        Label recordMealTypeLabel = new Label("Please select either breakfast, lunch, or dinner. Recorded meal type will appear here.");
        this.mealTypeAudioRecorder = new AudioRecorder(mealTypeAudioFile, recordMealTypeButton);
        recordMealTypeButton.setOnAction(e -> {
            if (recordMealTypeButton.isSelected()) {
                this.transcribedMealType = null;
                completeButton.setDisable(true);
                recordIngredientsButton.setDisable(true);
                this.mealTypeAudioRecorder.recordAudio();
                recordMealTypeButton.setText("Stop Recording");
            } else {
                mealTypeAudioRecorder.stopRecordingAudio();
                try {
                    transcribedMealType = controller.getMealType(controller.transcribeAudio(mealTypeAudioFile));
                } catch (IOException e1) {
                    recordMealTypeLabel.setText("Error recording audio!");
                }
                recordIngredientsButton.setDisable(false);
                if (transcribedMealType == null) {
                    recordMealTypeLabel.setText("Please say either breakfast, lunch, or dinner.");
                } else {
                    recordMealTypeLabel.setText("You selected " + transcribedMealType);
                }
                if (this.transcribedIngredients != null && this.transcribedMealType != null) {
                    completeButton.setDisable(false);
                }
                recordMealTypeButton.setText("Record Meal Type");
            }
        });
        this.getChildren().add(createStyledToggleButton(recordMealTypeButton));
        this.getChildren().add(recordMealTypeLabel);

        Label recordIngredientsLabel = new Label("Recorded ingredients will appear here...");
        this.ingredientsAudioRecorder = new AudioRecorder(ingredientsAudioFile, recordIngredientsButton);
        recordIngredientsButton.setOnAction(e -> {
            if (recordIngredientsButton.isSelected()) {
                completeButton.setDisable(true);
                recordMealTypeButton.setDisable(true);
                this.transcribedIngredients = null;
                ingredientsAudioRecorder.recordAudio();
                recordIngredientsButton.setText("Stop Recording");
            } else {
                ingredientsAudioRecorder.stopRecordingAudio();
                try {
                    transcribedIngredients = controller.transcribeAudio(ingredientsAudioFile);
                } catch (Exception e1) {
                    recordIngredientsLabel.setText("Error recording audio!");
                }
                recordMealTypeButton.setDisable(false);
                recordIngredientsLabel.setText("You said: " + transcribedIngredients);
                if (this.transcribedIngredients != null && this.transcribedMealType != null) {
                    completeButton.setDisable(false);
                }
                recordIngredientsButton.setText("Record Ingredients");
            }
        });
        this.getChildren().add(createStyledToggleButton(recordIngredientsButton));
        this.getChildren().add(recordIngredientsLabel);

        completeButton.setOnAction(e -> {
            controller.createAndShowRecipe(transcribedMealType, transcribedIngredients);
        });
        completeButton.setDisable(true);
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
        this.transcribedIngredients = null;
        this.transcribedMealType = null;
        sceneController.setTop(new RecipeCreationTopBar());
        sceneController.setCenter(this);
    }
}
