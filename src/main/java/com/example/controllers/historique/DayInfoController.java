package com.example.controllers.historique;

import com.example.controllers.Controller;
import com.example.core.*;
import com.example.core.Tache;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;

public class DayInfoController extends Controller{
    @FXML
    private VBox ensembleTaches ;
    @FXML
    private Label dateDay;
    @FXML
    private Label nomDay ;
    private User user ;
    private LocalDate date;
    private Day day ;
    private Planning planning ;


    public DayInfoController(User user) {
        this.user = user ;
        ensembleTaches = new VBox() ;
        //day = new Day() ;
    }

    //SETTER, GETTERS
    public void setDay (Day day) {this.day = day ;}
    public void setDate(LocalDate date) {this.date = date ;}
    public void setPlanning(Planning planning ) { this.planning = planning ;}
    public User getUser() {return user;}
    public LocalDate getDate() {return date ;}
    public Day getDay(){return day;}
    public Planning getPlanning(){return planning;}
    //FIN SETTERS, GETTERS

    public void afficherDate() {
        dateDay.setText(getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        nomDay.setText(getDate().getDayOfWeek().name());
    }

    public void afficherAucuneTache() {
        Button button = new Button() ;
        button.setText("Aucune Tache en ce jour!");
        button.getStyleClass().add("borderPaneTache") ;
        ensembleTaches.getChildren().add(button) ;
    }


    public BorderPane afficherTache(CreneauOccupe creneau) {
        try {
        AtomicBoolean clique = new AtomicBoolean(false) ;
        Tache tache = creneau.getTache();

        BorderPane borderPaneTache = new BorderPane();
        borderPaneTache.getStyleClass().add("borderPaneTache") ;

        HBox hbox = new HBox();
        hbox.setStyle("-fx-max-Width: 200px; -fx-min-Height: 50px");
        Label text = new Label("Tache: " + tache.getNom());
        text.setWrapText(true) ;

        hbox.getChildren().add(text) ;
        hbox.setAlignment(Pos.CENTER_LEFT);
        borderPaneTache.setLeft(hbox);

        Button moreButton = new Button("more");
        moreButton.getStyleClass().add("moreButton") ;
        moreButton.setOnMouseClicked(event -> {
            VBox moreInfo = new VBox() ;
            moreInfo.getChildren().add(new Label("Etat: " + tache.getState().getName())) ;
            moreInfo.getChildren().add(new Label("Heure debut : " + creneau.getHeureDebut().toString()));
            moreInfo.getChildren().add(new Label("Heure fin : " + creneau.getHeureFin().toString())) ;
            moreInfo.getChildren().add(new Label("Durée: " + tache.getDuree().toString()));
            moreInfo.getChildren().add(new Label("Catégorie: " + (tache.getCategory() == null ? "No Category" : tache.getCategory().getName())));
            borderPaneTache.setBottom(moreInfo);
            if (clique.get()==false) {moreInfo.setVisible(true); clique.set(true);}
            else {moreInfo.setVisible(false); borderPaneTache.setBottom(null); clique.set(false); }
        });
        borderPaneTache.setRight(moreButton);
        return borderPaneTache ;
        }
        catch (NullPointerException e) {
        afficherAucuneTache();
        e.printStackTrace();
        return  null;}
    }


    public void afficherTache() {

        try {
            if (!(day.hasCreneauxOccupees())) {throw new NullPointerException();}
            for (CreneauOccupe creneau : day.getCreneauxOccupes()) {
                ensembleTaches.getChildren().add(afficherTache(creneau));
            }
        }
        catch (NullPointerException e) {afficherAucuneTache();}
    }
}





