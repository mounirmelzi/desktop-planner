package com.example.controllers.profile;

import com.example.controllers.Controller;
import com.example.controllers.SidebarController;
import com.example.core.Badge;
import com.example.core.Category;
import com.example.core.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProfileController extends Controller implements Initializable {
    @FXML
    private ImageView goodBadgeImageView, veryGoodBadgeImageView, excellentBadgeImageView;
    @FXML
    private TextField pseudoTextField, nombreTachesTextField;
    @FXML
    private Spinner<Integer> hoursSpinner, minutesSpinner, secondsSpinner;
    @FXML
    private Label rendemmentLabel, jourRentableLabel, goodBadgeLabel, veryGoodBadgeLabel, excellentBadgeLabel;
    @FXML
    private VBox categoriesContainer;

    private final User user;
    private final SidebarController sidebarController;

    public ProfileController(@NotNull User user, SidebarController sidebarController) {
        this.user = user;
        this.sidebarController = sidebarController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        goodBadgeImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/goodBadge.png"))));
        veryGoodBadgeImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/veryGoodBadge.png"))));
        excellentBadgeImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/excellentBadge.png"))));
        pseudoTextField.setText(user.getPseudo());
        nombreTachesTextField.setText(String.valueOf(user.getNbrTachesMinParJour()));

        hoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, user.getDureeCreneauLibreMin().toHoursPart()));
        minutesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, user.getDureeCreneauLibreMin().toMinutesPart()));
        secondsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, user.getDureeCreneauLibreMin().toSecondsPart()));

        String rendementMoyen, dayPlusRentable;

        try {
            rendementMoyen = new DecimalFormat("###.##").format(user.getPlanning().rendementMoyen() * 100);
        } catch (NullPointerException e) {
            rendementMoyen = "00.00";
        }

        try {
            dayPlusRentable = user.getPlanning().dayPlusRentable().getDate().toString();
        } catch (NullPointerException e) {
            dayPlusRentable = "Aucun";
        }

        rendemmentLabel.setText("Moyenne rendemment: " + rendementMoyen + "%");
        jourRentableLabel.setText("Jour plus rentable: " + dayPlusRentable);

        updateCategories();

        if (user.hasPlanning()) {
            goodBadgeLabel.setText(String.valueOf(user.getPlanning().getBadges().get(Badge.GOOD)));
            veryGoodBadgeLabel.setText(String.valueOf(user.getPlanning().getBadges().get(Badge.VERY_GOOD)));
            excellentBadgeLabel.setText(String.valueOf(user.getPlanning().getBadges().get(Badge.EXCELLENT)));
        } else {
            goodBadgeLabel.setText("0");
            veryGoodBadgeLabel.setText("0");
            excellentBadgeLabel.setText("0");
        }
    }

    private void updateCategories() {
        categoriesContainer.getChildren().clear();
        for (String category : user.getCategories()) {
            categoriesContainer.getChildren().add(new CategorieBox(user.getCategorie(category)));
        }
    }

    @FXML
    private void handleEditPseudo() {
        if (user.setPseudo(pseudoTextField.getText())) {
            sidebarController.update();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Edit failed");
            alert.setHeaderText("Pseudo can't be modified");
            alert.setContentText("Ce pseudo existe déjà !!");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleEditCreneau() {
        Duration duration = Duration
                .ofHours(hoursSpinner.getValue())
                .plusMinutes(minutesSpinner.getValue())
                .plusSeconds(secondsSpinner.getValue());

        if (duration.isZero() || duration.isNegative()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Edit failed");
            alert.setHeaderText("Durée min d'un créneau libre can't be modified");
            alert.setContentText("Vous avez entrer une durée invalide !!");
            alert.showAndWait();
            return;
        }

        user.setDureeCreneauLibreMin(duration);
    }

    @FXML
    private void handleEditJournee() {
        try {
            int nbTaches = Integer.parseInt(nombreTachesTextField.getText());
            if (nbTaches < 0)
                throw new NumberFormatException();

            user.setNbrTachesMinParJour(nbTaches);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Edit failed");
            alert.setHeaderText("Nombre de tache can't be modified");
            alert.setContentText("Vous avez entrer un nombre invalide !!");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAddCategoryButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/profile/AddCategoryPopup.fxml"));
            loader.setControllerFactory(param -> new AddCategoryPopupController(user));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Add Category");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();

            updateCategories();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class CategorieBox extends HBox {
        public CategorieBox(@NotNull Category category) {
            this.setAlignment(Pos.CENTER);
            this.setMaxWidth(150);

            String bgColor = category.getColor();
            Color color = Color.web(bgColor);
            String textColor = (((0.2126 * color.getRed()) + (0.7152 * color.getGreen()) + (0.0722 * color.getBlue())) > 0.5) ? "black" : "white";

            Label label = new Label(category.getName());
            label.setStyle("-fx-text-fill: " + textColor + ";");
            label.setWrapText(true);
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
            this.setStyle("-fx-background-radius: 10; -fx-font-size: 18; -fx-font-weight: bold; -fx-padding: 10 15;");
            this.setStyle("-fx-background-color: " + bgColor + "; " + this.getStyle());

            this.getChildren().add(label);
        }
    }
}
