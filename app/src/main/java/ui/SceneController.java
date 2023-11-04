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


public class SceneController extends BorderPane {

    ListScene listScene;
    RecipeScene recipeScene;

    public SceneController() {
        listScene = new ListScene(this);
        //listScene.displayRecipeList(null);

        recipeScene = new RecipeScene(this);
    }

    public void displayRecipeDetails(Recipe recipe) {
        recipeScene.displayRecipe(recipe);
    }

    public void displayRecipeList(RecipeList recipes) {
        listScene.displayRecipeList(recipes);
    }
}