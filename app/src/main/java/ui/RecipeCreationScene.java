package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
        Button recordButton;
        String oldButtonText;

        AudioRecorder(String audioFilePath, Button recordButton) {
            this.audioFilePath = audioFilePath;
            this.recordButton = recordButton;
            this.oldButtonText = recordButton.getText();
        }

        public void recordAudio() {
            // implement this part
            // make sure to save the resulting audio file to this.audioFilePath   
            // i also added the recordButton to this class, so when the recording 
            // starts you can do recordButton.setText("Stop Recording");

            this.recordButton.setText("Stop Recording");
            this.recordButton.setOnAction(e1 -> {
                this.recordButton.setText(this.oldButtonText);
                // here is where you'd want to save the recorded audio
                this.recordButton.setOnAction(e2 -> {
                    recordAudio();
                });
            });
        }
    }

    RecipeCreationScene(SceneController sceneController, String ingredientsAudioFile) {
        this.sceneController = sceneController;

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            sceneController.displayRecipeList(null);
        });
        this.getChildren().add(cancelButton);

        Button recordIngredientsButton = new Button("Record Ingredients");
        this.audioRecorder = new AudioRecorder(ingredientsAudioFile, recordIngredientsButton);
        this.setSpacing(5);
        recordIngredientsButton.setOnAction(e -> {
            audioRecorder.recordAudio();
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