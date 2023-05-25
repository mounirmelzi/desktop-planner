package com.example.controllers.tache;

import com.example.controllers.Controller;
import com.example.core.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EditTacheSimplePopupController extends Controller implements Initializable {
    @FXML
    private TextField nomTacheTextField;
    @FXML
    private DatePicker deadlineDatePicker;
    @FXML
    private Spinner<Integer> deadlineHourSpinner, deadlineMinuteSpinner, deadlineSecondSpinner;
    @FXML
    private Spinner<Integer> durationDaysSpinner, durationHoursSpinner, durationMinutesSpinner, durationSecondsSpinner;
    @FXML
    private ComboBox<String> stateComboBox, priorityComboBox, categoryComboBox;
    @FXML
    private Spinner<Integer> periodicitySpinner;

    private static final String NO_CATEGORY_SELECTED = "No Category";
    private final TacheSimple tacheSimple;
    private final User user;
    private final TacheDecomposable parentTache;
    private final Project projectOfTache;

    public EditTacheSimplePopupController(TacheSimple tache, User user, TacheDecomposable parentTache, Project projectOfTache) {
        this.tacheSimple = tache;
        this.user = user;
        this.parentTache = parentTache;
        this.projectOfTache = projectOfTache;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
        blockFields();
    }

    private void init() {
        nomTacheTextField.setText(tacheSimple.getNom());
        deadlineDatePicker.setValue(tacheSimple.getDeadline().toLocalDate());
        deadlineHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, tacheSimple.getDeadline().toLocalTime().getHour()));
        deadlineMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, tacheSimple.getDeadline().toLocalTime().getMinute()));
        deadlineSecondSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, tacheSimple.getDeadline().toLocalTime().getSecond()));

        durationDaysSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 360, (int)tacheSimple.getDuree().toDaysPart()));
        durationHoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, tacheSimple.getDuree().toHoursPart()));
        durationMinutesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, tacheSimple.getDuree().toMinutesPart()));
        durationSecondsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, tacheSimple.getDuree().toSecondsPart()));

        periodicitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 360, tacheSimple.getPeriodicity()));

        stateComboBox.setItems(FXCollections.observableArrayList(Arrays.stream(State.values()).map(State::getName).collect(Collectors.toList())));
        stateComboBox.setValue(tacheSimple.getState().getName());

        priorityComboBox.setItems(FXCollections.observableArrayList(Arrays.stream(Priority.values()).map(Priority::getName).collect(Collectors.toList())));
        priorityComboBox.setValue(tacheSimple.getPriority().getName());

        ObservableList<String> categories = FXCollections.observableArrayList(new ArrayList<>(user.getCategories()));
        categories.add(0, NO_CATEGORY_SELECTED);
        categoryComboBox.setItems(categories);
        categoryComboBox.setValue(tacheSimple.getCategory() == null ? categories.get(0) : tacheSimple.getCategory().getName());
    }

    private void blockFields() {
        if (parentTache != null || !tacheSimple.isUnscheduled()) {
            blockDeadline();
            blockDuration();
            periodicitySpinner.setDisable(true);
            priorityComboBox.setDisable(true);
        }

        if (projectOfTache != null) {
            if (!projectOfTache.isUnscheduled() || !tacheSimple.isUnscheduled()) {
                blockDeadline();
                blockDuration();
                periodicitySpinner.setDisable(true);
                priorityComboBox.setDisable(true);
            }
        }
    }

    private void blockDuration() {
        durationDaysSpinner.setDisable(true);
        durationHoursSpinner.setDisable(true);
        durationMinutesSpinner.setDisable(true);
        durationSecondsSpinner.setDisable(true);
    }

    private void blockDeadline() {
        deadlineDatePicker.setDisable(true);
        deadlineHourSpinner.setDisable(true);
        deadlineMinuteSpinner.setDisable(true);
        deadlineSecondSpinner.setDisable(true);
    }

    @FXML
    private void handleEditTacheButtonAction(ActionEvent event) {
        try {
            String nom = nomTacheTextField.getText();
            Priority priority = Priority.getByName(priorityComboBox.getValue());
            State state = State.getStateByName(stateComboBox.getValue());
            Category category = categoryComboBox.getValue().equals(NO_CATEGORY_SELECTED) ? null : user.getCategorie(categoryComboBox.getValue());
            int periodicity = periodicitySpinner.getValue();
            Duration duration = Duration
                    .ofDays(durationDaysSpinner.getValue())
                    .plusHours(durationHoursSpinner.getValue())
                    .plusMinutes(durationMinutesSpinner.getValue())
                    .plusSeconds(durationSecondsSpinner.getValue());
            LocalDateTime deadline = LocalDateTime.of(
                    deadlineDatePicker.getValue(),
                    LocalTime.of(
                            deadlineHourSpinner.getValue(),
                            deadlineMinuteSpinner.getValue(),
                            deadlineSecondSpinner.getValue()
                    )
            );

            if (!deadline.equals(tacheSimple.getDeadline()) && deadline.isBefore(LocalDateTime.now())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Edit failed");
                alert.setHeaderText("Tache can't be modified");
                alert.setContentText("Le deadline de la tache est dans le passé !!");
                alert.showAndWait();
                return;
            }

            if (duration.isZero() || duration.isNegative()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Edit failed");
                alert.setHeaderText("Tache can't be modified");
                alert.setContentText("La tache n'a pas une durée");
                alert.showAndWait();
                return;
            }

            if (parentTache != null) {
                parentTache.getChildren().remove(tacheSimple);
                tacheSimple.setNom(nom);
                tacheSimple.setPriority(priority);
                tacheSimple.setState(state, user.getPlanning());
                tacheSimple.setCategory(category);
                tacheSimple.setDuree(duration);
                tacheSimple.setDeadline(deadline);
                parentTache.getChildren().add(tacheSimple);
            } else if (projectOfTache != null) {
                projectOfTache.getTaches().remove(tacheSimple);
                tacheSimple.setNom(nom);
                tacheSimple.setPriority(priority);
                tacheSimple.setState(state, user.getPlanning());
                tacheSimple.setCategory(category);
                tacheSimple.setDuree(duration);
                tacheSimple.setDeadline(deadline);
                projectOfTache.addTache(tacheSimple);
            } else {
                user.getTaches().remove(tacheSimple);
                tacheSimple.setNom(nom);
                tacheSimple.setPriority(priority);
                tacheSimple.setState(state, user.getPlanning());
                tacheSimple.setCategory(category);
                tacheSimple.setPeriodicity(periodicity);
                tacheSimple.setDuree(duration);
                tacheSimple.setDeadline(deadline);
                user.addTache(tacheSimple);
            }

            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Edit failed");
            alert.setHeaderText("You have entered invalid information");
            alert.setContentText("Merci de remplir tous les champs");
            alert.showAndWait();
        }
    }
}
