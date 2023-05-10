package com.example.controllers.auth;

import com.example.controllers.Controller;
import com.example.core.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController extends Controller {
    @FXML
    private TextField pseudoTextField;
    @FXML
    private Label messageLabel;

    @FXML
    private void handleRegisterButtonAction() {
        String pseudo = pseudoTextField.getText();

        try {
            User.load(pseudo);
            setErrorMessage("Ce compte existe déjà");
        } catch (Exception e) {
            try {
                if (pseudo == null || pseudo.length() == 0) throw new Exception("Aucun pseudo n'est rempli");

                User user = new User(pseudo);
                user.save();

                setSuccessMessage("La creation du compte est effectuée avec succes");
            } catch (IOException ioException) {
                setErrorMessage("Account creation failed, try again later");
                ioException.printStackTrace();
            } catch (Exception exception) {
                setErrorMessage(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    @FXML
    private void handleLoginButtonAction() {
        try {
            Parent root = new FXMLLoader(getClass().getResource("/views/auth/Login.fxml")).load();
            pseudoTextField.getScene().setRoot(root);
        } catch (IOException e) {
            setErrorMessage(e.getMessage());
            e.printStackTrace();
        }
    }


    private void setErrorMessage(String message) {
        messageLabel.setStyle("-fx-text-fill: #ff375f");
        messageLabel.setText(message);
    }

    private void setSuccessMessage(String message) {
        messageLabel.setStyle("-fx-text-fill: #00c853");
        messageLabel.setText(message);
    }
}
