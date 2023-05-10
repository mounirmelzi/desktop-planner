package com.example.controllers;

import com.example.core.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CalendarController extends Controller implements Initializable {
    @FXML
    private Label myLabel;

    private User user;

    public CalendarController(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myLabel.setText(user.getPseudo());
    }
}
