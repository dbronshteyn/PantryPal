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

public class LoginScene extends VBox {

    SceneManager sceneManager;
    Controller controller;
    private Label statusLabel = new Label("");
    private File automaticLoginFile = new File("automaticLogin.json");

    public class LoginSceneTopBar extends HBox {

        LoginSceneTopBar() {
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 10, 10, 10));
            this.setSpacing(10);
            this.setStyle("-fx-background-color: #c6ecc6;");

            Label title = new Label("Login");
            title.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
            this.getChildren().add(title);
        }

    }

    /**
     * Constructs a new AccountCreationScene with the provided scene manager.
     * 
     * @param sceneManager
     */
    LoginScene(SceneManager sceneManager, Controller controller) {
        this.sceneManager = sceneManager;
        this.controller = controller;
        this.setSpacing(5);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setAlignment(Pos.TOP_CENTER);
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #e7ffe6;");
    }

    /**
     * Displays the login scene, or automatically logs in if file is present
     */
    public void displayLoginScene() {
        if (automaticLoginFile.exists()) {
            try {
                JSONObject in = new JSONObject(new String(Files.readAllBytes(Paths.get(automaticLoginFile.getAbsolutePath()))));
                if (controller.login(in.getString("username"), in.getString("password"))) {
                    sceneManager.displayRecipeList();
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error reading automatic login file");
            }
        }

        this.getChildren().clear();

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);

        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        CheckBox autoLogin = new CheckBox("Remember Me");

        Button loginButton = createStyledButton("Login");
        loginButton.setDisable(true);
        loginButton.setOnAction(e -> {
            if (controller.login(usernameField.getText(), passwordField.getText())) {
                statusLabel.setText("");
                if (autoLogin.isSelected()) {
                    this.setAutomaticLogin(usernameField.getText(), passwordField.getText());
                }
                sceneManager.displayRecipeList();
            } else {
                statusLabel.setText("Login unsuccessful, try again or create an account");
            }
        });

        Button createAccountButton = createStyledButton("Create Account");
        createAccountButton.setOnAction(e -> sceneManager.displayAccountCreationScene());

        HBox buttonContainer = new HBox(loginButton, createAccountButton);
        buttonContainer.setSpacing(10);
        buttonContainer.setAlignment(Pos.CENTER);


        this.getChildren().add(statusLabel);
        setTextFieldTriggers(usernameField, loginButton, passwordField);
        setTextFieldTriggers(passwordField, loginButton, usernameField);

        this.getChildren().addAll(usernameField, passwordField, autoLogin, buttonContainer);

        sceneManager.setCenter(this);
        sceneManager.setTop(new LoginSceneTopBar());
    }

    private void setTextFieldTriggers(TextField textField, Button loginButton, TextField otherTextField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(textField.getText().length() == 0 || otherTextField.getText().length() == 0);
        });
    }

    /**
     * Creates a styled button with the provided text.
     * 
     * @param text
     * @return Button with the provided text.
     */
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #a3d9a5; -fx-text-fill: #000000;");
        button.setFont(new Font("Arial", 14));
        button.setPadding(new Insets(5, 15, 5, 15));

        // Hover effect
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #8cc68c; -fx-text-fill: #000000;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #a3d9a5; -fx-text-fill: #000000;"));

        return button;
    }

    private void setAutomaticLogin(String username, String password) {
        JSONObject out = new JSONObject();
        out.put("username", username);
        out.put("password", password);
        try {
            FileWriter fw = new FileWriter(automaticLoginFile);
            fw.write(out.toString());
            fw.flush();
            fw.close();
        } catch (Exception e) {
            System.out.println("Error writing automatic login file");
        }
    }
}