package com.example.consolecardgame.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.animation.FadeTransition;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashScreenController implements Initializable {

    @FXML
    private Node rootPane; // Reference to the root node of your splash screen

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playStartSound();
    }
    private void playStartSound() {
        try {
            // Load the sound file
            String soundPath = getClass().getResource("/com/example/consolecardgame/Music/SplashScreenMusic.wav").toExternalForm();
            AudioClip startSound = new AudioClip(soundPath);

            // Play the sound
            startSound.play();

        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, handle the exception or log it
        }
    }
}
