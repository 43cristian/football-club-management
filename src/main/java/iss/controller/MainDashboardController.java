package iss.controller;

import iss.model.Club;
import iss.model.User;
import iss.service.ClubService;
import iss.service.PlayerService;
import iss.service.UserService;
import iss.ui_utils.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;

public class MainDashboardController {
    @FXML private Label usernameLabel;
    @FXML private Label userTypeLabel;
    @FXML private Label clubNameLabel;
    @FXML private Label clubNationLabel;
    @FXML private ImageView clubLogoView;
    @FXML private StackPane squadListBtn;

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

        squadListBtn.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/squad-list-view.fxml"));
                javafx.scene.Parent nextView = loader.load();

                Stage currentStage = (Stage) squadListBtn.getScene().getWindow();

                SquadListController controller = loader.getController();
                controller.setData(userService, clubService, playerService, thisUser);

                currentStage.setScene(new Scene(nextView));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
