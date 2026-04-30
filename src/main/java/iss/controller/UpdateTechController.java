package iss.controller;

import iss.model.*;
import iss.service.ClubService;
import iss.service.PlayerService;
import iss.service.UserService;
import iss.ui_utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpdateTechController {
    @FXML private Label usernameLabel;
    @FXML private Label userTypeLabel;
    @FXML private Label clubNameLabel;
    @FXML private Label clubNationLabel;
    @FXML private ImageView clubLogoView;
    @FXML private Button backBtn;
    @FXML private Button updateBtn;
    @FXML private Label playerNameHeader;
    @FXML private TextField shirtNumberField;
    @FXML private ComboBox<String> medicalStatusCombo;
    @FXML private ListView<Position> positionsListView;
    private List<Integer> usedNumbers = new ArrayList<>();

    private UserService userService;
    private ClubService clubService;
    private PlayerService playerService;
    private User thisUser;
    private Player thisPlayer;

    public void setData(UserService userService,  ClubService clubService, PlayerService playerService, User thisUser, Player thisPlayer, List<Integer> usedNumbers) {
        this.userService = userService;
        this.clubService = clubService;
        this.playerService = playerService;
        this.thisUser = thisUser;
        this.thisPlayer = thisPlayer;
        this.usedNumbers = usedNumbers;
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

        backBtn.setOnAction(event -> {
            goBack();
        });

        medicalStatusCombo.setItems(FXCollections.observableArrayList(
                "FIT", "RECOVERING", "INJURED"
        ));

        positionsListView.setItems(FXCollections.observableArrayList(Position.values()));
        positionsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        playerNameHeader.setText(thisPlayer.getFirstName() + " " + thisPlayer.getLastName());
        shirtNumberField.setText(String.valueOf(thisPlayer.getShirtNumber()));
        for (Position pos : thisPlayer.getPositionList()) {
            positionsListView.getSelectionModel().select(pos);
        }

        updateBtn.setOnAction(event -> {
            int shirtNumber = -1;
            try {
               shirtNumber = Integer.parseInt(this.shirtNumberField.getText());
            } catch (NumberFormatException e) {
               CustomAlert.showMessage("Error", "Shirt number must be a valid number", true);
            }

            for (Integer n:  usedNumbers) {
                if (shirtNumber == n && n != thisPlayer.getShirtNumber())
                {
                    CustomAlert.showMessage("Error", "Shirt number is already used", true);
                    return;
                }
            }

            List<Position> selectedPositions = new ArrayList<>(positionsListView.getSelectionModel().getSelectedItems());

            thisPlayer.setShirtNumber(shirtNumber);
            thisPlayer.setPositionList(selectedPositions);
            try {
                playerService.updatePlayer(thisPlayer);
                CustomAlert.showMessage("Success", "Player updated", false);
                goBack();
            } catch (RuntimeException e) {
               CustomAlert.showMessage("Error", e.getMessage(), true);
           }
        });
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/player-profile-view.fxml"));
            javafx.scene.Parent nextView = loader.load();

            Stage currentStage = (Stage) backBtn.getScene().getWindow();

            PlayerProfileController controller = loader.getController();
            controller.setData(userService, clubService, playerService, thisUser, thisPlayer, usedNumbers);

            currentStage.setScene(new Scene(nextView));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
