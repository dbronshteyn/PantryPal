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

import java.util.List;


class RecipeCreationScene extends VBox {

    SceneController sceneController;
    AudioRecorder audioRecorder;
    Button completedButton;
    
    public class AudioRecorder {

        String audioFilePath;
        ToggleButton recordButton;
        AudioFormat audioFormat;
        TargetDataLine targetDataLine;
        Thread t;

        AudioRecorder(String audioFilePath, ToggleButton recordButton) {
            this.audioFilePath = audioFilePath;
            this.recordButton = recordButton;
        }

        public void recordAudio() {
            // implement this part
            // make sure to save the resulting audio file to this.audioFilePath   
            // i also added the recordButton to this class, so when the recording 
            // starts you can do recordButton.setText("Stop Recording");
            
            audioFormat = getAudioFormat();

            this.recordButton.setText("Stop Recording");

            Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            startRecording();
                        } catch (Exception e) {}
                    }
                }
            );
            t.start();
        }

        public void stopRecordingAudio() {
            stopRecording();
            this.recordButton.setText("Start Recording");
        }

        private AudioFormat getAudioFormat() {
            // the number of samples of audio per second.
            // 44100 represents the typical sample rate for CD-quality audio.
            float sampleRate = 44100;

            // the number of bits in each sample of a sound that has been digitized.
            int sampleSizeInBits = 16;

            // the number of audio channels in this format (1 for mono, 2 for stereo).
            int channels = 2;

            // whether the data is signed or unsigned.
            boolean signed = true;

            // whether the audio data is stored in big-endian or little-endian order.
            boolean bigEndian = false;

            return new AudioFormat(
                    sampleRate,
                    sampleSizeInBits,
                    channels,
                    signed,
                    bigEndian);
        }

        private void startRecording() {
            try {
                // the format of the TargetDataLine
                DataLine.Info dataLineInfo = new DataLine.Info(
                        TargetDataLine.class,
                        audioFormat);
                // the TargetDataLine used to capture audio data from the microphone
                targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                targetDataLine.open(audioFormat);
                targetDataLine.start();
                //recordingLabel.setVisible(true);

                // the AudioInputStream that will be used to write the audio data to a file
                AudioInputStream audioInputStream = new AudioInputStream(
                        targetDataLine);

                // the file that will contain the audio data
                File audioFile = new File(audioFilePath);
                AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
                //recordingLabel.setVisible(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void stopRecording() {
            targetDataLine.stop();
            targetDataLine.close();
        }
    }

    RecipeCreationScene(SceneController sceneController, String ingredientsAudioFile) {
        this.sceneController = sceneController;

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            sceneController.displayRecipeList(null);
        });
        this.getChildren().add(cancelButton);

        //Button recordIngredientsButton = new Button("Record Ingredients");
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
            System.out.println("should generate a recipe now");
            // want to make this call some recipe generator on the backend


        });
        this.getChildren().add(completedButton);
}

    public void displayRecipeCreationScene() {
        sceneController.setTop(new Label("Create a Recipe"));
        sceneController.setCenter(this);
    }
}