package com.example.controllers.profile;

import com.example.controllers.Controller;
import com.example.core.User;
import javafx.fxml.Initializable;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController extends Controller implements Initializable {
    private final User user;

    public ProfileController(@NotNull User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
