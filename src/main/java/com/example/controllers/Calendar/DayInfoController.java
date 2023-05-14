package com.example.controllers.Calendar;

import com.example.controllers.Controller;
import com.example.core.Calendrier;
import com.example.core.Creneau;
import com.example.core.CreneauLibre;
import com.example.core.Day;
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
    private final Calendrier calendrier;
    private final LocalDate date;
    private final CalendarController calendarController;
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
                creneauxContainer.getChildren().add(new CreneauLibreItem(c.getHeureDebut(), c.getHeureFin()));
            else
                creneauxContainer.getChildren().add(new Label((c.getHeureDebut() + " O " + c.getHeureFin())));

        }
    }


    public class CreneauLibreItem extends HBox {
        private final Label startTimeLabel;
        private final Label endTimeLabel;
        private final Button moreInfoButton;
        private final Button deleteButton;

        public CreneauLibreItem(LocalTime startTime, LocalTime endTime) {
            startTimeLabel = new Label(startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            startTimeLabel.setStyle("-fx-font-size: 16px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #2196f3;");

            endTimeLabel = new Label(endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            endTimeLabel.setStyle("-fx-font-size: 16px;" + "-fx-font-weight: bold;" + "-fx-text-fill: #2196f3;");

            moreInfoButton = new Button("More Info");
            moreInfoButton.setStyle("-fx-background-color: #2196f3;" + "-fx-text-fill: white;" + "-fx-font-size: 14px;" + "-fx-font-weight: bold;" + "-fx-padding: 5 10;" + "-fx-background-radius: 20;");
            moreInfoButton.setCursor(Cursor.HAND);

            deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: #f44336;" + "-fx-text-fill: white;" + "-fx-font-size: 14px;" + "-fx-font-weight: bold;" + "-fx-padding: 5 10;" + "-fx-background-radius: 20;");
            deleteButton.setCursor(Cursor.HAND);

            VBox buttonsBox = new VBox(10);
            buttonsBox.getChildren().addAll(moreInfoButton, deleteButton);
            buttonsBox.setAlignment(Pos.CENTER);

            getChildren().addAll(startTimeLabel, new Label("-"), endTimeLabel, buttonsBox);
            setSpacing(10);
            setAlignment(Pos.CENTER);
            setPadding(new Insets(10));
            getStyleClass().add("creneau-libre-item");
            setStyle("-fx-background-color: white;" + "-fx-background-radius: 10;" + "-fx-padding: 10;" + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 5, 0, 0, 1);");
        }
    }
}
