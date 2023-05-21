package com.example.controllers.project;

import com.example.controllers.Controller;
import com.example.core.Project;
import com.example.core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddProjectPopupController extends Controller {
    @FXML
    private TextField nomProjectTextField;
    @FXML
    private TextArea descriptionProjectTextArea;

    private final User user;

    public AddProjectPopupController(User user) {
        this.user = user;
    }

    @FXML
    private void handleAddProjectButtonAction(ActionEvent event) {
        String name = nomProjectTextField.getText();
        String description = descriptionProjectTextArea.getText();

        if (name == null || description == null || name.length() == 0 || description.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Project creation failed");
            alert.setHeaderText("You have entered invalid information");
            alert.setContentText("Merci de remplir tous les champs");
            alert.showAndWait();
            return;
        }

        Project project = new Project(name, description);
        user.addProject(project);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
