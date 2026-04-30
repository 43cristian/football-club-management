package iss.controller;

import iss.model.*;
import iss.service.ClubService;
import iss.service.PlayerService;
import iss.service.UserService;
import iss.ui_utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddPlayerController {
    @FXML private Label usernameLabel;
    @FXML private Label userTypeLabel;
    @FXML private Label clubNameLabel;
    @FXML private Label clubNationLabel;
    @FXML private ImageView clubLogoView;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker birthDatePicker;
    @FXML private TextField shirtNumberField;
    @FXML private TextField nationalityField;
    @FXML private ListView<Position> positionsListView;
    @FXML private Button saveBtn;
    @FXML private Button backBtn;

    private UserService userService;
    private ClubService clubService;
    private PlayerService playerService;
    private User thisUser;

    public void setData(UserService userService,  ClubService clubService, PlayerService playerService, User thisUser) {
        this.userService = userService;
        this.clubService = clubService;
        this.playerService = playerService;
        this.thisUser = thisUser;
        setGUI();
    }

    private void setGUI() {
        usernameLabel.setText(this.thisUser.getUsername());
        userTypeLabel.setText(this.thisUser.getUserType().toString());
        Club thisClub = thisUser.getClub();

        clubNameLabel.setText(thisClub.getName());
        clubNationLabel.setText(thisClub.getNation());

        try {
            String path = thisClub.getLogoPath();
            File file = new File(path);
            if (file.exists()) {
                clubLogoView.setImage(new Image(file.toURI().toString()));
            }
        } catch (Exception e) {
            CustomAlert.showMessage("Error", e.getMessage(), true);
        }

        positionsListView.setItems(FXCollections.observableArrayList(Position.values()));
        positionsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        saveBtn.setOnAction(event -> {
            try {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                LocalDate birthDate = birthDatePicker.getValue();
                String shirtText = shirtNumberField.getText();
                int shirtNumber = Integer.parseInt(shirtText);
                String nationality = nationalityField.getText();
                List<Position> selectedPositions = new ArrayList<>(positionsListView.getSelectionModel().getSelectedItems());

                Player p = new Player(null, firstName, lastName, birthDate, shirtNumber, selectedPositions, nationality, thisUser.getClub());
                System.out.println(p);
                playerService.addPlayer(firstName, lastName, birthDate, shirtNumber, selectedPositions, nationality, thisUser.getClub());
                CustomAlert.showMessage("Success", "Player added", false);
                goBack();
            } catch (NumberFormatException e) {
                CustomAlert.showMessage("Error", "Shirt number must be a valid number", true);
            } catch (RuntimeException e) {
                CustomAlert.showMessage("Error", e.getMessage(), true);
            }
        });

        backBtn.setOnAction(e -> goBack());
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/squad-list-view.fxml"));
            Parent root = loader.load();

            SquadListController controller = loader.getController();
            controller.setData(userService, clubService, playerService, thisUser);

            Stage currentStage = (Stage) firstNameField.getScene().getWindow();
            currentStage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
