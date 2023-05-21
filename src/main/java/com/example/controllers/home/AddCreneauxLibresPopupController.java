package com.example.controllers.home;

import com.example.controllers.Controller;
import com.example.core.Planning;
import com.example.core.utils.Pair;
import com.example.core.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;

public class AddCreneauxLibresPopupController extends Controller {
    @FXML
    private TextField dimancheStartTime, dimancheEndTime;
    @FXML
    private TextField lundiStartTime, lundiEndTime;
    @FXML
    private TextField mardiStartTime, mardiEndTime;
    @FXML
    private TextField mercrediStartTime, mercrediEndTime;
    @FXML
    private TextField jeudiStartTime, jeudiEndTime;
    @FXML
    private TextField vendrediStartTime, vendrediEndTime;
    @FXML
    private TextField samediStartTime, samediEndTime;

    private Planning planning;

    public AddCreneauxLibresPopupController(Planning planning) {
        this.planning = planning;
    }
    
    @FXML
    private void addCreneauxLibresButtonAction(ActionEvent event) {
        HashMap<DayOfWeek, Pair<LocalTime, LocalTime>> creneauxLibres = new HashMap<>();

        creneauxLibres.put(DayOfWeek.SUNDAY, new Pair<>(
                Utils.stringToLocalTime(dimancheStartTime.getText()),
                Utils.stringToLocalTime(dimancheEndTime.getText())
        ));
        creneauxLibres.put(DayOfWeek.MONDAY, new Pair<>(
                Utils.stringToLocalTime(lundiStartTime.getText()),
                Utils.stringToLocalTime(lundiEndTime.getText())
        ));
        creneauxLibres.put(DayOfWeek.TUESDAY, new Pair<>(
                Utils.stringToLocalTime(mardiStartTime.getText()),
                Utils.stringToLocalTime(mardiEndTime.getText())
        ));
        creneauxLibres.put(DayOfWeek.WEDNESDAY, new Pair<>(
                Utils.stringToLocalTime(mercrediStartTime.getText()),
                Utils.stringToLocalTime(mercrediEndTime.getText())
        ));
        creneauxLibres.put(DayOfWeek.THURSDAY, new Pair<>(
                Utils.stringToLocalTime(jeudiStartTime.getText()),
                Utils.stringToLocalTime(jeudiEndTime.getText())
        ));
        creneauxLibres.put(DayOfWeek.FRIDAY, new Pair<>(
                Utils.stringToLocalTime(vendrediStartTime.getText()),
                Utils.stringToLocalTime(vendrediEndTime.getText())
        ));
        creneauxLibres.put(DayOfWeek.SATURDAY, new Pair<>(
                Utils.stringToLocalTime(samediStartTime.getText()),
                Utils.stringToLocalTime(samediEndTime.getText())
        ));

        planning.ajouterCreneauLibre(creneauxLibres);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    public void applyToAllButtonAction(ActionEvent event) {
        lundiStartTime.setText(dimancheStartTime.getText());
        mardiStartTime.setText(dimancheStartTime.getText());
        mercrediStartTime.setText(dimancheStartTime.getText());
        jeudiStartTime.setText(dimancheStartTime.getText());
        vendrediStartTime.setText(dimancheStartTime.getText());
        samediStartTime.setText(dimancheStartTime.getText());

        lundiEndTime.setText(dimancheEndTime.getText());
        mardiEndTime.setText(dimancheEndTime.getText());
        mercrediEndTime.setText(dimancheEndTime.getText());
        jeudiEndTime.setText(dimancheEndTime.getText());
        vendrediEndTime.setText(dimancheEndTime.getText());
        samediEndTime.setText(dimancheEndTime.getText());
    }
}
