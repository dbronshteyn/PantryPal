package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.control.TextField;

import middleware.Controller;
import ui.ListScene.ListSceneTopBar;
import ui.ListScene.RecipeInListUI;
public class LoginScene extends VBox {
    
    SceneManager sceneManager;
    private Label statusLabel = new Label("");



    public class LoginSceneTopBar extends HBox {

        private Button cancelButton;
        private Button createAccountButton;

        LoginSceneTopBar() {
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 10, 10, 10));
            this.setSpacing(10);
            this.setStyle("-fx-background-color: #c6ecc6;");

            cancelButton = createStyledButton("Cancel");
            cancelButton.setOnAction(e -> sceneManager.displayRecipeList());
            createAccountButton = createStyledButton("Create Account");
            createAccountButton.setOnAction(e -> sceneManager.displayAccountCreationScene());
            this.getChildren().addAll(cancelButton, createAccountButton);

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
    LoginScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.setSpacing(5);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setAlignment(Pos.TOP_CENTER);
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #e7ffe6;");
    }

    /**
     * Displays the account creation scene
     */
    public void displayLoginScene() {
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
            Boolean response = Controller.login(usernameField.getText(), passwordField.getText());
            if (response == true) {
                statusLabel.setText("Login successful");
                if (autoLogin.isSelected()) {
                    // Implement automatic login
                    System.out.println("Automatic login set");
                }
                sceneManager.displayRecipeList();
            } else {
                statusLabel.setText("Login unsuccessful, try again or create an account");
            }
        });
        this.getChildren().add(statusLabel);

        setTextFieldTriggers(usernameField, loginButton, passwordField);
        setTextFieldTriggers(passwordField, loginButton, usernameField);

        this.getChildren().addAll(usernameField, passwordField, autoLogin, loginButton);

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
}
