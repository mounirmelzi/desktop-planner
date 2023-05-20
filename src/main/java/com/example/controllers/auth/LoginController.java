package com.example.controllers.auth;

import com.example.controllers.Controller;
import com.example.controllers.SidebarController;
import com.example.core.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginController extends Controller {
    @FXML
    private TextField pseudoTextField;
    @FXML
    private Label errorLabel;

    @FXML
    private void handleLoginButtonAction() {
        String pseudo = pseudoTextField.getText();

        try {
            User user = User.load(pseudo);
            if (user == null) throw new Exception();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Sidebar.fxml"));
            loader.setControllerFactory(param -> new SidebarController(user));

            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) pseudoTextField.getScene().getWindow();
            primaryStage.setResizable(true);
            primaryStage.setScene(scene);

            // center the stage in the middle of the screen
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX(screenBounds.getMinX() + (screenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY(screenBounds.getMinY() + (screenBounds.getHeight() - primaryStage.getHeight()) / 2);

            // add alert when user click exit button after logging in
            primaryStage.setOnCloseRequest(windowEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Logout");
                alert.setHeaderText("Do you want to logout?");
                Optional<ButtonType> buttonType = alert.showAndWait();

                if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
                    try {
                        user.save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        Platform.exit();
                    }
                } else {
                    windowEvent.consume();
                }
            });
        } catch (Exception e) {
            errorLabel.setText("L'utilisateur n'existe pas");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegisterButtonAction() {
        try {
            Parent root = new FXMLLoader(getClass().getResource("/views/auth/Register.fxml")).load();
            pseudoTextField.getScene().setRoot(root);
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePseudoTextFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLoginButtonAction();
        }
    }
}
