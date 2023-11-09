package ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;

import javax.sound.sampled.*;
import java.io.File;

import java.io.IOException;

import backend.Recipe;
import backend.RecipeBuilder;
import backend.RecipeBuilder.ResettableElement;
import backend.RecipeList;


/**
 * The RecipeCreationScene class is responsible for the UI components of
 * creating a recipe by allowing the user to record audio for inputting
 * ingredients.
 */
class RecipeCreationScene extends VBox {

    SceneController sceneController;
    Button completedButton;
    File ingredientsAudioFile;
    File mealTypeAudioFile;
    RecipeList recipeList;

    public class RecipeCreationTopBar extends HBox {

        private Button cancelButton;

        RecipeCreationTopBar() {
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 10, 10, 10));
            this.setSpacing(10);
            this.setStyle("-fx-background-color: #c6ecc6;");

            cancelButton = createStyledButton("Cancel");
            cancelButton.setOnAction(e -> sceneController.displayRecipeList(null));
            this.getChildren().add(cancelButton);

            Label title = new Label("Create a Recipe");
            title.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
            this.getChildren().add(title);
        }

        public Button getCancelButton() {
            return cancelButton;
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
    RecipeCreationScene(SceneController sceneController, RecipeList recipeList, File ingredientsAudioFile, File mealTypeAudioFile) {
        this.sceneController = sceneController;
        this.setSpacing(10);
        this.setPadding(new Insets(20, 20, 20, 20));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: #e7ffe6;");

        this.recipeList = recipeList;
        this.ingredientsAudioFile = ingredientsAudioFile;
        this.mealTypeAudioFile = mealTypeAudioFile;
    }

    private void setRecordingButtonTriggers(ToggleButton button, 
                                            Label label, 
                                            String successMessage, 
                                            String invalidTypeMessage, 
                                            File audioFile, 
                                            RecipeBuilder recipeBuilder, 
                                            ResettableElement element, 
                                            Button completeButton, 
                                            Button cancelButton, 
                                            ToggleButton otherButton) {
        AudioRecorder audioRecorder = new AudioRecorder(audioFile, button);
        String originalText = button.getText();
        button.setOnAction(e -> {
            if (button.isSelected()) {
                element.reset();
                completeButton.setDisable(true);
                otherButton.setDisable(true);
                cancelButton.setDisable(true);
                audioRecorder.recordAudio();
                button.setText("Stop Recording");
            } else {
                audioRecorder.stopRecordingAudio();
                try {
                    String result = element.specify(audioFile);
                    if (result == null) {
                        label.setText(invalidTypeMessage);
                    } else {
                        label.setText(String.format(successMessage, result));
                    }
                } catch (IOException e1) {
                    label.setText("Error recording audio!");
                }
                button.setDisable(false);
                button.setText(originalText);
                otherButton.setDisable(false);
                cancelButton.setDisable(false);
                if (recipeBuilder.isCompleted()) {
                    completeButton.setDisable(false);
                }
            }
        });
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

    public void displayRecipeCreationScene(RecipeBuilder recipeBuilder) {
        this.getChildren().clear();
        Button completeButton = createStyledButton("Generate Recipe");
        RecipeCreationTopBar topBar = new RecipeCreationTopBar();

        Label recordMealTypeLabel = new Label("Please select either breakfast, lunch, or dinner.");
        Label recordIngredientsLabel = new Label("Recorded ingredients will appear here...");
        ToggleButton recordIngredientsButton = new ToggleButton("Record Ingredients");
        ToggleButton recordMealTypeButton = new ToggleButton("Record Meal Type");

        setRecordingButtonTriggers(recordMealTypeButton, 
                                   recordMealTypeLabel, 
                                   "You selected %s", 
                                   "Please select either breakfast, lunch, or dinner.", 
                                   mealTypeAudioFile, 
                                   recipeBuilder, 
                                   recipeBuilder.getMealTypeElement(),
                                   completeButton, 
                                   topBar.getCancelButton(), 
                                   recordIngredientsButton);
        this.getChildren().add(createStyledToggleButton(recordMealTypeButton));
        this.getChildren().add(recordMealTypeLabel);

        setRecordingButtonTriggers(recordIngredientsButton, 
                                   recordIngredientsLabel, 
                                   "You said: %s", 
                                   null, 
                                   ingredientsAudioFile, 
                                   recipeBuilder, 
                                   recipeBuilder.getIngredientsElement(),
                                   completeButton, 
                                   topBar.getCancelButton(), 
                                   recordMealTypeButton);
        this.getChildren().add(createStyledToggleButton(recordIngredientsButton));
        this.getChildren().add(recordIngredientsLabel);

        completeButton.setOnAction(e -> {
            try {
                Recipe recipe = recipeBuilder.returnRecipe();
                sceneController.displayNewlyCreatedRecipe(recipe, recipeList);
            } catch (IOException e1) {
                e1.printStackTrace();
            } 
        });
        completeButton.setDisable(true);
        this.getChildren().add(completeButton);
        sceneController.setTop(topBar);
        sceneController.setCenter(this);
    }
}
