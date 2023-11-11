package ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.control.TextArea;

import backend.Controller;

class RecipeScene extends ScrollPane {

    Controller controller;
    SceneManager sceneManager;
    Label instructionsLabel;
    TextArea instructionsTextArea;

    public class RecipeSceneTopBar extends HBox {
        RecipeSceneTopBar(String recipeID) {
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 10, 10, 10));
            this.setSpacing(10);
            this.setStyle("-fx-background-color: #c6ecc6;");

            Label title = new Label(controller.getRecipeTitle(recipeID));
            title.setFont(new Font(SceneManager.FONT, 20));

            Button backButton = createStyledButton("Back");
            backButton.setOnAction(e -> sceneManager.displayRecipeList());   
          
            Button editButton = createStyledButton("Edit");
            editButton.setOnAction(e -> displayRecipeEditScene(recipeID));

            Button deleteButton = createStyledButton("Delete");
            deleteButton.setOnAction(e -> {
                controller.removeRecipe(recipeID);
                sceneManager.displayRecipeList();
            });

            this.getChildren().addAll(backButton, title, editButton, deleteButton);
        }
    }

    public class NewlyCreatedRecipeSceneTopBar extends HBox {
        NewlyCreatedRecipeSceneTopBar(String recipeID) {
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 10, 10, 10));
            this.setSpacing(10);
            this.setStyle("-fx-background-color: #c6ecc6;");

            Label title = new Label(controller.getRecipeTitle(recipeID));
            title.setFont(new Font(SceneManager.FONT, 20));

            Button cancelButton = createStyledButton("Cancel");
            cancelButton.setOnAction(e -> sceneManager.displayRecipeList());

            Button saveButton = createStyledButton("Save");
            saveButton.setOnAction(e -> {
                controller.saveRecipe(recipeID);
                sceneManager.displayRecipeList();
            });

            this.getChildren().addAll(cancelButton, title, saveButton);
        }
    }

    public class RecipeEditTopBar extends HBox {
        RecipeEditTopBar(String recipeID) {
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 10, 10, 10));
            this.setSpacing(10);
            this.setStyle("-fx-background-color: #c6ecc6;");

            Label title = new Label(controller.getRecipeTitle(recipeID));
            title.setFont(new Font(SceneManager.FONT, 20));

            Button cancelButton = createStyledButton("Cancel");
            cancelButton.setOnAction(e -> sceneManager.displayRecipeList());

            Button saveButton = createStyledButton("Save Edits");
            saveButton.setOnAction(e -> {
                controller.editRecipe(recipeID, instructionsTextArea.getText());
                sceneManager.displayRecipeDetails(recipeID);
            });

            this.getChildren().addAll(cancelButton, title, saveButton);
        }
    }

    RecipeScene(SceneManager sceneManager, Controller controller) {
        this.sceneManager = sceneManager;
        this.controller = controller;
        
        instructionsLabel = new Label();
        instructionsLabel.setWrapText(true);
        instructionsLabel.setFont(new Font(SceneManager.FONT, 14));
        instructionsLabel.setStyle("-fx-background-color: #e7ffe6;");
        this.setFitToWidth(true);
        this.setContent(instructionsLabel);
        this.setPadding(new Insets(30, 30, 30, 30));
        this.setStyle("-fx-background: #e7ffe6;");

        instructionsTextArea = new TextArea();
        instructionsTextArea.setPrefHeight(450);
    }

    public void displayRecipeEditScene(String recipeID) {
        this.displayRecipeDetails(recipeID);
        this.instructionsTextArea.setText(controller.getRecipeInstructions(recipeID));
        this.instructionsTextArea.setWrapText(true);
        this.setContent(instructionsTextArea);
        sceneManager.setCenter(this);
        sceneManager.setTop(new RecipeEditTopBar(recipeID));
    }

    public void displayRecipeDetails(String recipeID) {
        instructionsLabel.setText(controller.getRecipeInstructions(recipeID));
        this.setContent(instructionsLabel);
        sceneManager.setCenter(this);
        sceneManager.setTop(new RecipeSceneTopBar(recipeID));
    }

    public void displayNewlyCreatedRecipe(String recipeID) {
        instructionsLabel.setText(controller.getRecipeInstructions(recipeID));
        sceneManager.setCenter(this);
        sceneManager.setTop(new NewlyCreatedRecipeSceneTopBar(recipeID));
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #a3d9a5; -fx-text-fill: #000000;");
        button.setFont(new Font(SceneManager.FONT, 14));
        button.setPadding(new Insets(5, 15, 5, 15));

        // Hover effect
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #8cc68c; -fx-text-fill: #000000;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #a3d9a5; -fx-text-fill: #000000;"));

        return button;
    }
}
