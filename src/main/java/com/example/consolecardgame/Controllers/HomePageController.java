package com.example.consolecardgame.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    @FXML
    private Button StartButton, ExitButton;

    @FXML
    private MediaPlayer mediaPlayer;

    @FXML
    private Slider VolumeSlider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Any initialization code here
        // Load the background music
        String musicPath = getClass().getResource("/com/example/consolecardgame/Music/BGMusic1.mp3").toExternalForm();
        Media media = new Media(musicPath);
        mediaPlayer = new MediaPlayer(media);

        // Play the music in a loop
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        // Set initial volume
        mediaPlayer.setVolume(VolumeSlider.getValue() / 100.0);

        // Bind the volume of the mediaPlayer to the slider's value
        VolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlayer.setVolume(newValue.doubleValue() / 100.0);
        });
    }

    @FXML
    private void handleStartGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/consolecardgame/FXML/GameSettingsFXML.fxml"));
            Parent gameSettingsRoot = loader.load();

            // Set the new scene
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(gameSettingsRoot);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, display an error dialog
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        // Close the application
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
        mediaPlayer.stop();
    }
}
