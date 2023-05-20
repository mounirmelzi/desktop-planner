package com.example.controllers.home;

import com.example.controllers.Controller;
import com.example.core.Tache;
import com.example.core.User;
import com.example.core.exceptions.UnscheduledException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class TacheItemController extends Controller implements Initializable {
    @FXML
    private Label titleLabel, categoryLabel;
    @FXML
    private Button planificationButton;
    @FXML
    private GridPane root;

    private final User user;
    private final Tache tache;
    private final HomeController homeController;

    public TacheItemController(User user, Tache tache, HomeController homeController) {
        this.user = user;
        this.tache = tache;
        this.homeController = homeController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        update();
    }

    private void update() {
        root.setStyle(root.getStyle() + "-fx-bbackground-color: " + tache.getColor() + ";");
        titleLabel.setText(tache.getNom());
        categoryLabel.setText(tache.getCategory() != null ? tache.getCategory().getName() : "No category");
        planificationButton.setText(tache.isUnscheduled() ? "Planifier" : "Deplanifier");
    }

    @FXML
    private void handleInfoButtonAction(ActionEvent event) {
        // todo show information of a task
    }

    @FXML
    private void handlePlanificationButtonAction(ActionEvent event) {
        if (tache.isUnscheduled()) {
            if (!user.hasPlanning()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Planification failed");
                alert.setHeaderText("You don't have a planning");
                alert.setContentText("Vous devez créer un planning d'abord !");
                alert.showAndWait();
                return;
            }

            try {
                tache.planifier(user.getPlanning(), LocalDateTime.now());
                update();
            } catch (UnscheduledException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Planification failed");
                alert.setHeaderText("Tache can't be scheduled properly");
                alert.setContentText("La planification automatique de catte tache n'est pas possible, veuillez ajuster vos créneau et réessayer");
                alert.show();
            }
        } else {
            tache.deplanifier(user.getPlanning());
            update();
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        user.deleteTache(tache);
        homeController.updateTaches();
    }
}
