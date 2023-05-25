package com.example.controllers.tache;

import com.example.controllers.Controller;
import com.example.core.Project;
import com.example.core.TacheDecomposable;
import com.example.core.TacheSimple;
import com.example.core.User;
import com.example.core.exceptions.DecompositionImpossibleException;
import com.example.core.utils.Pair;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class InfoTacheDecomposablePopupController extends Controller implements Initializable {
    @FXML
    private Label nameLabel, durationLabel, priorityLabel, deadlineLabel, categoryLabel, stateLabel;
    @FXML
    private VBox childrenContainer;

    private final TacheDecomposable tache;
    private final User user;
    private final Project projectOfTache;

    public InfoTacheDecomposablePopupController(TacheDecomposable tache, User user, Project projectOfTache) {
        this.tache = tache;
        this.user = user;
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

        childrenContainer.getChildren().clear();
        childrenContainer.setPadding(new Insets(5));
        for (TacheSimple tacheSimple : tache.getChildren()) {
            SubTacheCard subTacheCard = new SubTacheCard(tacheSimple);
            childrenContainer.getChildren().add(subTacheCard);
        }
    }

    @FXML
    private void handleEditButtonAction(@NotNull ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tache/EditTacheDecomposablePopup.fxml"));
            loader.setControllerFactory(param -> new EditTacheDecomposablePopupController(tache, user, projectOfTache));
            stage.setTitle("Edit Tache Décomposable Information");

            Scene scene = new Scene(loader.load());
            stage.setResizable(false);
            stage.setScene(scene);

            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
            stage.getIcons().add(icon);

            Parent root = ((Node)event.getSource()).getScene().getRoot();
            root.setEffect(new GaussianBlur(15));
            stage.showAndWait();
            root.setEffect(null);

            update();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDecomposerButtonAction() {
        if (!user.hasPlanning()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Decomposition failed");
            alert.setHeaderText("You don't have a planning");
            alert.setContentText("Vous devez créer un planning d'abord !");
            alert.showAndWait();
            return;
        }

        try {
            tache.setChildren(tache.decomposer(new Pair<>(user.getPlanning(), null)));
            update();
        } catch (DecompositionImpossibleException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Décomposition failed");
            alert.setHeaderText("You can't  decompose this tache");
            if (tache.hasChildren()) {
                alert.setContentText("Cette tache est déjà décomposée");
            } else if (!tache.isUnscheduled()) {
                alert.setContentText("Cette tache est planifiée! vous ne pouvez pas la décomposer");
            } else {
                alert.setContentText("Vous devez ajuster vos créneaux de votre planning courant d'abord !");
            }

            alert.showAndWait();
        }
    }

    @FXML
    private void handleClearButtonAction() {
        tache.clear(user.getPlanning());
        update();
    }


    private class SubTacheCard extends GridPane {
        private final TacheSimple tache;

        public SubTacheCard(@NotNull TacheSimple tache) {
            this.tache = tache;

            this.setAlignment(Pos.CENTER);
            this.setHgap(20);

            ColumnConstraints first = new ColumnConstraints();
            first.setPrefWidth(150);
            ColumnConstraints second = new ColumnConstraints();
            second.setPrefWidth(75);
            this.getColumnConstraints().addAll(first, second);

            Label titleLabel = new Label();
            titleLabel.setText(tache.getNom());
            titleLabel.setStyle("-fx-text-fill: #0d47a1; -fx-font-size: 24px; -fx-effect: dropshadow(gaussian, rgba(33,150,243,0.7), 5, 0, 0, 0);");
            HBox titleBox = new HBox();
            titleBox.getChildren().add(titleLabel);
            titleBox.setAlignment(Pos.CENTER);
            this.add(titleBox, 0, 0);

            Button moreButton = new Button();
            moreButton.setText("More");
            moreButton.setMinWidth(75);
            moreButton.setOnAction(this::handleMoreButtonAction);
            moreButton.setStyle("-fx-background-color: #2B7BFF; -fx-text-fill: white; -fx-font-size: 18px; -fx-cursor: hand;");
            DropShadow effect = new DropShadow();
            effect.setRadius(5);
            effect.setSpread(0.2);
            effect.setColor(Color.web("#1F579D"));
            moreButton.setEffect(effect);
            HBox moreBox = new HBox();
            moreBox.getChildren().add(moreButton);
            moreBox.setAlignment(Pos.CENTER);
            this.add(moreBox, 1, 0);
        }

        private void handleMoreButtonAction(ActionEvent event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tache/InfoTacheSimplePopup.fxml"));
                loader.setControllerFactory(param -> new InfoTacheSimplePopupController(this.tache, InfoTacheDecomposablePopupController.this.user, InfoTacheDecomposablePopupController.this.tache, InfoTacheDecomposablePopupController.this.projectOfTache));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle("Sub Tache Info");
                stage.setResizable(false);
                stage.setScene(scene);

                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
                stage.getIcons().add(icon);

                Parent root = ((Node)event.getSource()).getScene().getRoot();
                root.setEffect(new GaussianBlur(15));
                stage.showAndWait();
                root.setEffect(null);

                update();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
