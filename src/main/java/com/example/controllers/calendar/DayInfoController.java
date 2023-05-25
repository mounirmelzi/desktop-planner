package com.example.controllers.calendar;

import com.example.controllers.Controller;
import com.example.controllers.tache.InfoTacheDecomposablePopupController;
import com.example.controllers.tache.InfoTacheSimplePopupController;
import com.example.core.*;
import com.example.core.exceptions.CreneauLibreDurationException;
import com.example.core.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DayInfoController extends Controller implements Initializable {
    @FXML
    private Label dayDateLabel;
    @FXML
    private Label dayNameLabel;
    @FXML
    private VBox creneauxContainer;
    @FXML
    private TextField heureDebutTextField;
    @FXML
    private TextField heureFinTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private ProgressBar realisationStateProgressBar;

    private final CalendarController calendarController;
    private final User user;
    private final LocalDate date;
    private Day day;

    public DayInfoController(User user, Day day, LocalDate date, CalendarController calendarController) {
        this.user = user;
        this.day = day;
        this.date = date;
        this.calendarController = calendarController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dayDateLabel.setText(date.toString());
        dayNameLabel.setText(date.getDayOfWeek().name());
        updateCreneaux();
    }

    @FXML
    private void handleAddCreneauButtonAction() {
        LocalTime startTime = Utils.stringToLocalTime(heureDebutTextField.getText());
        LocalTime endTime = Utils.stringToLocalTime(heureFinTextField.getText());

        try {
            if (day == null) {
                Day newDay = new Day(date);
                if (!user.getCalendrier().addDay(newDay))
                    throw new NullPointerException();

                day = newDay;
            }

            CreneauLibre creneauLibre = new CreneauLibre(startTime, endTime);

            if (!day.ajouterCreneauLibre(creneauLibre)) throw new CreneauLibreDurationException();
            updateCreneaux();
        } catch (CreneauLibreDurationException | IllegalArgumentException | NullPointerException e) {
            errorLabel.setText("Impossible d'ajouter ce créneau libre");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddCreneauTextFieldKeyPressed(@NotNull KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleAddCreneauButtonAction();
        }
    }

    private void updateCreneaux() {
        errorLabel.setText("");
        creneauxContainer.getChildren().clear();
        calendarController.updateCalendar();
        realisationStateProgressBar.setProgress(day == null ? 0.0 : day.getRendement());

        if (day == null || !day.hasCreneaux())
            return;

        for (Creneau c : day.getCreneaux()) {
            if (c.isLibre())
                creneauxContainer.getChildren().add(new CreneauLibreItem((CreneauLibre) c));
            else
                creneauxContainer.getChildren().add(new CreneauOccupeItem((CreneauOccupe) c));
        }
    }


    public class CreneauLibreItem extends HBox {
        private final CreneauLibre creneauLibre;

        public CreneauLibreItem(@NotNull CreneauLibre creneauLibre) {
            super();
            this.getChildren().clear();
            this.creneauLibre = creneauLibre;

            Label startTimeLabel = new Label(creneauLibre.getHeureDebut().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            startTimeLabel.setStyle("-fx-font-size: 16px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #2196f3;");

            Label sepLabel = new Label("-");
            sepLabel.setStyle("-fx-font-size: 16px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #2196f3;");

            Label endTimeLabel = new Label(creneauLibre.getHeureFin().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            endTimeLabel.setStyle("-fx-font-size: 16px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #2196f3;");

            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: #f44336;" + "-fx-text-fill: white;" + "-fx-font-size: 14px;" + "-fx-font-weight: bold;" + "-fx-padding: 5 10;" + "-fx-background-radius: 20;");
            deleteButton.setCursor(Cursor.HAND);
            deleteButton.setOnAction(this::handleDeleteButtonAction);

            Button planifierButton = new Button("Planifier");
            planifierButton.setStyle("-fx-background-color: #32CD32;" + "-fx-text-fill: white;" + "-fx-font-size: 14px;" + "-fx-font-weight: bold;" + "-fx-padding: 5 10;" + "-fx-background-radius: 20;");
            planifierButton.setCursor(Cursor.HAND);
            planifierButton.setOnAction(this::handlePlanifierButtonAction);

            VBox btnsBox = new VBox();
            btnsBox.setSpacing(10);
            btnsBox.setAlignment(Pos.CENTER);
            btnsBox.getChildren().addAll(planifierButton, deleteButton);

            HBox container = new HBox();
            container.setPadding(new Insets(5));
            container.setSpacing(10);
            container.setAlignment(Pos.CENTER);
            container.getChildren().addAll(startTimeLabel, sepLabel, endTimeLabel, btnsBox);

            this.getChildren().add(container);
            this.setStyle("-fx-background-color: white;" + "-fx-background-radius: 10;" + "-fx-padding: 10;" + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 5, 0, 0, 1);");
        }

        private void handleDeleteButtonAction(ActionEvent event) {
            day.getCreneaux().remove(this.creneauLibre);
            updateCreneaux();
        }

        private void handlePlanifierButtonAction(ActionEvent event) {
            if (!user.hasPlanning()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Planification failed");
                alert.setHeaderText("You don't have a planning");
                alert.setContentText("Vous devez créer un planning d'abord !");
                alert.showAndWait();
                return;
            }

            if (date.isBefore(user.getPlanning().getDateDebut()) || date.isAfter(user.getPlanning().getDateFin())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Planification failed");
                alert.setHeaderText("No Planning Found");
                alert.setContentText("Vous essayez de planifier une tâche en dehors de la période du planning courant");
                alert.showAndWait();
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/calendar/PlanifierManuellement.fxml"));
                loader.setControllerFactory(param -> new PlanifierManuellementController(user, date, creneauLibre.getHeureDebut()));

                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle("Planification Manuelle");
                stage.setResizable(false);
                stage.setScene(scene);

                Parent root = ((Node)event.getSource()).getScene().getRoot();
                root.setEffect(new GaussianBlur(15));
                stage.showAndWait();
                root.setEffect(null);

                updateCreneaux();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class CreneauOccupeItem extends HBox {
        private final CreneauOccupe creneauOccupe;
        private final Button blockButton;

        public CreneauOccupeItem(@NotNull CreneauOccupe creneauOccupe) {
            super();
            this.getChildren().clear();
            this.creneauOccupe = creneauOccupe;

            Label startTimeLabel = new Label(creneauOccupe.getHeureDebut().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            startTimeLabel.setStyle("-fx-font-size: 16px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #2196f3;");

            Label sepLabel = new Label("-");
            sepLabel.setStyle("-fx-font-size: 16px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #2196f3;");

            Label endTimeLabel = new Label(creneauOccupe.getHeureFin().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            endTimeLabel.setStyle("-fx-font-size: 16px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #2196f3;");

            HBox timeBox = new HBox(startTimeLabel, sepLabel, endTimeLabel);
            timeBox.setSpacing(10);
            timeBox.setAlignment(Pos.CENTER);

            Label taskTitleBox = new Label(creneauOccupe.getTache().getNom());
            taskTitleBox.setMaxWidth(150);
            taskTitleBox.setStyle("-fx-font-size: 16px; -fx-wrap-text: true;");

            VBox infoBox = new VBox(timeBox, taskTitleBox);
            infoBox.setSpacing(10);
            infoBox.setAlignment(Pos.CENTER);

            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: #f44336;" + "-fx-text-fill: white;" + "-fx-font-size: 14px;" + "-fx-font-weight: bold;" + "-fx-padding: 5 10;" + "-fx-background-radius: 20;");
            deleteButton.setCursor(Cursor.HAND);
            deleteButton.setOnAction(this::handleDeleteButtonAction);

            Button infoButton = new Button("More");
            infoButton.setStyle("-fx-background-color: #2196f3;" + "-fx-text-fill: white;" + "-fx-font-size: 14px;" + "-fx-font-weight: bold;" + "-fx-padding: 5 10;" + "-fx-background-radius: 20;");
            infoButton.setCursor(Cursor.HAND);
            infoButton.setOnAction(this::handleShowMoreInfoButtonAction);

            blockButton = new Button(creneauOccupe.isBlocked() ? "Unblock" : "Block");
            blockButton.setStyle("-fx-background-color: #32CD32;" + "-fx-text-fill: white;" + "-fx-font-size: 14px;" + "-fx-font-weight: bold;" + "-fx-padding: 5 10;" + "-fx-background-radius: 20;");
            blockButton.setCursor(Cursor.HAND);
            blockButton.setOnAction(this::handleBlockButtonAction);
            blockButton.setMinWidth(75);
            blockButton.setMaxWidth(75);

            VBox buttonsBox = new VBox(infoButton, blockButton, deleteButton);
            buttonsBox.setSpacing(10);
            buttonsBox.setAlignment(Pos.CENTER);

            HBox container = new HBox(infoBox, buttonsBox);
            container.setPadding(new Insets(10));
            container.setSpacing(10);
            container.setAlignment(Pos.CENTER);

            this.getChildren().add(container);
            this.setStyle("-fx-background-color: white;" + "-fx-background-radius: 10;" + "-fx-padding: 10;" + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 5, 0, 0, 1);");
        }

        private void handleDeleteButtonAction(ActionEvent event) {
            Tache tache = this.creneauOccupe.getTache();

            if (tache.isPeriodique()) {
                errorLabel.setText("Impossible de supprimer un créneau contenant une tache périodique");
                return;
            }

            tache.deplanifier(user.getPlanning());
            updateCreneaux();
        }

        private void handleBlockButtonAction(ActionEvent event) {
            creneauOccupe.setBlocked(!creneauOccupe.isBlocked());
            blockButton.setText(creneauOccupe.isBlocked() ? "Unblock" : "Block");
        }

        private void handleShowMoreInfoButtonAction(ActionEvent event) {
            Tache tache = creneauOccupe.getTache();

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

                    Parent root = ((Node)event.getSource()).getScene().getRoot();
                    root.setEffect(new GaussianBlur(15));
                    stage.showAndWait();
                    root.setEffect(null);
                } else {
                    loader = new FXMLLoader(getClass().getResource("/views/tache/InfoTacheDecomposablePopup.fxml"));
                    loader.setControllerFactory(param -> new InfoTacheDecomposablePopupController((TacheDecomposable) tache, user, null));
                    Scene scene = new Scene(loader.load());
                    Stage stage = new Stage();
                    stage.setTitle("Tache Decomposable Info");
                    stage.setResizable(false);
                    stage.setScene(scene);

                    Parent root = ((Node)event.getSource()).getScene().getRoot();
                    root.setEffect(new GaussianBlur(15));
                    stage.showAndWait();
                    root.setEffect(null);
                }

                updateCreneaux();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
