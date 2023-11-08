package ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import backend.Recipe;
import backend.RecipeList;
import backend.Controller;

class RecipeScene extends VBox {

    Controller controller;
    SceneController sceneController;
    Label instructionsLabel;

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

            this.getChildren().addAll(backButton, title);
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
                controller.saveJSON(recipe);
                sceneController.displayRecipeList(recipeList);
            });

            this.getChildren().addAll(cancelButton, title, saveButton);
        }
    }

    RecipeScene(Controller controller, SceneController sceneController) {
        this.controller = controller;
        this.sceneController = sceneController;
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setStyle("-fx-background-color: #e7ffe6;");

        instructionsLabel = new Label();
        instructionsLabel.setWrapText(true);
        instructionsLabel.setFont(new Font("Arial", 14));

        this.getChildren().add(instructionsLabel);
    }

    public void displayRecipe(Recipe recipe) {
        instructionsLabel.setText(recipe.getInstructions());
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
