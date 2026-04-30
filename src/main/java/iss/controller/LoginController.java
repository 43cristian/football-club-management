package iss.controller;

import iss.exceptions.AuthenticationException;
import iss.model.User;
import iss.model.UserType;
import iss.service.ClubService;
import iss.service.PlayerService;
import iss.service.UserService;
import iss.ui_utils.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    private UserService userService;
    private PlayerService playerService;
    private ClubService clubService;

    public void setData(UserService userService, PlayerService playerService, ClubService clubService) {
        this.userService = userService;
        this.playerService = playerService;
        this.clubService = clubService;
        setGUI();
    }

    public void setGUI() {
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            try {
                User loggedUser = userService.login(username, password);

                if (loggedUser.getUserType() == UserType.ADMIN) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin-view.fxml"));
                        Scene nextScene = new Scene(loader.load());

                        Stage adminStage = new Stage();
                        adminStage.setTitle("Football Club Management - Admin Dashboard");
                        adminStage.setScene(nextScene);
                        adminStage.show();

                        AdminDashboardController controller = (AdminDashboardController) loader.getController();
                        controller.setData(userService, clubService, playerService);
                        Stage currentStage = (Stage) usernameField.getScene().getWindow();
                        currentStage.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main-dashboard-view.fxml"));
                        Scene nextScene = new Scene(loader.load());

                        Stage coachStage = new Stage();
                        coachStage.setTitle("Football Club Management");
                        coachStage.setScene(nextScene);
                        coachStage.show();

                        MainDashboardController controller = (MainDashboardController) loader.getController();
                        controller.setData(userService, clubService, playerService, loggedUser);
                        Stage currentStage = (Stage) usernameField.getScene().getWindow();
                        currentStage.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (AuthenticationException e) {
                CustomAlert.showMessage("Error", e.getMessage(), true);
            }
        });
    }
}