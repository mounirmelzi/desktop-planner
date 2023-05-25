package com.example.controllers.profile;

import com.example.controllers.Controller;
import com.example.core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class AddCategoryPopupController extends Controller {
    @FXML
    private TextField nameTextField;
    @FXML
    private ColorPicker colorPicker;

    private final User user;

    public AddCategoryPopupController(User user) {
        this.user = user;
    }

    @FXML
    private void handleAddButtonAction(@NotNull ActionEvent event) {
        String name = nameTextField.getText();
        Color _color = colorPicker.getValue();
        String color = (_color == null) ? null : "#" + _color.toString().substring(2, 8);

        user.addCategory(name, color);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
