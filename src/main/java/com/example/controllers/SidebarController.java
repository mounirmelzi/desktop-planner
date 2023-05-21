package com.example.controllers;

import com.example.controllers.calendar.CalendarController;
import com.example.controllers.home.HomeController;
import com.example.core.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SidebarController extends Controller implements Initializable {
    private static final double WINDOW_WIDTH = 1080;
    private static final double WINDOW_HEIGHT = 680;
    @FXML
    private BorderPane root;
    @FXML
    private BorderPane menu;
    @FXML
    private AnchorPane pageContent;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Label usernameLabel;
    private User user;

    public SidebarController(User user) {
        this.user = user;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.setPrefWidth(WINDOW_WIDTH);
        root.setPrefHeight(WINDOW_HEIGHT);

        menu.setPrefHeight(WINDOW_HEIGHT);
        pageContent.setPrefHeight(WINDOW_HEIGHT);

        logoImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png"))));

        usernameLabel.setText(user.getPseudo());

        showPage("/views/home/Home.fxml", new HomeController(user));
    }


    private void showPage(String fxmlFilePath, Controller controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            if (controller != null) {
                loader.setControllerFactory(param -> controller);
            }

            Parent page = loader.load();
            pageContent.getChildren().clear();
            pageContent.getChildren().add(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPage(String fxmlFilePath) {
        showPage(fxmlFilePath, null);
    }

    @FXML
    private void handleHome(ActionEvent event) {
        showPage("/views/home/Home.fxml", new HomeController(user));
    }

    @FXML
    private void handleCalendar(ActionEvent event) {
        showPage("/views/calendar/Calendar.fxml", new CalendarController(user));
    }

    @FXML
    private void handleLogout(@NotNull ActionEvent event) {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            user.save();

            Parent root = new FXMLLoader(getClass().getResource("/views/auth/Login.fxml")).load();
            Scene scene = new Scene(root);

            primaryStage.setResizable(false);
            primaryStage.setMaximized(false);
            primaryStage.setScene(scene);

            // center the stage in the middle of the screen
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX(screenBounds.getMinX() + (screenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY(screenBounds.getMinY() + (screenBounds.getHeight() - primaryStage.getHeight()) / 2);

            // remove the alert message
            primaryStage.setOnCloseRequest(windowEvent -> Platform.exit());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
