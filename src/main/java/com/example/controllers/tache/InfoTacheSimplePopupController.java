package com.example.controllers.tache;

import com.example.controllers.Controller;
import com.example.core.Project;
import com.example.core.TacheDecomposable;
import com.example.core.TacheSimple;
import com.example.core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class InfoTacheSimplePopupController extends Controller implements Initializable {
    @FXML
    private Label nameLabel, durationLabel, priorityLabel, deadlineLabel, categoryLabel, stateLabel, periodicityLabel;

    private final TacheSimple tache;
    private final User user;
    private final TacheDecomposable parentTache;
    private final Project projectOfTache;

    public InfoTacheSimplePopupController(TacheSimple tache, User user, TacheDecomposable parentTache, Project projectOfTache) {
        this.tache = tache;
        this.user = user;
        this.parentTache = parentTache;
        this.projectOfTache = projectOfTache;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        update();
    }

    private void update() {
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
    private void handleEditButtonAction(@NotNull ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tache/EditTacheSimplePopup.fxml"));
            loader.setControllerFactory(param -> new EditTacheSimplePopupController(tache, user, parentTache, projectOfTache));
            stage.setTitle("Edit Tache Simple Information");

            Scene scene = new Scene(loader.load());
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();

            update();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
