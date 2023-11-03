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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.layout.VBox;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class RecipeManager extends VBox {

    private Stage primaryStage;

    RecipeManager(Stage primaryStage) {
        this.setSpacing(5);
        this.setPrefSize(500, 560);
        // this.setStyle("-fx-background-color: #F0F8FF;");
        this.primaryStage = primaryStage;
    }

    public void addRecipe(String title) {
        Recipe recipe = new Recipe(title);
        this.getChildren().add(recipe);
    }
}