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
import backend.Recipe;
import backend.RecipeList;
import backend.Controller;

class RecipeScene extends ScrollPane {

    Controller controller;
    SceneController sceneController;
    Label instructionsLabel;
    TextArea instructionsTextArea;

    public class RecipeSceneTopBar extends HBox {
        RecipeSceneTopBar(Recipe recipe) {
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 10, 10, 10));
            this.setSpacing(10);
            this.setStyle("-fx-background-color: #c6ecc6;");

            Label title = new Label(recipe.getTitle());
            title.setFont(new Font("Arial", 20));

            Button backButton = createStyledButton("Back");
            backButton.setOnAction(e -> sceneController.displayRecipeList(null));   
          
            Button editButton = createStyledButton("Edit");
            editButton.setOnAction(e -> displayRecipeEditScene(recipe));

            Button deleteButton = createStyledButton("Delete");
            deleteButton.setOnAction(e -> {
                sceneController.removeRecipe(recipe);
                sceneController.displayRecipeList(null);
            });

            this.getChildren().addAll(backButton, title, editButton, deleteButton);
        }
    }

    public class NewlyCreatedRecipeSceneTopBar extends HBox {
        NewlyCreatedRecipeSceneTopBar(Recipe recipe, Controller controller, SceneController sceneController, RecipeList recipeList) {
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 10, 10, 10));
            this.setSpacing(10);
            this.setStyle("-fx-background-color: #c6ecc6;");

            Label title = new Label(recipe.getTitle());
            title.setFont(new Font("Arial", 20));

            Button cancelButton = createStyledButton("Cancel");
            cancelButton.setOnAction(e -> sceneController.displayRecipeList(null));

            Button saveButton = createStyledButton("Save");
            saveButton.setOnAction(e -> {
                recipeList.addRecipe(recipe);
                sceneController.displayRecipeList(recipeList);
            });

            Button editButton = createStyledButton("Edit");
            editButton.setOnAction(e -> {
                recipeList.addRecipe(recipe);
                sceneController.displayRecipeList(recipeList);
                displayRecipeEditScene(recipe);
            });

            this.getChildren().addAll(cancelButton, title, saveButton, editButton);
        }
    }

    public class RecipeEditTopBar extends HBox {
        RecipeEditTopBar(Recipe recipe, SceneController sceneController) {
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 10, 10, 10));
            this.setSpacing(10);
            this.setStyle("-fx-background-color: #c6ecc6;");

            Label title = new Label(recipe.getTitle());
            title.setFont(new Font("Arial", 20));

            Button cancelButton = createStyledButton("Cancel");
            cancelButton.setOnAction(e -> sceneController.displayRecipeList(null));

            Button saveButton = createStyledButton("Save Edits");
            saveButton.setOnAction(e -> {
                sceneController.saveEdits(recipe, instructionsTextArea.getText());
            });

            this.getChildren().addAll(cancelButton, title, saveButton);
        }
    }

    RecipeScene(Controller controller, SceneController sceneController) {
        this.controller = controller;
        this.sceneController = sceneController;
        
        instructionsLabel = new Label();
        instructionsLabel.setWrapText(true);
        instructionsLabel.setFont(new Font("Arial", 14));
        instructionsLabel.setStyle("-fx-background-color: #e7ffe6;");
        this.setFitToWidth(true);
        this.setContent(instructionsLabel);
        this.setPadding(new Insets(30, 30, 30, 30));
        this.setStyle("-fx-background: #e7ffe6;");

        instructionsTextArea = new TextArea();
        instructionsTextArea.setPrefHeight(450);
    }

    public void displayRecipeEditScene(Recipe recipe) {
        this.displayRecipe(recipe);
        this.instructionsTextArea.setText(recipe.getInstructions());
        this.instructionsTextArea.setWrapText(true);
        this.setContent(instructionsTextArea);
        sceneController.setCenter(this);
        sceneController.setTop(new RecipeEditTopBar(recipe, sceneController));
    }

    public void displayRecipe(Recipe recipe) {
        instructionsLabel.setText(recipe.getInstructions());
        this.setContent(instructionsLabel);
        sceneController.setCenter(this);
        sceneController.setTop(new RecipeSceneTopBar(recipe));
    }

    public void displayNewlyCreatedRecipe(Recipe recipe, RecipeList recipeList) {
        instructionsLabel.setText(recipe.getInstructions());
        sceneController.setCenter(this);
        sceneController.setTop(new NewlyCreatedRecipeSceneTopBar(recipe, controller, sceneController, recipeList));
    }

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
