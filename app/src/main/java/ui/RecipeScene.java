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
import backend.RecipeList;

import java.awt.*;
import java.awt.event.*;

import java.util.List;


class RecipeScene extends Label {

    public class RecipeSceneTopBar extends HBox {
        RecipeSceneTopBar(Recipe recipe, SceneController sceneController) {
            Label title = new Label(recipe.getTitle());
            this.getChildren().add(title);
            Button backButton = new Button("Back");
            backButton.setOnAction(e -> {
                sceneController.displayRecipeList(null);
            });
            this.getChildren().add(backButton);
        }
    }

    public class NewlyCreatedRecipeSceneTopBar extends HBox {
        NewlyCreatedRecipeSceneTopBar(Recipe recipe, SceneController sceneController, RecipeList recipeList) {
            Label title = new Label(recipe.getTitle());
            this.getChildren().add(title);
            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(e -> {
                sceneController.displayRecipeList(null);
            });
            this.getChildren().add(cancelButton);

            Button saveButton = new Button("Save");
            saveButton.setOnAction(e -> {
                recipeList.addRecipe(recipe);
                sceneController.displayRecipeList(recipeList);
            });
            this.getChildren().add(saveButton);
        }
    }

    SceneController sceneController;

    RecipeScene(SceneController sceneController) {
        this.sceneController = sceneController;
        //this.setPrefSize(500, 560);
        //this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void displayRecipe(Recipe recipe) {
        this.setText(recipe.getInstructions());
        sceneController.setCenter(this);
        sceneController.setTop(new RecipeSceneTopBar(recipe, sceneController));
    }

    public void displayNewlyCreatedRecipe(Recipe recipe, RecipeList recipeList) {
        this.setText(recipe.getInstructions());
        sceneController.setCenter(this);
        sceneController.setTop(new NewlyCreatedRecipeSceneTopBar(recipe, sceneController, recipeList));
    }
}