package com.example.controllers.home;

import com.example.controllers.Controller;
import com.example.core.User;
import com.example.core.exceptions.InvalidDateTimeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class NewPlanningPopupController extends Controller implements Initializable {
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    private final User user;

    public NewPlanningPopupController(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startDatePicker.setValue(LocalDate.now());
    }

    @FXML
    private void handleCreatePlanningButtonAction(@NotNull ActionEvent event) {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        try {
            user.createPlanning(startDate, endDate);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (InvalidDateTimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Planning creation failed");
            alert.setHeaderText("You choosed invalid date for your planning");
            alert.setContentText("Vous ne pouvez pas creer un planning avant la date d'aujourd'hui");
            alert.show();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Planning creation failed");
            alert.setHeaderText("You choosed invalid date for your planning");
            alert.setContentText("Vous devez choisir une date début et une date fin pour votre planning");
            alert.show();
        }
    }
}
