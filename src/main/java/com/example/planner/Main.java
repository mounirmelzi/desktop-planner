package com.example.planner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = new FXMLLoader(getClass().getResource("/views/auth/Login.fxml")).load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("My Desktop Planner");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
