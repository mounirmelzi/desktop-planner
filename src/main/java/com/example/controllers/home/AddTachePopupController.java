package com.example.controllers.home;

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

public class AddTachePopupController extends Controller implements Initializable {
    @FXML
    protected ComboBox<String> categoryComboBox;
    @FXML
    private TextField nomTacheTextField;
    @FXML
    private CheckBox isDecomposableCheckBox;
    @FXML
    private DatePicker deadlineDatePicker;
    @FXML
    private Spinner<Integer> deadlineHourSpinner, deadlineMinuteSpinner, deadlineSecondSpinner;
    @FXML
    private Spinner<Integer> durationDaysSpinner, durationHoursSpinner, durationMinutesSpinner, durationSecondsSpinner;
    @FXML
    private ComboBox<String> priorityComboBox;

    private final User user;

    public AddTachePopupController(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> priorities = FXCollections.observableList(Arrays.stream(Priority.values()).map(Priority::getName).collect(Collectors.toList()));
        priorityComboBox.setItems(priorities);
        priorityComboBox.setValue(priorities.get(0));

        ObservableList<String> categories = FXCollections.observableList(new ArrayList<>(user.getCategories()));
        categoryComboBox.setItems(categories);
        categoryComboBox.setValue(categories.get(0));
    }

    @FXML
    private void handleAddTacheButtonAction(ActionEvent event) {
        try {
            String nom = nomTacheTextField.getText();
            Priority priority = Priority.getByName(priorityComboBox.getValue());
            Category category = user.getCategorie(categoryComboBox.getValue());
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

            user.addTache(
                    isDecomposableCheckBox.isSelected()
                            ?
                            new TacheDecomposable(nom, duration, priority, deadline, category)
                            :
                            new TacheSimple(nom, duration, priority, deadline, category)
            );

            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tache creation failed");
            alert.setHeaderText("You have entered invalid information");
            alert.setContentText("Merci de remplir tous les champs");
            alert.show();
        }
    }
}
