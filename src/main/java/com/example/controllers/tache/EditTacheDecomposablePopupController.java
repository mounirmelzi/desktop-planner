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

public class EditTacheDecomposablePopupController extends Controller implements Initializable {
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

    private static final String NO_CATEGORY_SELECTED = "No Category";
    private final TacheDecomposable tacheDecomposable;
    private final User user;
    private final Project projectOfTache;

    public EditTacheDecomposablePopupController(TacheDecomposable tache, User user, Project projectOfTache) {
        this.tacheDecomposable = tache;
        this.user = user;
        this.projectOfTache = projectOfTache;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
        blockFields();
    }

    private void init() {
        nomTacheTextField.setText(tacheDecomposable.getNom());
        deadlineDatePicker.setValue(tacheDecomposable.getDeadline().toLocalDate());
        deadlineHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, tacheDecomposable.getDeadline().toLocalTime().getHour()));
        deadlineMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, tacheDecomposable.getDeadline().toLocalTime().getMinute()));
        deadlineSecondSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, tacheDecomposable.getDeadline().toLocalTime().getSecond()));

        durationDaysSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 360, (int)tacheDecomposable.getDuree().toDaysPart()));
        durationHoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, tacheDecomposable.getDuree().toHoursPart()));
        durationMinutesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, tacheDecomposable.getDuree().toMinutesPart()));
        durationSecondsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, tacheDecomposable.getDuree().toSecondsPart()));

        stateComboBox.setItems(FXCollections.observableArrayList(Arrays.stream(State.values()).map(State::getName).collect(Collectors.toList())));
        stateComboBox.setValue(tacheDecomposable.getState().getName());

        priorityComboBox.setItems(FXCollections.observableArrayList(Arrays.stream(Priority.values()).map(Priority::getName).collect(Collectors.toList())));
        priorityComboBox.setValue(tacheDecomposable.getPriority().getName());

        ObservableList<String> categories = FXCollections.observableArrayList(new ArrayList<>(user.getCategories()));
        categories.add(0, NO_CATEGORY_SELECTED);
        categoryComboBox.setItems(categories);
        categoryComboBox.setValue(tacheDecomposable.getCategory() == null ? categories.get(0) : tacheDecomposable.getCategory().getName());
    }

    private void blockFields() {
        if (!tacheDecomposable.isUnscheduled()) {
            blockDeadline();
            blockDuration();
            priorityComboBox.setDisable(true);
        }

        if (projectOfTache != null) {
            if (!projectOfTache.isUnscheduled() || !tacheDecomposable.isUnscheduled()) {
                blockDeadline();
                blockDuration();
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

            if (!deadline.equals(tacheDecomposable.getDeadline()) && deadline.isBefore(LocalDateTime.now())) {
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

            if (projectOfTache == null) {
                user.getTaches().remove(tacheDecomposable);
                tacheDecomposable.setNom(nom);
                tacheDecomposable.setPriority(priority);
                tacheDecomposable.setState(state, user.getPlanning());
                tacheDecomposable.setCategory(category);
                tacheDecomposable.setDuree(duration);
                tacheDecomposable.setDeadline(deadline);
                user.addTache(tacheDecomposable);
            } else {
                //projectOfTache.getTaches().remove(tacheDecomposable);
                tacheDecomposable.setNom(nom);
                tacheDecomposable.setPriority(priority);
                tacheDecomposable.setState(state, user.getPlanning());
                tacheDecomposable.setCategory(category);
                tacheDecomposable.setDuree(duration);
                tacheDecomposable.setDeadline(deadline);
                //projectOfTache.addTache(tacheDecomposable);
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
