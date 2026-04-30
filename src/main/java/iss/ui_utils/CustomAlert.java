package iss.ui_utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomAlert {
    public static void showMessage(String title, String message, boolean isError) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        window.initStyle(StageStyle.UNDECORATED);

        VBox layout = new VBox(20);
        String borderColor = isError ? "#e74c3c" : "#006ddb";
        layout.setStyle("-fx-background-color: white; " +
                "-fx-border-color: " + borderColor + "; " +
                "-fx-border-width: 4px;");
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30, 40, 30, 40));

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: " + borderColor + "; -fx-font-size: 22px; -fx-font-weight: bold;");

        Label msgLabel = new Label(message);
        msgLabel.setStyle("-fx-text-fill: #555555; -fx-font-size: 16px; -fx-text-alignment: center;");
        msgLabel.setWrapText(true);

        Button closeButton = new Button("OK");
        closeButton.setStyle("-fx-background-color: " + borderColor + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8px 30px; " +
                "-fx-cursor: hand;");

        closeButton.setOnAction(e -> window.close());

        layout.getChildren().addAll(titleLabel, msgLabel, closeButton);
        Scene scene = new Scene(layout);
        window.setScene(scene);

        window.showAndWait();
    }
}