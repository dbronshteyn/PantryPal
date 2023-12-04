package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.control.Label;

import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.control.TextField;

import middleware.Controller;

/**
 * This class represents the scene that displays the list of recipes.
 */
public class AccountCreationScene extends VBox {

    SceneManager sceneManager;
    Controller controller;

    public class AccountCreationTopBar extends HBox {

        private Button cancelButton;

        AccountCreationTopBar() {
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 10, 10, 10));
            this.setSpacing(10);
            this.setStyle("-fx-background-color: #c6ecc6;");

            cancelButton = createStyledButton("Cancel");
            cancelButton.setOnAction(e -> sceneManager.displayLoginScene());

            this.getChildren().add(cancelButton);

            Label title = new Label("Register for account");
            title.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
            this.getChildren().add(title);
        }
    }

    /**
     * Constructs a new AccountCreationScene with the provided scene manager.
     * 
     * @param sceneManager
     */
    AccountCreationScene(SceneManager sceneManager, Controller controller) {
        this.sceneManager = sceneManager;
        this.controller = controller;

        this.setSpacing(5);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setAlignment(Pos.TOP_CENTER);
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #e7ffe6;");
    }

    /**
     * Displays the account creation scene
     */
    public void displayAccountCreationScene() {
        this.getChildren().clear();

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);

        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        Button createAccountButton = createStyledButton("Create Account");
        createAccountButton.setDisable(true);
        createAccountButton.setOnAction(e -> {
            String response = controller.addAccount(usernameField.getText(), passwordField.getText());
            if (response == null) {
                this.getChildren().add(new Label("Username already in use, try another"));
            } else {
                sceneManager.displayRecipeList();
            }
        });

        setTextFieldTriggers(usernameField, createAccountButton, passwordField);
        setTextFieldTriggers(passwordField, createAccountButton, usernameField);

        this.getChildren().addAll(usernameField, passwordField, createAccountButton);

        sceneManager.setCenter(this);
        sceneManager.setTop(new AccountCreationTopBar());
    }

    private void setTextFieldTriggers(TextField textField, Button createAccountButton, TextField otherTextField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            createAccountButton.setDisable(textField.getText().length() == 0 || otherTextField.getText().length() == 0);
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
}