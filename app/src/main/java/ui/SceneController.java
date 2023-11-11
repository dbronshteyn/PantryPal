package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.SceneController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;

import backend.RecipeList;

import backend.Recipe;
import backend.Controller;
import backend.RecipeBuilder;

public class SceneController extends BorderPane {

    ListScene listScene;
    RecipeScene recipeScene;
    RecipeCreationScene recipeCreationScene;
    Controller controller;

    public SceneController(Controller controller, File ingredientsAudioFile, File mealTypeAudioFile) {
        this.setStyle("-fx-background-color: #e7ffe6;"); // Setting the background color

        listScene = new ListScene(this, controller);
        recipeScene = new RecipeScene(controller, this);
        recipeCreationScene = new RecipeCreationScene(this, controller.getRecipeList(), ingredientsAudioFile, mealTypeAudioFile);

        this.controller = controller;

        // If you want to show the list scene by default
        this.setCenter(listScene);
    }

    public void displayRecipeDetails(Recipe recipe) {
        recipeScene.displayRecipe(recipe);
    }

    public void displayNewlyCreatedRecipe(Recipe recipe, RecipeList recipeList) {
        recipeScene.displayNewlyCreatedRecipe(recipe, recipeList);
    }

    public void displayRecipeList(RecipeList recipes) {
        listScene.displayRecipeList(recipes);
    }

    public void displayRecipeCreationScene(RecipeBuilder recipeBuilder) {
        recipeCreationScene.displayRecipeCreationScene(recipeBuilder);
    }

    public void saveEdits(Recipe recipe, String newInstructions) {
        this.controller.saveEdits(recipe, newInstructions);
    }

    public void removeRecipe(Recipe recipe) {
        this.controller.removeRecipe(recipe);
    }
}