package com.example.planner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = new FXMLLoader(getClass().getResource("/views/auth/Login.fxml")).load();
            Scene scene = new Scene(root);

            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
            primaryStage.getIcons().add(icon);

            primaryStage.setTitle("My Desktop Planner");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
