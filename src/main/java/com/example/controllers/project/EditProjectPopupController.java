package com.example.controllers.project;

import com.example.controllers.Controller;
import com.example.core.Project;
import com.example.core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;

public class EditProjectPopupController extends Controller implements Initializable {
    @FXML
    private TextField nomProjectTextField;
    @FXML
    private TextArea descriptionProjectTextArea;

    private final Project project;
    private final User user;

    public EditProjectPopupController(Project project, User user) {
        this.project = project;
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nomProjectTextField.setText(project.getNom());
        descriptionProjectTextArea.setText(project.getDescription());
    }

    @FXML
    private void handleEditProjectButtonAction(@NotNull ActionEvent event) {
        String nom = nomProjectTextField.getText();
        String description = descriptionProjectTextArea.getText();

        user.getProjects().remove(project);
        project.setNom(nom);
        project.setDescription(description);
        user.getProjects().add(project);

        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
