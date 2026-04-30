package iss;
import iss.controller.LoginController;
import iss.repository.ClubRepository;
import iss.repository.PlayerRepository;
import iss.repository.UserRepository;
import iss.service.ClubService;
import iss.service.PlayerService;
import iss.service.UserService;
import iss.utils.JPAUtil;
import iss.validation.ClubValidator;
import iss.validation.PlayerValidator;
import iss.validation.UserValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private UserService userService;
    private ClubService clubService;
    private PlayerService playerService;
    private UserValidator userValidator;
    private ClubValidator clubValidator;
    private PlayerValidator playerValidator;

    @Override
    public void init() {
        try {
            JPAUtil.getEntityManagerFactory().createEntityManager().close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        UserRepository userRepository = new UserRepository();
        ClubRepository clubRepository = new ClubRepository();
        PlayerRepository playerRepository = new PlayerRepository();

        userValidator = new UserValidator();
        clubValidator = new ClubValidator();
        playerValidator = new PlayerValidator();

        userService = new UserService(userRepository, userValidator);
        clubService = new ClubService(clubRepository, clubValidator);
        playerService = new PlayerService(playerRepository, playerValidator);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login-view.fxml"));
        VBox root = loader.load();

        LoginController controller = loader.getController();
        controller.setData(userService, playerService, clubService);

        Image appIcon = new Image(getClass().getResourceAsStream("/images/fcm-icon.png"));
        stage.getIcons().add(appIcon);

        Scene scene = new Scene(root);
        stage.setTitle("Management Club Fotbal - Login");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        JPAUtil.closeEntityManagerFactory();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
