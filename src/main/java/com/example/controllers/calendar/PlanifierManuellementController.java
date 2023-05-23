package com.example.controllers.calendar;

import com.example.controllers.Controller;
import com.example.core.Tache;
import com.example.core.User;
import com.example.core.exceptions.UnscheduledException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class PlanifierManuellementController extends Controller implements Initializable {
    @FXML
    private VBox tachesRadioButtonsContainer;
    private final ToggleGroup toggleGroup;

    private final User user;
    private final LocalDate date;
    private final LocalTime time;

    public PlanifierManuellementController(User user, LocalDate date, LocalTime time) {
        this.toggleGroup = new ToggleGroup();
        this.user = user;
        this.date = date;
        this.time = time;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tachesRadioButtonsContainer.getChildren().clear();
        for (Tache tache : user.getTaches().stream().filter(Tache::isUnscheduled).toList()) {
            tachesRadioButtonsContainer.getChildren().add(new TacheRadioButton(tache));
        }
    }

    @FXML
    private void handlePlanifierButtonAction(@NotNull ActionEvent event) {
        TacheRadioButton selected = (TacheRadioButton) toggleGroup.getSelectedToggle();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Planification failed");
            alert.setHeaderText("No Tache Selected");
            alert.setContentText("Vous n'avez pas sélectionner une tache pour la planifier");
            alert.showAndWait();
            return;
        }

        try {
            Tache tache = selected.getTache();
            tache.planifierManuellement(user.getPlanning(), date, time);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (UnscheduledException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Planification error");
            alert.setHeaderText("Taches can't be all scheduled properly");
            alert.setContentText("Veuillez vérifier vos créneau ou le deadline après réessayer");
            alert.showAndWait();
        }
    }


    private class TacheRadioButton extends RadioButton {
        private final Tache tache;

        public TacheRadioButton(@NotNull Tache tache) {
            super(tache.getNom());
            this.tache = tache;
            this.setToggleGroup(toggleGroup);
        }

        public Tache getTache() {
            return tache;
        }
    }
}
