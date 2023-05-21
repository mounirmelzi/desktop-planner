package com.example.controllers.home;

import com.example.controllers.Controller;
import com.example.core.Project;
import com.example.core.User;
import com.example.core.exceptions.UnscheduledException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ProjectItemController extends Controller implements Initializable {
    @FXML
    private Label titleLabel, descriptionLabel;
    @FXML
    private Button planificationButton;

    private final User user;
    private final Project project;
    private final HomeController homeController;

    public ProjectItemController(User user, Project project, HomeController homeController) {
        this.user = user;
        this.project = project;
        this.homeController = homeController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        update();
    }

    private void update() {
        titleLabel.setText(project.getNom());
        descriptionLabel.setText(project.getDescription());
        if (!project.hasTaches())
            planificationButton.setText("No Tasks");
        else
            planificationButton.setText(project.isUnscheduled() ? "Planifier" : "Deplanifier");
    }

    @FXML
    private void handleInfoButtonAction(ActionEvent event) {
        // todo show info of project
    }

    @FXML
    private void handlePlanificationButtonAction(ActionEvent event) {
        if (project.isUnscheduled()) {
            if (!user.hasPlanning()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Planification failed");
                alert.setHeaderText("You don't have a planning");
                alert.setContentText("Vous devez créer un planning d'abord !");
                alert.showAndWait();
                return;
            }

            try {
                project.planifier(user.getPlanning(), LocalDateTime.now());
                update();
            } catch (UnscheduledException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Planification failed");
                alert.setHeaderText("Project can't be scheduled properly");
                alert.setContentText("La planification automatique du projet n'est pas possible, veuillez ajuster vos créneau et réessayer");
                alert.showAndWait();
            }
        } else {
            project.deplanifier(user.getPlanning());
            update();
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        user.deleteProgect(project);
        homeController.updateProjects();
    }
}
