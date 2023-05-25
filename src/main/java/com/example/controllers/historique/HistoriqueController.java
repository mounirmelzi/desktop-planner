package com.example.controllers.historique;

import com.example.controllers.Controller;
import com.example.core.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.time.*;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;


public class HistoriqueController extends Controller implements Initializable {


    private final User user;

    @FXML
    private HBox historiquePlannings, hboxBadges;
    @FXML
    private Button prevMonthButton, nextMonthButton;
    @FXML
    private Label yearLabel, monthLabel, nbBadgesGood, nbBadgesVeryGood, nbBadgesExcellent;
    @FXML
    private GridPane calendarGridPane;
    @FXML
    private AnchorPane dayInfoAnchorPane;
    @FXML
    private ImageView badgeGood, badgeVeryGood, badgeExcellent;
    @FXML
    private HBox daysNamesContainer;
    @FXML
    private Label nbTachesCompletees,nbProjetCompletes ;
    private final DayInfoController dayInfoController;



    public HistoriqueController(User user) {
        this.user = user;
        dayInfoController = new DayInfoController(user);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {

        hboxBadges.setVisible(false);
        prevMonthButton.setVisible(false);
        nextMonthButton.setVisible(false);
        //user.getHistorique().archive(user.getPlanning());
        ajouterHistoriquePlanning(user);
        afficherDernierPlanning(user);
    }

    public void afficherDernierPlanning(User user) {
        try {
            Map.Entry<LocalDateTime, Planning> entry = user.getHistorique().getLastPlanningArchive();
            Planning lastP = entry == null ? null : entry.getValue();
            if (lastP == null) {
                afficherAucunPlanning();
            } else {
                afficherPlanning(lastP, lastP.getDateDebut());
                historiquePlannings.getChildren().get(0).getStyleClass().add("btn-pressed");
            }
            afficherNbCompletes(lastP, user.getHistorique().getDateArchivage(lastP));
            afficherBadges(lastP);
        } catch (NullPointerException e) {
            afficherAucunPlanning();
            e.printStackTrace();
        }
    }


    @FXML
    public void ajouterHistoriquePlanning(User user) {
        historiquePlannings.getChildren().clear();
        try {
            Historique h = user.getHistorique();
            if ((h == null) || (h.getHistoriquePlannings() == null)) {
                afficherAucunPlanning();
            } else {
                for (Map.Entry<LocalDateTime, Planning> entry : h.getHistoriquePlannings().entrySet()) {
                    addButtonPlanning(entry.getValue(), entry.getKey());
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void addButtonPlanning(Planning p, LocalDateTime FDate) {
        try {
            Button button = new Button("Planning" + "\n" + "[" + p.getDateDebut() + " || " + FDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "]");
            button.getStyleClass().add("btn");
            button.setOnAction(actionEvent -> {
                historiquePlannings.getChildren().forEach(child -> child.getStyleClass().remove("btn-pressed"));
                button.getStyleClass().add("btn-pressed") ;
                afficherPlanning(p, p.getDateDebut());
                afficherBadges(p);
                afficherNbCompletes(p, FDate);
                afficherDayInfo(p.getDayByDate(p.getDateDebut()), p.getDateDebut());
            });
            historiquePlannings.getChildren().add(button);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void afficherPlanning(Planning p, LocalDate date) {

        if (p != null) {
            prevMonthButton.setVisible(true);
            nextMonthButton.setVisible(true);

            dayInfoController.setPlanning(p);

            yearLabel.setText(String.valueOf(date.getYear()));
            monthLabel.setText(date.getMonth().toString());
            calendarGridPane.getChildren().clear();
            daysNamesContainer.getChildren().clear();

            for (int i = 1; i <= 7; i++) {
                Label dayNameLabel = new Label();
                dayNameLabel.setText(LocalDate.of(date.getYear(), date.getMonth(), i).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()).toUpperCase());
                dayNameLabel.setPrefSize(50, 50);
                dayNameLabel.getStyleClass().add("day-label");
                dayNameLabel.setStyle("-fx-border-color: #123456; -fx-background-color: #123456; -fx-text-fill: #f0f0f0;");
                daysNamesContainer.getChildren().add(dayNameLabel);
            }


            int daysInMonth = YearMonth.now().withMonth(date.getMonth().getValue()).lengthOfMonth();
            for (int i = 1; i <= daysInMonth; i++) {
                date = LocalDate.of(date.getYear(), date.getMonth(), i);
                DayLabel dayLabel = new DayLabel(date, p.getCalendrier().getDayByDate(date));
                dayLabel.setText(String.valueOf(i));
                calendarGridPane.add(dayLabel, (i - 1) % 7, (i + 6) / 7);
            }

            afficherDayInfo(p.getDayByDate(date), p.getDateDebut());
        }
    }


    public void afficherDayInfo(Day day, LocalDate date) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/historique/DayInfo.fxml"));
        loader.setController(dayInfoController);
        try {
            Parent dayInfo = loader.load();
            dayInfoAnchorPane.getChildren().clear();
            dayInfoAnchorPane.getChildren().add(dayInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dayInfoController.setDate(date);
        dayInfoController.setDay(day);
        dayInfoController.afficherDate();
        dayInfoController.afficherTache();
    }

    public void handleNextMonth() {
        Month monthInMonthLabel = Month.valueOf(monthLabel.getText());
        //Month dayInfoMonth = dayInfo.getDate().getMonth() ;
        Month dateFin = dayInfoController.getPlanning().getDateFin().getMonth() ;
        //Month archivageMonth = (user.getHistorique().getDateArchivage(dayInfoController.getPlanning())).getMonth();
        if (dateFin.compareTo(monthInMonthLabel) > 0) {
            if (monthInMonthLabel.getValue() < 12) {
                afficherPlanning(dayInfoController.getPlanning(), LocalDate.of(Integer.parseInt(yearLabel.getText()), monthInMonthLabel.plus(1).getValue(), 1));
            } else {
                afficherPlanning(dayInfoController.getPlanning(), LocalDate.of(Integer.parseInt(yearLabel.getText()) + 1, 1, 1));
            }
        }
    }

    public void handlePrevMonth() {
        Month monthInMonthLabel = Month.valueOf(monthLabel.getText());
        //Month dayInfoMonth = dayInfo.getDate().getMonth() ;
        Month dateDebutMonth = dayInfoController.getPlanning().getDateDebut().getMonth();
        if (monthInMonthLabel.compareTo(dateDebutMonth) > 0) {
            if (monthInMonthLabel.getValue() > 1) {
                afficherPlanning(dayInfoController.getPlanning(), LocalDate.of(Integer.parseInt(yearLabel.getText()), monthInMonthLabel.minus(1).getValue(), 1));
            } else {
                afficherPlanning(dayInfoController.getPlanning(), LocalDate.of(Integer.parseInt(yearLabel.getText()) - 1, 12, 1));
            }
        }
    }

    public void afficherNbCompletes (Planning p ,LocalDateTime dateArchivage){
        nbTachesCompletees.setText((p == null) ? "0" : String.valueOf(p.getNbTachesCompletees()));
        nbProjetCompletes.setText(String.valueOf(dayInfoController.getUser().getHistorique().getNbProjetsCompletes(dateArchivage)));
    }
    public void afficherBadges(Planning planning) {

        hboxBadges.setVisible(true);
        badgeGood.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/goodBadge.png"))));
        badgeVeryGood.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/veryGoodBadge.png"))));
        badgeExcellent.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ExcellentBadge.png"))));

        try {
            if (planning.getBadges().get(Badge.GOOD) != null){
            nbBadgesGood.setText(String.valueOf(planning.getBadges().get(Badge.GOOD)));}
            else  {nbBadgesGood.setText("0");}
        } catch (NullPointerException e) {
            nbBadgesGood.setText("0");
        }
        try {
            if (planning.getBadges().get(Badge.VERY_GOOD) != null) {nbBadgesVeryGood.setText(String.valueOf(planning.getBadges().get(Badge.VERY_GOOD)));}
            else {nbBadgesVeryGood.setText("0");}
        } catch (NullPointerException e) {
            nbBadgesVeryGood.setText("0");
        }
        try {
            if (planning.getBadges().get(Badge.EXCELLENT) != null) { nbBadgesExcellent.setText(String.valueOf(planning.getBadges().get(Badge.EXCELLENT)));}
            else { nbBadgesExcellent.setText("0");}
        } catch (NullPointerException e) {
            nbBadgesExcellent.setText("0");
        }
    }

    private void afficherAucunPlanning() {
        Button button = new Button("Vous n'avez aucun planning archivé !");
        button.getStyleClass().add("btnAucunPlanning");
        historiquePlannings.getChildren().add(button);
    }

    private class DayLabel extends Label {
        private final Day day;

        public DayLabel(@NotNull LocalDate date, Day day) {
            super(String.valueOf(date.getDayOfMonth()));
            this.day = dayInfoController.getPlanning().getCalendrier().getDayByDate(date);

            this.setOnMouseClicked(event -> handleDayLabelMouseClicked(event, day, date));
            this.setPrefSize(50, 50);
            this.setStyles();
            this.setCursor(Cursor.HAND);
        }

        private void setStyles() {
            this.getStyleClass().add("day-label");

            if (day != null && day.getTotalTachesNumber()!=0) {
                this.getStyleClass().clear();
                this.getStyleClass().add("day-label-occupe");
            }
        }

        private void handleDayLabelMouseClicked(@NotNull MouseEvent event, Day day, LocalDate date) {
            if (event.getButton() == MouseButton.PRIMARY)
                afficherDayInfo(day, date);
        }
    }
}
