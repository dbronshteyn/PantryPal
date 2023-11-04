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

import java.awt.*;
import java.awt.event.*;

import java.util.List;


public class ListScene extends VBox {

    SceneController sceneController;
    ScrollPane scroller;

    public class RecipeInListUI extends HBox {
        RecipeInListUI(Recipe recipe, SceneController sceneController) {
            //this.setPrefSize(800, 20);
            //this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
            Label title = new Label(recipe.getTitle());
            this.getChildren().add(title);
            Button detailButton = new Button("View Details");
            detailButton.setOnAction(e -> {
                sceneController.displayRecipeDetails(recipe);
            });
            this.getChildren().add(detailButton);
        }
    }

    ListScene(SceneController sceneController) {
        this.sceneController = sceneController;
        this.setSpacing(5);
        this.setPrefSize(500, 560);
        scroller = new ScrollPane(this);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);
        //this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void displayRecipeList(List<Recipe> recipes) {
        if (recipes != null) {
            this.getChildren().clear();
            for (Recipe recipe : recipes) {
                this.getChildren().add(new RecipeInListUI(recipe, this.sceneController));
            }
        }
        sceneController.setCenter(scroller);
        sceneController.setTop(new Label("Recipes"));
    }
}