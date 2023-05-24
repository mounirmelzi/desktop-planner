package com.example.controllers.project;

import com.example.controllers.Controller;
import com.example.controllers.tache.InfoTacheDecomposablePopupController;
import com.example.controllers.tache.InfoTacheSimplePopupController;
import com.example.core.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InfoProjectPopupController extends Controller implements Initializable {
    @FXML
    private Label nameLabel;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private VBox tachesContainer;
    @FXML
    private Button editButton;

    private final Project project;
    private final User user;

    public InfoProjectPopupController(Project project, User user) {
        this.project = project;
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        update();
    }

    private void update() {
        editButton.setDisable(!project.isUnscheduled() && project.hasTaches());
        nameLabel.setText(project.getNom());
        descriptionTextArea.setText(project.getDescription());

        tachesContainer.getChildren().clear();
        for (Tache tache : project.getTaches()) {
            TacheCard tacheCard = new TacheCard(tache);
            tachesContainer.getChildren().add(tacheCard);
        }
    }

    @FXML
    private void handleEditButtonAction() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/project/EditProjectPopup.fxml"));
            loader.setControllerFactory(param -> new EditProjectPopupController(project, user));
            stage.setTitle("Edit Project Information");

            Scene scene = new Scene(loader.load());
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();

            update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class TacheCard extends GridPane {
        private final Tache tache;

        public TacheCard(@NotNull Tache tache) {
            this.tache = tache;

            this.setAlignment(Pos.CENTER);
            this.setHgap(20);

            ColumnConstraints first = new ColumnConstraints();
            first.setPrefWidth(200);
            ColumnConstraints second = new ColumnConstraints();
            second.setPrefWidth(150);
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
            moreButton.setMinWidth(50);
            moreButton.setOnAction(this::handleMoreButtonAction);
            moreButton.setStyle("-fx-background-color: #2B7BFF; -fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");
            DropShadow moreEffect = new DropShadow();
            moreEffect.setRadius(5);
            moreEffect.setSpread(0.2);
            moreEffect.setColor(Color.web("#1F579D"));
            moreButton.setEffect(moreEffect);
            HBox moreBox = new HBox();
            moreBox.getChildren().add(moreButton);
            moreBox.setAlignment(Pos.CENTER);

            Button deleteButton = new Button();
            deleteButton.setDisable(!project.isUnscheduled());
            deleteButton.setText("Delete");
            deleteButton.setMinWidth(50);
            deleteButton.setOnAction(this::handleDeleteButtonAction);
            deleteButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");
            DropShadow deleteEffect = new DropShadow();
            deleteEffect.setRadius(5);
            deleteEffect.setSpread(0.2);
            deleteEffect.setColor(Color.web("#FF0000"));
            deleteButton.setEffect(deleteEffect);
            HBox deleteBox = new HBox();
            deleteBox.getChildren().add(deleteButton);
            deleteBox.setAlignment(Pos.CENTER);

            HBox buttonsContainer = new HBox();
            buttonsContainer.setSpacing(10);
            buttonsContainer.setMinWidth(200);
            buttonsContainer.getChildren().addAll(moreBox, deleteBox);

            this.add(buttonsContainer, 1, 0);
        }

        private void handleMoreButtonAction(ActionEvent event) {
            try {
                Stage stage = new Stage();
                FXMLLoader loader;

                if (tache instanceof TacheSimple) {
                    loader = new FXMLLoader(getClass().getResource("/views/tache/InfoTacheSimplePopup.fxml"));
                    loader.setControllerFactory(param -> new InfoTacheSimplePopupController((TacheSimple) this.tache, InfoProjectPopupController.this.user, null, InfoProjectPopupController.this.project));
                    stage.setTitle("Tache Simple Info");
                } else {
                    loader = new FXMLLoader(getClass().getResource("/views/tache/InfoTacheDecomposablePopup.fxml"));
                    loader.setControllerFactory(param -> new InfoTacheDecomposablePopupController((TacheDecomposable) this.tache, InfoProjectPopupController.this.user, InfoProjectPopupController.this.project));
                    stage.setTitle("Tache Décomposable Info");
                }

                Scene scene = new Scene(loader.load());
                stage.setResizable(false);
                stage.setScene(scene);
                stage.showAndWait();

                update();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleDeleteButtonAction(ActionEvent event) {
            project.deleteTache(this.tache);
            update();
        }
    }
}
