package com.example.controllers.calendar;

import com.example.controllers.Controller;
import com.example.core.Day;
import com.example.core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
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
    @FXML
    private HBox daysNamesContainer;

    private final User user;
    private YearMonth currentYearMonth;

    public CalendarController(User user) {
        this.user = user;
        this.currentYearMonth = YearMonth.now();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateCalendar();
        LocalDate now = LocalDate.now();
        updateDayInfo(user.getCalendrier().getDayByDate(now), now);
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

    private void handleDayLabelMouseClicked(@NotNull MouseEvent event, Day day, LocalDate date) {
        if (event.getButton() == MouseButton.PRIMARY)
            updateDayInfo(day, date);
    }

    void updateCalendar() {
        monthLabel.setText(currentYearMonth.getMonth().toString());
        yearLabel.setText(String.valueOf(currentYearMonth.getYear()));
        calendarGridPane.getChildren().clear();
        daysNamesContainer.getChildren().clear();

        for (int i = 1; i <= 7; i++) {
            Label dayNameLabel = new Label();
            dayNameLabel.setText(LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(), i).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()).toUpperCase());
            dayNameLabel.setPrefSize(50, 50);
            dayNameLabel.getStyleClass().add("day-label");
            dayNameLabel.setStyle("-fx-border-color: #123456; -fx-background-color: #123456; -fx-text-fill: #f0f0f0;");
            daysNamesContainer.getChildren().add(dayNameLabel);
        }

        int daysInMonth = currentYearMonth.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            LocalDate date = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(), i);
            DayLabel dayLabel = new DayLabel(date);

            calendarGridPane.add(dayLabel, (i - 1) % 7, (i + 6) / 7);
        }
    }

    private void updateDayInfo(Day day, LocalDate date) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/calendar/DayInfo.fxml"));
        loader.setControllerFactory(param -> new DayInfoController(user, day, date, this));

        try {
            Parent dayInfoPane = loader.load();
            dayInfoAnchorPane.getChildren().clear();
            dayInfoAnchorPane.getChildren().add(dayInfoPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class DayLabel extends Label {
        private final Day day;
        private final LocalDate date;

        public DayLabel(@NotNull LocalDate date) {
            super(String.valueOf(date.getDayOfMonth()));
            this.day = user.getCalendrier().getDayByDate(date);
            this.date = date;

            this.setOnMouseClicked(event -> handleDayLabelMouseClicked(event, day, date));
            this.setPrefSize(50, 50);
            this.setStyles();
            this.setCursor(Cursor.HAND);
        }

        private void setStyles() {
            this.getStyleClass().add("day-label");

            if (day != null && day.hasCreneauxOccupees()) {
                this.getStyleClass().clear();
                this.getStyleClass().add("day-label-occupe");
            } else if (day != null && day.hasCreneaux())
                this.setStyle("-fx-border-color: #123456;");

            if (date.isEqual(LocalDate.now()))
                this.setStyle("-fx-border-color: #ee6c4d;");
        }
    }
}
