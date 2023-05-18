package com.example.controllers.Calendar;

import com.example.controllers.Controller;
import com.example.core.*;
import com.example.core.exceptions.CreneauLibreDurationException;
import com.example.core.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    private final CalendarController calendarController;
    private final Calendrier calendrier;
    private final LocalDate date;
    private Day day;

    public DayInfoController(Calendrier calendrier, Day day, LocalDate date, CalendarController calendarController) {
        this.calendrier = calendrier;
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
    private void handleAddCreneauButtonAction(ActionEvent event) {
        LocalTime startTime = Utils.stringToLocalTime(heureDebutTextField.getText());
        LocalTime endTime = Utils.stringToLocalTime(heureFinTextField.getText());

        try {
            if (day == null) {
                Day newDay = new Day(date);
                if (!calendrier.addDay(newDay))
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

    private void updateCreneaux() {
        errorLabel.setText("");
        creneauxContainer.getChildren().clear();
        calendarController.updateCalendar();

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
        private CreneauLibre creneauLibre;

        public CreneauLibreItem(CreneauLibre creneauLibre) {
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

            HBox container = new HBox();
            container.setPadding(new Insets(10));
            container.setSpacing(10);
            container.setAlignment(Pos.CENTER);
            container.getChildren().addAll(startTimeLabel, sepLabel, endTimeLabel, deleteButton);

            this.getChildren().add(container);
            this.setStyle("-fx-background-color: white;" + "-fx-background-radius: 10;" + "-fx-padding: 10;" + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 5, 0, 0, 1);");
        }

        private void handleDeleteButtonAction(ActionEvent event) {
            //day.deleteCreneau(this.creneauLibre);
            updateCreneaux();
        }
    }

    public class CreneauOccupeItem extends HBox {
        private CreneauOccupe creneauOccupe;

        public CreneauOccupeItem(CreneauOccupe creneauOccupe) {
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
            taskTitleBox.setStyle("-fx-font-size: 16px;");

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

            VBox buttonsBox = new VBox(infoButton, deleteButton);
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
            //day.deleteCreneau(this.creneauOccupe);
            updateCreneaux();
        }

        private void handleShowMoreInfoButtonAction(ActionEvent event) {
            System.out.println("show more info about creneau occupe " + day.getDate());
        }
    }
}
