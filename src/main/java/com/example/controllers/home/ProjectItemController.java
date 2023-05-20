package com.example.controllers.home;

import com.example.controllers.Controller;
import com.example.core.Project;
import com.example.core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
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
        planificationButton.setText(project.isUnscheduled() ? "Planifier" : "Deplanifier");
    }

    @FXML
    private void handleInfoButtonAction(ActionEvent event) {
        // todo show info of project
    }

    @FXML
    private void handlePlanificationButtonAction(ActionEvent event) {
        // todo planifier/deplanifier project
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        // todo delete project
    }
}
