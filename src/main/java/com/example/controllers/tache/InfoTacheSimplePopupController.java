package com.example.controllers.tache;

import com.example.controllers.Controller;
import com.example.core.Project;
import com.example.core.TacheDecomposable;
import com.example.core.TacheSimple;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class InfoTacheSimplePopupController extends Controller implements Initializable {
    @FXML
    private Label nameLabel, durationLabel, priorityLabel, deadlineLabel, categoryLabel, stateLabel, periodicityLabel;
    @FXML
    private Button editButton;

    private final TacheSimple tache;
    private final TacheDecomposable parentTache;
    private final Project projectOfTache;
    private final Boolean isEditable;

    public InfoTacheSimplePopupController(TacheSimple tache, boolean isEditable, TacheDecomposable parentTache, Project projectOfTache) {
        this.tache = tache;
        this.parentTache = parentTache;
        this.projectOfTache = projectOfTache;
        this.isEditable = isEditable;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        update();
    }

    private void update() {
        editButton.setDisable(!isEditable);
        nameLabel.setText(tache.getNom());
        durationLabel.setText(String.format("%s Days %s Hours %s Minutes %s Seconds",
                tache.getDuree().toDaysPart(),
                tache.getDuree().toHoursPart(),
                tache.getDuree().toMinutesPart(),
                tache.getDuree().toSecondsPart()
        ));
        priorityLabel.setText(tache.getPriority().getName());
        deadlineLabel.setText(tache.getDeadline().getDayOfWeek() + " " + tache.getDeadline().format(DateTimeFormatter.ISO_DATE_TIME).replace("T", " at "));
        categoryLabel.setText(tache.getCategory() == null ? "No Category" : tache.getCategory().getName());
        stateLabel.setText(tache.getState().getName());
        periodicityLabel.setText(tache.getPeriodicity() + " Days");
    }

    @FXML
    private void handleEditButtonAction(ActionEvent event) {
        // todo edit tache simple

        update();
    }
}
