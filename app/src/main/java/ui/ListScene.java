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
import javafx.scene.control.ScrollPane;

import javax.swing.*;

import backend.Recipe;
import backend.RecipeList;

import java.awt.*;
import java.awt.event.*;

import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// import font
import javafx.scene.text.Font;

public class ListScene extends VBox {

    SceneController sceneController;
    ScrollPane scroller;

    public class RecipeInListUI extends HBox {
        RecipeInListUI(Recipe recipe, SceneController sceneController, RecipeList recipeList) {
            this.setSpacing(10);
            this.setAlignment(Pos.CENTER_LEFT);
            this.setPadding(new Insets(10, 10, 10, 10));

            Label title = new Label(recipe.getTitle());
            title.setFont(new Font("Arial", 16));
            title.setWrapText(true);

            Button detailButton = createStyledButton("View Details");
            detailButton.setOnAction(e -> {
                sceneController.displayRecipeDetails(recipe, recipeList);
            });


            this.getChildren().addAll(title, detailButton);
            this.setStyle("-fx-background-color: #e7ffe6; -fx-border-color: #a3d9a5; -fx-border-width: 0.5;");
        }
    }

    public class ListSceneTopBar extends HBox {
        ListSceneTopBar(SceneController sceneController) {
            this.setAlignment(Pos.CENTER);
            this.setPadding(new Insets(10, 10, 10, 10));
            this.setSpacing(10);

            Label recipesLabel = new Label("Recipes");
            recipesLabel.setFont(new Font("Arial", 20));

            Button newRecipeButton = createStyledButton("New Recipe");
            newRecipeButton.setOnAction(e -> {
                sceneController.displayRecipeCreationScene();
            });

            this.getChildren().addAll(recipesLabel, newRecipeButton);
            this.setStyle("-fx-background-color: #c6ecc6;");
        }
    }

    ListScene(SceneController sceneController) {
        this.sceneController = sceneController;
        this.setSpacing(5);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setPrefSize(500, 560);
        scroller = new ScrollPane(this);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);

        this.setStyle("-fx-background-color: #e7ffe6;");
    }

    public void displayRecipeList(RecipeList recipes) {
        if (recipes != null) {
            this.getChildren().clear();
            for (Recipe recipe : recipes.getRecipes()) {
                RecipeInListUI recipeEntry = new RecipeInListUI(recipe, this.sceneController, recipes);
                this.getChildren().add(recipeEntry);
            }
        }
        sceneController.setCenter(scroller);
        sceneController.setTop(new ListSceneTopBar(this.sceneController));
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