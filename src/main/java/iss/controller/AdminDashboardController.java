package iss.controller;

import iss.model.Club;
import iss.model.User;
import iss.model.UserType;
import iss.service.ClubService;
import iss.service.PlayerService;
import iss.service.UserService;
import iss.ui_utils.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class AdminDashboardController {
    @FXML private TextField nameField;
    @FXML private TextField nationField;
    @FXML private Button addLogoBtn;
    @FXML private Label logoPathLabel;
    @FXML private ColorPicker primaryColorPicker;
    @FXML private ColorPicker secondaryColorPicker;
    @FXML private Button saveClubBtn;
    @FXML private TableView<Club> clubsTable;
    @FXML private TableColumn<Club, String> colName;
    @FXML private TableColumn<Club, String> colNation;
    @FXML private TableView<User> accountsTable;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, UserType> colUserType;
    @FXML private TextField accUserField;
    @FXML private PasswordField accPassField;
    @FXML private ComboBox<UserType> userTypeBox;
    @FXML private Button addAccBtn;
    @FXML private Button updateAccBtn;
    @FXML private Button deleteAccBtn;
    private ObservableList<Club> clubsData = FXCollections.observableArrayList();
    private ObservableList<User> accountsData = FXCollections.observableArrayList();

    private UserService userService;
    private ClubService clubService;
    private PlayerService playerService;

    private String selectedPath;

    public void setData (UserService userService, ClubService clubService, PlayerService playerService) {
        this.userService = userService;
        this.clubService = clubService;
        this.playerService = playerService;
        setGUI();
    }

    public void setGUI() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNation.setCellValueFactory(new PropertyValueFactory<>("nation"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colUserType.setCellValueFactory(new PropertyValueFactory<>("userType"));

        clubsTable.setItems(clubsData);
        accountsTable.setItems(accountsData);

        userTypeBox.setItems(FXCollections.observableArrayList(UserType.COACH, UserType.SPORTING_DIRECTOR));

        saveClubBtn.setOnAction(event -> {
            try {
                String name = nameField.getText();
                String nation = nationField.getText();

                String primaryColor = primaryColorPicker.getValue().toString().substring(2, 8).toUpperCase();
                String secondaryColor = secondaryColorPicker.getValue().toString().substring(2, 8).toUpperCase();
                String savedPath = saveImageToResources();
                Club savedClub = clubService.addClub(name, nation, savedPath, primaryColor, secondaryColor);
                clubsData.add(savedClub);
                CustomAlert.showMessage("Success", "Club added", false);
                nameField.clear();
                nationField.clear();
                selectedPath = "";
                logoPathLabel.setText("");
            } catch (Exception e) {
                CustomAlert.showMessage("Error", e.getMessage(), true);
                e.printStackTrace();
            }
        });

        addLogoBtn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select club logo");

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
            );

            Stage stage = (Stage) logoPathLabel.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                this.selectedPath = selectedFile.getAbsolutePath();
                logoPathLabel.setText(selectedFile.getName());

            } else {
                CustomAlert.showMessage("Error", "Logo file is null", true);
            }
        });

        clubsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadUsersForClub(newSelection);
            } else {
                accountsData.clear();
            }
        });

        accountsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                accUserField.setText(newSelection.getUsername());
                userTypeBox.setValue(newSelection.getUserType());
            }
        });

        addAccBtn.setOnAction(event -> {
            Club selectedClub = clubsTable.getSelectionModel().getSelectedItem();
            if (selectedClub == null) {
                CustomAlert.showMessage("Error", "No club selected", true);
                return;
            }

            try {
                String username = accUserField.getText();
                String password = accPassField.getText();

                UserType selectedType = userTypeBox.getValue();

                if (selectedType == null) {
                    CustomAlert.showMessage("Error", "No user type selected", true);
                    return;
                }

                User newUser = userService.addUser(username, password, selectedType, selectedClub);

                accountsData.add(newUser);

                accUserField.clear();
                accPassField.clear();
                CustomAlert.showMessage("Success", "User added", false);
            } catch (Exception e) {
                CustomAlert.showMessage("Error", e.getMessage(), true);
            }
        });

        deleteAccBtn.setOnAction(event -> {
            User selectedUser = accountsTable.getSelectionModel().getSelectedItem();
            if (selectedUser == null) {
                CustomAlert.showMessage("Error", "No user selected", true);
                return;
            }

            try {
                userService.deleteUser(selectedUser.getId());

                accountsData.remove(selectedUser);

                accUserField.clear();
                accPassField.clear();
                CustomAlert.showMessage("Success", "User deleted", false);
            } catch (Exception e) {
                CustomAlert.showMessage("Error", e.getMessage(), true);
            }
        });

        updateAccBtn.setOnAction(event -> {
            User selectedUser = accountsTable.getSelectionModel().getSelectedItem();
            if (selectedUser == null) {
                CustomAlert.showMessage("Error", "No user selected", true);
                return;
            }

            try {
                String newUsername = accUserField.getText();
                String newPassword = accPassField.getText();
                UserType newType = userTypeBox.getValue();

                if (newType == null) {
                    CustomAlert.showMessage("Error", "No user type selected", true);
                    return;
                }

                User updatedUser = new User(selectedUser.getId(), newUsername, newPassword, newType, selectedUser.getClub());
                userService.updateUser(updatedUser);

                selectedUser.setUsername(newUsername);
                selectedUser.setUserType(newType);
                accountsTable.refresh();

                CustomAlert.showMessage("Success", "User updated", false);

                accUserField.clear();
                accPassField.clear();
                userTypeBox.getSelectionModel().clearSelection();

            } catch (Exception e) {
                CustomAlert.showMessage("Error", e.getMessage(), true);
            }
        });

        loadClubs();
    }

    private String saveImageToResources() throws IOException {
        if (selectedPath == null || selectedPath.isEmpty()) {
            return "default_logo.png";
        }

        File sourceFile = new File(selectedPath);
        File destDir = new File("src/main/resources/logos");
        String fileName = nameField.getText().replaceAll("\\s+", "_") + ".png";
        File destFile = new File(destDir, fileName);

        java.nio.file.Files.copy(sourceFile.toPath(), destFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        return destFile.getAbsolutePath();
    }

    public void loadClubs() {
        clubsData.setAll(clubService.getAllClubs());
    }

    public void loadUsersForClub(Club club) {
        accountsData.setAll(userService.getUsersByClub(club));
    }
}
