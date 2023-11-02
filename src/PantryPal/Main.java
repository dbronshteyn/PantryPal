package src.PantryPal;

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


class AppFrame extends BorderPane {

    private RecipeManager recipeManager;

    AppFrame(Stage primaryStage) {
        recipeManager = new RecipeManager(primaryStage);
        ScrollPane scroller = new ScrollPane(recipeManager);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);
        recipeManager.addRecipe("recipe 1");
        recipeManager.addRecipe("recipe 2");
        recipeManager.addRecipe("recipe 3");
        this.setCenter(scroller);
    }
}

public class Main extends Application {

    static final int WINDOW_WIDTH = 950;
    static final int WINDOW_HEIGHT = 600;


    @Override
    public void start(Stage primaryStage) throws Exception {
        AppFrame root = new AppFrame(primaryStage);
        primaryStage.setTitle("PantryPal");
        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        //primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
