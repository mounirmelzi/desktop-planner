package com.example.controllers.home;

import com.example.controllers.Controller;
import com.example.controllers.project.AddProjectPopupController;
import com.example.controllers.tache.AddTachePopupController;
import com.example.core.Project;
import com.example.core.Tache;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeController extends Controller implements Initializable {
    @FXML
    private Label pseudoLabel, todayDateLabel, planningLabel;
    @FXML
    private HBox tachesContainer, projectsContainer;

    private final User user;

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
    private void replanifierButtonAction() {
        if (!user.hasPlanning()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Planification failed");
            alert.setHeaderText("You don't have a planning");
            alert.setContentText("Vous devez créer un planning d'abord !");
            alert.showAndWait();
            return;
        }

        try {
            user.replanifier();
        } catch (UnscheduledException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Planification error");
            alert.setHeaderText("Taches can't be all scheduled properly");
            alert.setContentText("Il y'a une tache non planifée, veuillez ajuster vos créneau et réessayer");
            alert.showAndWait();
        }

        updateTaches();
    }

    @FXML
    private void planifierAutoButtonAction() {
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
            alert.showAndWait();
        }

        updateTaches();
    }

    @FXML
    private void addTacheButtonAction(@NotNull ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tache/AddTachePopup.fxml"));
            loader.setControllerFactory(param -> new AddTachePopupController(user));

            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Create New Tache");
            stage.setResizable(false);
            stage.setScene(scene);

            Parent root = ((Node)event.getSource()).getScene().getRoot();
            root.setEffect(new GaussianBlur(15));
            stage.showAndWait();
            root.setEffect(null);

            updateTaches();
            updateProjects();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addProjetButtonAction(@NotNull ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/project/AddProjectPopup.fxml"));
            loader.setControllerFactory(param -> new AddProjectPopupController(user));

            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Create New Project");
            stage.setScene(scene);

            Parent root = ((Node)event.getSource()).getScene().getRoot();
            root.setEffect(new GaussianBlur(15));
            stage.showAndWait();
            root.setEffect(null);

            updateProjects();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Create New Planning");
            stage.setResizable(false);
            stage.setScene(scene);

            Parent root = ((Node)event.getSource()).getScene().getRoot();
            root.setEffect(new GaussianBlur(15));
            stage.showAndWait();
            root.setEffect(null);

            updateInfos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void archiverPlanningButtonAction() {
        if (!user.hasPlanning()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Archiving Current Planning Failed");
            alert.setHeaderText("You don't have a planning");
            alert.setContentText("Vous devez créer un planning d'abord !");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Archiving Current Planning");
        confirmAlert.setHeaderText("Do you want to archive your current planning ?");
        confirmAlert.setContentText("Le planning courant sera sauvegardé dans l'historique, et supprimé plus tard après la déplanification de tous vos taches et projets");
        Optional<ButtonType> buttonType = confirmAlert.showAndWait();

        if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
            user.archiverCurrentPlanning();
            updateInfos();
            updateTaches();
            updateProjects();
        }
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

            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Extend Planning");
            stage.setResizable(false);
            stage.setScene(scene);

            Parent root = ((Node)event.getSource()).getScene().getRoot();
            root.setEffect(new GaussianBlur(15));
            stage.showAndWait();
            root.setEffect(null);

            updateInfos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addCreneauLibreButtonAction(ActionEvent event) {
        try {
            if (!user.hasPlanning()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Create Creneaux Libres failed");
                alert.setHeaderText("You don't have a planning");
                alert.setContentText("Vous devez créer un planning d'abord !");
                alert.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home/AddCreneauxLibresPopup.fxml"));
            loader.setControllerFactory(param -> new AddCreneauxLibresPopupController(user.getPlanning()));

            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Creneaux Libres");
            stage.setResizable(false);

            Parent root = ((Node)event.getSource()).getScene().getRoot();
            root.setEffect(new GaussianBlur(15));
            stage.showAndWait();
            root.setEffect(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
