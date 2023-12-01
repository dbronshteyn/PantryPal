package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.*;
import javafx.scene.control.Label;

import org.json.JSONObject;
import java.io.FileWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.control.TextField;

import middleware.Controller;

public class ServerErrorScene extends VBox {

    SceneManager sceneManager;

    ServerErrorScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.setSpacing(5);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setAlignment(Pos.TOP_CENTER);
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #e7ffe6;");
    }

    public void displayServerErrorScene() {
        this.getChildren().clear();

        Label title = new Label("Server Error");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");

        Label message = new Label("There was an error connecting to the server. Please try again later.");
        message.setStyle("-fx-font-size: 16;");

        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> System.exit(0));

        this.getChildren().addAll(title, message, quitButton);
        this.setAlignment(Pos.CENTER);

        sceneManager.setCenter(this);
        sceneManager.setTop(null);
    }
}
