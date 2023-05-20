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

public class ExtendPlanningPopupController extends Controller implements Initializable {
    @FXML
    private DatePicker newEndDatePicker;

    private final User user;

    public ExtendPlanningPopupController(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newEndDatePicker.setValue(user.getPlanning().getDateFin());
    }

    @FXML
    private void handleExtendPlanningButtonAction(@NotNull ActionEvent event) {
        LocalDate newEndDate = newEndDatePicker.getValue();

        try {
            user.extendPlanning(newEndDate);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (InvalidDateTimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Extend planning failed");
            alert.setHeaderText("You choosed invalid date for your planning");
            alert.setContentText("La nouvelle date fin du planning est avant la date fin courante !");
            alert.show();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Extend planning failed");
            alert.setHeaderText("You choosed invalid date for your planning");
            alert.setContentText("Vous devez choisir la nouvelle date fin pour votre planning");
            alert.show();
        }
    }
}
