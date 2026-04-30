package iss.controller;

import iss.model.*;
import iss.service.ClubService;
import iss.service.PlayerService;
import iss.service.UserService;
import iss.ui_utils.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerProfileController {
    @FXML
    private Label usernameLabel;
    @FXML private Label userTypeLabel;
    @FXML private Label clubNameLabel;
    @FXML private Label clubNationLabel;
    @FXML private ImageView clubLogoView;
    @FXML private Button backBtn;
    @FXML private Button saveBtn;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label birthDateLabel;
    @FXML private Label shirtNumberLabel;
    @FXML private Label nationalityLabel;
    @FXML private Label positionsLabel;
    private List<Integer> usedNumbers = new ArrayList<>();
//    @FXML private Label startDateLabel;
//    @FXML private Label endDateLabel;
//    @FXML private Label wageLabel;
//    @FXML private Label bonusLabel;
//    @FXML private Label bonusDescLabel;

    private UserService userService;
    private ClubService clubService;
    private PlayerService playerService;
    private User thisUser;
    private Player thisPlayer;

    public void setData(UserService userService,  ClubService clubService, PlayerService playerService, User thisUser, Player thisPlayer, List<Integer>  usedNumbers) {
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
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/squad-list-view.fxml"));
                javafx.scene.Parent nextView = loader.load();

                Stage currentStage = (Stage) backBtn.getScene().getWindow();

                SquadListController controller = loader.getController();
                controller.setData(userService, clubService, playerService, thisUser);

                currentStage.setScene(new Scene(nextView));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        saveBtn.setOnAction(event -> {
            if (thisUser.getUserType() == UserType.SPORTING_DIRECTOR) {
                CustomAlert.showMessage("Error", "Sporting directors can't update profiles yet", true);
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/update-tech-view.fxml"));
                javafx.scene.Parent nextView = loader.load();

                Stage currentStage = (Stage) backBtn.getScene().getWindow();

                UpdateTechController controller = loader.getController();
                controller.setData(userService, clubService, playerService, thisUser, thisPlayer, usedNumbers);

                currentStage.setScene(new Scene(nextView));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        firstNameLabel.setText(thisPlayer.getFirstName());
        lastNameLabel.setText(thisPlayer.getLastName());
        birthDateLabel.setText(thisPlayer.getBirthDate().toString());
        shirtNumberLabel.setText(String.valueOf(thisPlayer.getShirtNumber()));
        nationalityLabel.setText(thisPlayer.getNationality());

        String pos = thisPlayer.getPositionList().stream()
                .map(Position::name)
                .collect(Collectors.joining(", "));
        positionsLabel.setText(pos);
    }
}
