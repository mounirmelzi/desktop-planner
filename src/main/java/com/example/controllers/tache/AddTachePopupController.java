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

public class AddTachePopupController extends Controller implements Initializable {
    @FXML
    protected ComboBox<String> categoryComboBox, priorityComboBox, projectComboBox;
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
    private Spinner<Integer> periodicitySpinner;

    private static final String NO_PROJECT_SELECTED = "No Project";
    private static final String NO_CATEGORY_SELECTED = "No Category";
    private final User user;

    public AddTachePopupController(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> priorities = FXCollections.observableArrayList(Arrays.stream(Priority.values()).map(Priority::getName).collect(Collectors.toList()));
        priorityComboBox.setItems(priorities);
        priorityComboBox.setValue(priorities.size() > 0 ? priorities.get(0) : null);

        ObservableList<String> categories = FXCollections.observableArrayList(new ArrayList<>(user.getCategories()));
        categories.add(0, NO_CATEGORY_SELECTED);
        categoryComboBox.setItems(categories);
        categoryComboBox.setValue(categories.size() > 0 ? categories.get(0) : null);

        ObservableList<String> projects = FXCollections.observableArrayList(user.getProjects().stream().map(Project::getNom).toList());
        projects.add(0, NO_PROJECT_SELECTED);
        projectComboBox.setItems(projects);
        projectComboBox.setValue(projects.size() > 0 ? projects.get(0) : null);

        periodicitySpinner.disableProperty().bind(isDecomposableCheckBox.selectedProperty());
    }

    @FXML
    private void handleAddTacheButtonAction(ActionEvent event) {
        try {
            String nom = nomTacheTextField.getText();
            Priority priority = Priority.getByName(priorityComboBox.getValue());
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

            if (deadline.isBefore(LocalDateTime.now())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Tache Creation failed");
                alert.setHeaderText("Tache can't be created");
                alert.setContentText("Le deadline de la tache est dans le passé !!");
                alert.showAndWait();
                return;
            }

            if (duration.isZero() || duration.isNegative()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Tache Creation failed");
                alert.setHeaderText("Tache can't be created");
                alert.setContentText("La tache n'a pas une durée");
                alert.showAndWait();
                return;
            }

            Project project = projectComboBox.getValue().equals(NO_PROJECT_SELECTED) ? null : user.getProject(projectComboBox.getValue());

            Alert projectScheduledAlert = new Alert(Alert.AlertType.WARNING);
            projectScheduledAlert.setTitle("Tache Creation failed");
            projectScheduledAlert.setHeaderText("Tache can't be added to the project");
            projectScheduledAlert.setContentText("Ce projet est déjà planifié, pour lui ajouter une tache veuillez le déplanifier d'abord");

            if (isDecomposableCheckBox.isSelected()) {
                if (project == null) {
                    if (!user.addTache(new TacheDecomposable(nom, duration, priority, deadline, category))) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Tache creation failed");
                        alert.setHeaderText("This task already existe");
                        alert.setContentText("Changer le nom, le deadline ou la priorité de la tache et réessayer");
                        alert.showAndWait();
                        return;
                    }

                    ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
                    return;
                }

                if (project.hasTaches() && !project.isUnscheduled()) {
                    projectScheduledAlert.showAndWait();
                    return;
                }

                if (!project.addTache(new TacheDecomposable(nom, duration, priority, deadline, category))) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Tache creation failed");
                    alert.setHeaderText("This task already existe");
                    alert.setContentText("Changer les informations de la tache et réessayer");
                    alert.showAndWait();
                    return;
                }

                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            } else {
                TacheSimple tacheSimple = new TacheSimple(nom, duration, priority, deadline, category);
                tacheSimple.setPeriodicity(periodicitySpinner.getValue());

                if (project == null) {
                    if (!user.addTache(tacheSimple)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Tache creation failed");
                        alert.setHeaderText("This task already existe");
                        alert.setContentText("Changer le nom, le deadline ou la priorité de la tache et réessayer");
                        alert.showAndWait();
                        return;
                    }

                    ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
                    return;
                }

                if (project.hasTaches() && !project.isUnscheduled()) {
                    projectScheduledAlert.showAndWait();
                    return;
                }

                if (!project.addTache(tacheSimple)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Tache creation failed");
                    alert.setHeaderText("This task already existe");
                    alert.setContentText("Changer les informations de la tache et réessayer");
                    alert.showAndWait();
                    return;
                }

                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tache creation failed");
            alert.setHeaderText("You have entered invalid information");
            alert.setContentText("Merci de remplir tous les champs");
            alert.showAndWait();
        }
    }
}
