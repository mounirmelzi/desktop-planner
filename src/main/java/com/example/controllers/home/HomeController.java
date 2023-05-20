package com.example.controllers.home;

import com.example.controllers.Controller;
import com.example.core.Project;
import com.example.core.Tache;
import com.example.core.User;
import com.example.core.exceptions.UnscheduledException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HomeController extends Controller implements Initializable {
    @FXML
    private Label pseudoLabel, todayDateLabel, planningLabel;
    @FXML
    private HBox tachesContainer, projectsContainer;

    private User user;

    public HomeController(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateInfos();
        updateTaches();
        updateProjects();
    }

    @FXML
    private void planifierAutoButtonAction(ActionEvent event) {
        if (!user.hasPlanning()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Planification failed");
            alert.setHeaderText("You don't have a planning");
            alert.setContentText("Vous devez créer un planning d'abord !");
            alert.showAndWait();
            return;
        }

        try {
            user.planifierAuto();
        } catch (UnscheduledException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Planification error");
            alert.setHeaderText("Taches can't be all scheduled properly");
            alert.setContentText("Il y'a une tache non planifée, veuillez ajuster vos créneau et réessayer");
            alert.show();
        }

        updateTaches();
    }

    @FXML
    private void addTacheButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home/AddTachePopup.fxml"));
            loader.setControllerFactory(param -> new AddTachePopupController(user));

            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Create New Tache");
            stage.setScene(scene);
            stage.showAndWait();

            updateTaches();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addProjetButtonAction(ActionEvent event) {
        // todo ajouter un projet pour un utilisateur
    }

    @FXML
    private void newPlanningButtonAction(ActionEvent event) {
        try {
            if (user.hasPlanning()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Create New Planning failed");
                alert.setHeaderText("You already have a planning");
                alert.setContentText("Vous pouvez éntendre la période de votre planning où l'archiver pour créer un nouveau planning");
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home/NewPlanningPopup.fxml"));
            loader.setControllerFactory(param -> new NewPlanningPopupController(user));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Create New Planning");
            stage.setScene(scene);
            stage.showAndWait();

            updateInfos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void archiverPlanningButtonAction(ActionEvent event) {
        // todo archiver le planning courant
    }

    @FXML
    private void extendPlanningButtonAction(ActionEvent event) {
        try {
            if (!user.hasPlanning()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Extend Planning failed");
                alert.setHeaderText("You don't have a planning");
                alert.setContentText("Vous devez créer un planning d'abord !");
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home/ExtendPlanningPopup.fxml"));
            loader.setControllerFactory(param -> new ExtendPlanningPopupController(user));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Extend Planning");
            stage.setScene(scene);
            stage.showAndWait();

            updateInfos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addCreneauLibreButtonAction(ActionEvent event) {
        // todo ajouter creneau libre pour un ensemble des journées
    }

    private void updateInfos() {
        pseudoLabel.setText(user.getPseudo());
        todayDateLabel.setText(LocalDate.now().toString().replace('-', '/'));

        if (user.getPlanning() == null) {
            planningLabel.setTextFill(Color.RED);
            planningLabel.setText("You don't have planning !! Create One ...");
        } else {
            planningLabel.setTextFill(Color.BLACK);
            planningLabel.setText("Votre planning courant est du " + user.getPlanning().getDateDebut().toString().replace('-', '/') + " jusqu'à " + user.getPlanning().getDateFin().toString().replace('-', '/'));
        }
    }

    void updateTaches() {
        tachesContainer.getChildren().clear();

        for (Tache tache : user.getTaches()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home/TacheItem.fxml"));
            loader.setControllerFactory(param -> new TacheItemController(user, tache, this));
            try {
                tachesContainer.getChildren().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void updateProjects() {
        projectsContainer.getChildren().clear();

        for (Project project : user.getProjects()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home/ProjectItem.fxml"));
            loader.setControllerFactory(param -> new ProjectItemController(user, project, this));
            try {
                projectsContainer.getChildren().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
