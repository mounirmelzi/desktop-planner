package com.example.controllers.home;

import com.example.controllers.Controller;
import com.example.controllers.tache.InfoTacheDecomposablePopupController;
import com.example.controllers.tache.InfoTacheSimplePopupController;
import com.example.core.Tache;
import com.example.core.TacheDecomposable;
import com.example.core.TacheSimple;
import com.example.core.User;
import com.example.core.exceptions.UnscheduledException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
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
        root.setStyle(root.getStyle() + "-fx-background-color: " + tache.getColor() + ";");
        titleLabel.setText(tache.getNom());
        categoryLabel.setText(tache.getCategory() != null ? tache.getCategory().getName() : "No category");
        planificationButton.setText(tache.isUnscheduled() ? "Planifier" : "Deplanifier");
    }

    @FXML
    private void handleInfoButtonAction() {
        try {
            FXMLLoader loader;
            if (tache instanceof TacheSimple) {
                loader = new FXMLLoader(getClass().getResource("/views/tache/InfoTacheSimplePopup.fxml"));
                loader.setControllerFactory(param -> new InfoTacheSimplePopupController((TacheSimple) tache, user, null, null));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle("Tache Simple Info");
                stage.setResizable(false);
                stage.setScene(scene);
                stage.showAndWait();
            } else {
                loader = new FXMLLoader(getClass().getResource("/views/tache/InfoTacheDecomposablePopup.fxml"));
                loader.setControllerFactory(param -> new InfoTacheDecomposablePopupController((TacheDecomposable) tache, user, null));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle("Tache Decomposable Info");
                stage.setResizable(false);
                stage.setScene(scene);
                stage.showAndWait();
            }

            update();
            homeController.updateTaches();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePlanificationButtonAction() {
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
                alert.showAndWait();
            }
        } else {
            tache.deplanifier(user.getPlanning());
            update();
        }
    }

    @FXML
    private void handleDeleteButtonAction() {
        user.deleteTache(tache);
        homeController.updateTaches();
    }
}
