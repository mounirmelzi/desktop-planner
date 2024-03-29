package com.example.controllers.home;

import com.example.controllers.Controller;
import com.example.controllers.project.InfoProjectPopupController;
import com.example.core.Project;
import com.example.core.User;
import com.example.core.exceptions.UnscheduledException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
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
    private void handleInfoButtonAction(@NotNull ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/project/InfoProjectPopup.fxml"));
            loader.setControllerFactory(param -> new InfoProjectPopupController(project, user));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Project Info");
            stage.setResizable(false);
            stage.setScene(scene);

            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
            stage.getIcons().add(icon);

            Parent root = ((Node)event.getSource()).getScene().getRoot();
            root.setEffect(new GaussianBlur(15));
            stage.showAndWait();
            root.setEffect(null);

            update();
            homeController.updateProjects();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePlanificationButtonAction() {
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
    private void handleDeleteButtonAction() {
        user.deleteProject(project);
        homeController.updateProjects();
    }
}
