package com.example.controllers.Calendar;

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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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

        int daysInMonth = currentYearMonth.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            LocalDate date = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(), i);
            DayLabel dayLabel = new DayLabel(date);

            calendarGridPane.add(dayLabel, ((i < 29) ? ((i - 1) % 7) : (2 + (i - 1) % 7)), (i + 6) / 7);
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

        public DayLabel(@NotNull LocalDate date) {
            super(String.valueOf(date.getDayOfMonth()));
            this.day = user.getCalendrier().getDayByDate(date);

            this.setOnMouseClicked(event -> handleDayLabelMouseClicked(event, day, date));
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
