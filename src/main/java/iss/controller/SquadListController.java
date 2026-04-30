package iss.controller;

import iss.model.*;
import iss.service.ClubService;
import iss.service.PlayerService;
import iss.service.UserService;
import iss.ui_utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SquadListController {
    @FXML private Button backBtn;
    @FXML private Label usernameLabel;
    @FXML private Label userTypeLabel;
    @FXML private Label clubNameLabel;
    @FXML private Label clubNationLabel;
    @FXML private ImageView clubLogoView;
    @FXML private TableView<Player> squadTable;
    @FXML private TableColumn<Player, Integer> colNumber;
    @FXML private TableColumn<Player, String> colName;
    @FXML private TableColumn<Player, String> colPosition;
    @FXML private TableColumn<Player, String> colNationality;
    @FXML private Button addPlayerBtn;
    private ObservableList<Player> playersData = FXCollections.observableArrayList();

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

        if (thisUser.getUserType() != UserType.SPORTING_DIRECTOR) {
            addPlayerBtn.setVisible(false);
        }

        colNumber.setCellValueFactory(new PropertyValueFactory<>("shirtNumber"));
        colName.setCellValueFactory(cellData -> {
            Player p = cellData.getValue();
            String fullName = p.getFirstName() + " " + p.getLastName();
            return new javafx.beans.property.SimpleStringProperty(fullName);
        });
        colPosition.setCellValueFactory(cellData -> {
            List<Position> positions = cellData.getValue().getPositionList();

            String positionsString = positions.stream()
                    .map(Enum::name)
                    .collect(java.util.stream.Collectors.joining(", "));

            return new javafx.beans.property.SimpleStringProperty(positionsString);
        });
        colNationality.setCellValueFactory(new PropertyValueFactory<>("nationality"));

        squadTable.setItems(playersData);

        squadTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                List<Integer> usedNumbers = new ArrayList<>();
                for (Player p : playersData) {
                    usedNumbers.add(p.getShirtNumber());
                }

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/player-profile-view.fxml"));
                    javafx.scene.Parent nextView = loader.load();

                    Stage currentStage = (Stage) backBtn.getScene().getWindow();

                    PlayerProfileController controller = loader.getController();
                    controller.setData(userService, clubService, playerService, thisUser, newSelection, usedNumbers);

                    currentStage.setScene(new Scene(nextView));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        backBtn.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main-dashboard-view.fxml"));
                javafx.scene.Parent nextView = loader.load();

                Stage currentStage = (Stage) backBtn.getScene().getWindow();

                MainDashboardController controller = loader.getController();
                controller.setData(userService, clubService, playerService, thisUser);

                currentStage.setScene(new Scene(nextView));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        addPlayerBtn.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add-player-view.fxml"));
                javafx.scene.Parent nextView = loader.load();

                Stage currentStage = (Stage) backBtn.getScene().getWindow();

                AddPlayerController controller = loader.getController();
                controller.setData(userService, clubService, playerService, thisUser);

                currentStage.setScene(new Scene(nextView));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loadPlayers();
    }

    private void loadPlayers() {
        if (playerService != null && thisUser.getClub() != null) {
            List<Player> players = playerService.getPlayersByClub(thisUser.getClub());
            players.sort(Comparator.comparingInt(Player::getShirtNumber));
            playersData.setAll(players);
        }
    }
}
