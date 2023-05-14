package com.example.controllers;

import com.example.core.Calendrier;
import com.example.core.Day;
import com.example.core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ResourceBundle;

public class CalendarController extends Controller implements Initializable {
    @FXML
    private Button prevMonthButton, nextMonthButton;
    @FXML
    private Label yearLabel, monthLabel;
    @FXML
    private GridPane calendarGridPane;
    @FXML
    private AnchorPane dayInfoAnchorPane;

    private final User user;
    private YearMonth currentYearMonth;

    public CalendarController(User user) {
        this.user = user;
        this.currentYearMonth = YearMonth.now();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateCalendar();
        updateDayInfo(user.getCalendrier().getDayByDate(LocalDate.now()));
    }

    @FXML
    private void handleMonthButtonAction(@NotNull ActionEvent event) {
        if (event.getSource() == prevMonthButton) {
            currentYearMonth = currentYearMonth.minusMonths(1);
        } else if (event.getSource() == nextMonthButton) {
            currentYearMonth = currentYearMonth.plusMonths(1);
        }

        updateCalendar();
    }

    private void handleDayLabelMouseClicked(@NotNull MouseEvent event, Day day) {
        if (event.getButton() == MouseButton.PRIMARY)
            updateDayInfo(day);
    }

    private void updateCalendar() {
        monthLabel.setText(currentYearMonth.getMonth().toString());
        yearLabel.setText(String.valueOf(currentYearMonth.getYear()));
        calendarGridPane.getChildren().clear();

        int daysInMonth = currentYearMonth.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            LocalDate date = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(), i);
            DayLabel dayLabel = new DayLabel(user.getCalendrier(), date);

            calendarGridPane.add(dayLabel, ((i < 29) ? ((i - 1) % 7) : (2 + (i - 1) % 7)), (i + 6) / 7);
        }
    }

    private void updateDayInfo(Day day) {
        // todo : load day info fxml and pass day in args to display day infos
        // he can add creneau libre, supprimer creneau libre, clear all creneau, view all creneau : in this section
        dayInfoAnchorPane.getChildren().clear();
        dayInfoAnchorPane.getChildren().add(new Label(day != null ? day.getDate().toString() : "No day infos"));
    }


    private class DayLabel extends Label {
        private final Day day;

        public DayLabel(@NotNull Calendrier calendrier, @NotNull LocalDate date) {
            super(String.valueOf(date.getDayOfMonth()));
            this.day = calendrier.getDayByDate(date);

            this.setOnMouseClicked(event -> handleDayLabelMouseClicked(event, day));
            this.setPrefSize(50, 50);
            this.setStyles(day != null && day.hasCreneaux());
            this.setCursor(Cursor.HAND);
        }

        private void setStyles(boolean mark) {
            this.getStyleClass().add("day-label");
            this.setStyle((mark ? "-fx-border-color: #123456;" : ""));
        }
    }
}
