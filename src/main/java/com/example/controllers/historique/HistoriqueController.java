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

    //region attributs
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
    //endregion

    //region constructeur
    /**
     * Constructeur
     * @param user l'utilisateur associé
     */
    public HistoriqueController(User user) {
        this.user = user;
        dayInfoController = new DayInfoController(user);
    }
    //endregion

    //region methodes
    /**
     * Initialiser l'affichage de la page Historique, pour afficher la liste des plannings arrchivés
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hboxBadges.setVisible(false);
        prevMonthButton.setVisible(false);
        nextMonthButton.setVisible(false);
        ajouterHistoriquePlanning(user);
        afficherDernierPlanning(user);
    }

    /**
     * Affichage du dernier planning archivé
     * @param user l'utilisateur associé au controlleur
     */
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

    /**
     * L'ajout de la liste des bouttons des plannings archivés à la page Historique
     * @param user l'utilisateur associé au controlleur
     */
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

    /**
     * Ajouter le boutton representant un planning archivé
     * @param p le planning à afficher ses infos
     * @param FDate la date de l'archivage du planning
     */
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

    /**
     * Affichage des informaion associé a un planning archivé, son calendrier, badges, nbTaches et projets completes
     * @param p le planning à afficher ses infos
     * @param date la date du planning à afficher
     */
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

    /**
     * Afficher les information a propos d'une journée d'un des planning archivé
     * @param day represente la journée à afficher ses infos (ses taches si il y'en a)
     * @param date la date de la journée que l'on affiche ses infos
     */
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

    /**
     * handler du boutton nextMonth qui avance dans l'affichage vers le mois suivant
     */
    public void handleNextMonth() {
        Month monthInMonthLabel = Month.valueOf(monthLabel.getText());
        Month dateFin = dayInfoController.getPlanning().getDateFin().getMonth() ;
        if (dateFin.compareTo(monthInMonthLabel) > 0) {
            if (monthInMonthLabel.getValue() < 12) {
                afficherPlanning(dayInfoController.getPlanning(), LocalDate.of(Integer.parseInt(yearLabel.getText()), monthInMonthLabel.plus(1).getValue(), 1));
            } else {
                afficherPlanning(dayInfoController.getPlanning(), LocalDate.of(Integer.parseInt(yearLabel.getText()) + 1, 1, 1));
            }
        }
    }

    /**
     * handler du boutton prevtMonth qui recule dans l'affichage vers le mois precedant
     */
    public void handlePrevMonth() {
        Month monthInMonthLabel = Month.valueOf(monthLabel.getText());
        Month dateDebutMonth = dayInfoController.getPlanning().getDateDebut().getMonth();
        if (monthInMonthLabel.compareTo(dateDebutMonth) > 0) {
            if (monthInMonthLabel.getValue() > 1) {
                afficherPlanning(dayInfoController.getPlanning(), LocalDate.of(Integer.parseInt(yearLabel.getText()), monthInMonthLabel.minus(1).getValue(), 1));
            } else {
                afficherPlanning(dayInfoController.getPlanning(), LocalDate.of(Integer.parseInt(yearLabel.getText()) - 1, 12, 1));
            }
        }
    }

    /**
     * affichage du nombre des taches et projets completés pour un planning archivé
     * @param p le planning à afficher le nb de ses taches et projets completés
     * @param dateArchivage la date de l'archivage du planning
     */
    public void afficherNbCompletes (Planning p ,LocalDateTime dateArchivage){
        nbTachesCompletees.setText((p == null) ? "0" : String.valueOf(p.getNbTachesCompletees()));
        nbProjetCompletes.setText(String.valueOf(dayInfoController.getUser().getHistorique().getNbProjetsCompletes(dateArchivage)));
    }

    /**
     * afficher le nombre des badges gagnés associés à un planning
     * @param planning le planning a afficher ses bades gagnés
     */
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

    /**
     * ajout du boutton "aucun planning" au conteneur de la liste des plannings affichés
     */
    private void afficherAucunPlanning() {
        Button button = new Button("Vous n'avez aucun planning archivé !");
        button.getStyleClass().add("btnAucunPlanning");
        historiquePlannings.getChildren().add(button);
    }

    /**
     * Classe interne pour representer une journée du calendrier du planning associé dans dayInfoController
     */
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
    //endregion
}
