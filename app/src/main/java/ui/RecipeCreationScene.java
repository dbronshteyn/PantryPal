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


class RecipeCreationScene extends VBox {

    SceneController sceneController;
    AudioRecorder audioRecorder;
    Button completedButton;
    
    public class AudioRecorder {

        File audioFile;
        ToggleButton recordButton;
        AudioFormat audioFormat;
        TargetDataLine targetDataLine;

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

        public void stopRecordingAudio() {
            stopRecording();
            this.recordButton.setText("Start Recording");
        }

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

        private void stopRecording() {
            targetDataLine.stop();
            targetDataLine.close();
        }
    }

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

    public void displayRecipeCreationScene() {
        sceneController.setTop(new Label("Create a Recipe"));
        sceneController.setCenter(this);
    }
}