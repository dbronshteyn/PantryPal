package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.*;
import backend.Recipe;
import java.awt.*;
import java.awt.event.*;
import backend.Controller;
import java.util.List;

/**
 * The RecipeCreationScene class is resposnible for the UI components of 
 * creating a recipe by allowing the user to record audio for inputting ingredients.
 */
class RecipeCreationScene extends VBox {

    SceneController sceneController;
    AudioRecorder audioRecorder;
    Button completedButton;
    
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
         * @param audioFile     The file where we want to save the recorded audio.
         * @param recordButton  The button that allows the user to record audio.
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
            this.recordButton.setText("Stop Recording");
            Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        startRecording();
                    }
                }
            );
            t.start();
        }

        /**
         * Allows the user to stop recording audio when the "Stop" button is pressed.
         */
        public void stopRecordingAudio() {
            stopRecording();
            this.recordButton.setText("Start Recording");
        }

        /**
         * Initiates the process of recording audio. 
         * Sets up the audio recording parameters and captures audio data from the microphone.
         */
        private void startRecording() {
            try {
                DataLine.Info dataLineInfo = new DataLine.Info(
                    TargetDataLine.class,
                    audioFormat);
                targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                targetDataLine.open(audioFormat);
                targetDataLine.start();
                AudioInputStream audioInputStream = new AudioInputStream(
                        targetDataLine);
                AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        this.audioFile);
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
     * @param sceneController        The controller for managing scenes.
     * @param controller             The backend controller.
     * @param ingredientsAudioFile   The file used to store recorded audio for the input ingredients.
     */
    
    
     RecipeCreationScene(SceneController sceneController, Controller controller, File ingredientsAudioFile) {
        this.sceneController = sceneController;

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            sceneController.displayRecipeList(null);
        });
        this.getChildren().add(cancelButton);

        ToggleButton recordIngredientsButton = new ToggleButton("Record Ingredients");
        this.audioRecorder = new AudioRecorder(ingredientsAudioFile, recordIngredientsButton);
        this.setSpacing(5);
        recordIngredientsButton.setOnAction(e -> {
            if (recordIngredientsButton.isSelected()) {
                audioRecorder.recordAudio();
            } else {
                audioRecorder.stopRecordingAudio();
            }

        });
        this.getChildren().add(recordIngredientsButton);

        completedButton = new Button("Generate Recipe");
        completedButton.setOnAction(e -> {
            controller.createAndShowRecipe(ingredientsAudioFile);
        });
        this.getChildren().add(completedButton);
    }

    /**
     * Display the recipe creation scene with title and content.
     */
    public void displayRecipeCreationScene() {
        sceneController.setTop(new Label("Create a Recipe"));
        sceneController.setCenter(this);
    }
}