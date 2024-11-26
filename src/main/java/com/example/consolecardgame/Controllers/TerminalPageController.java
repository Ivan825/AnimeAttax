package com.example.consolecardgame.Controllers;

import com.example.consolecardgame.Controllers.GameSettings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class TerminalPageController {

    @FXML
    private TextArea terminalTextArea;

    @FXML
    private TextField inputTextField;

    @FXML
    private Button exitButton;

    @FXML
    private Slider musicVolumeSlider;

    private GameSettings gameSettings;

    /**
     * Command history for navigating with arrow keys.
     */
    private ArrayList<String> commandHistory = new ArrayList<>();
    private int historyIndex = -1;

    /**
     * Media and MediaPlayer for background music.
     */
    private Media media;
    private MediaPlayer mediaPlayer;

    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Handle Up and Down arrow keys for command history
        inputTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.UP) {
                if (historyIndex < commandHistory.size() - 1) {
                    historyIndex++;
                    inputTextField.setText(commandHistory.get(commandHistory.size() - 1 - historyIndex));
                    inputTextField.positionCaret(inputTextField.getText().length());
                }
                event.consume();
            } else if (event.getCode() == KeyCode.DOWN) {
                if (historyIndex > 0) {
                    historyIndex--;
                    inputTextField.setText(commandHistory.get(commandHistory.size() - 1 - historyIndex));
                    inputTextField.positionCaret(inputTextField.getText().length());
                } else {
                    historyIndex = -1;
                    inputTextField.clear();
                }
                event.consume();
            }
        });

        // Initialize background music
        initializeBackgroundMusic();

        // Bind slider to music volume
        musicVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue() / 100.0); // Slider is 0-100, MediaPlayer volume is 0.0-1.0
            }
        });
    }

    /**
     * Initializes and starts the background music.
     */
    private void initializeBackgroundMusic() {
        try {
            // Path to the music file
            // Ensure the music file is located at src/main/resources/com/example/consolecardgame/Audio/background.mp3
            URL resource = getClass().getResource("/com/example/consolecardgame/Music/InGameBackground.mp3");
            if (resource == null) {
                appendToTerminal("Background music file not found.");
                return;
            }
            String musicPath = resource.toExternalForm();
            media = new Media(musicPath);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music
            mediaPlayer.setVolume(musicVolumeSlider.getValue() / 100.0); // Set initial volume
            mediaPlayer.play();
            appendToTerminal("Background music started.");
        } catch (Exception e) {
            appendToTerminal("Error initializing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles user input from the TextField.
     */
    @FXML
    private void handleUserInput() {
        String userInput = inputTextField.getText().trim();
        if (!userInput.isEmpty()) {
            appendToTerminal(">> " + userInput);
            commandHistory.add(userInput);
            historyIndex = -1;
            // gameLogic.handleInput(userInput); // Uncomment when GameLogic is implemented
            inputTextField.clear();
        }
    }

    /**
     * Appends text to the terminal TextArea.
     *
     * @param text The text to append.
     */
    public void appendToTerminal(String text) {
        terminalTextArea.appendText(text + "\n");
        // Auto-scroll to the bottom
        terminalTextArea.setScrollTop(Double.MAX_VALUE);
    }

    /**
     * Sets the GameSettings object and initializes GameLogic.
     *
     * @param settings The GameSettings object.
     */
    public void setGameSettings(GameSettings settings) {
        this.gameSettings = settings;
        // Initialize GameLogic if needed
        // this.gameLogic = new GameLogic(gameSettings, this);
        // this.gameLogic.startGame();

        // Append greeting message with player names
        String greeting = "Hello, " + gameSettings.getPlayer1Name() + " and " + gameSettings.getPlayer2Name() + "!";
        greeting += "\nWelcome to Anime Attax.";
        appendToTerminal(greeting);
    }

    /**
     * Handles the Exit Game button action.
     */
    @FXML
    private void handleExitGame() {
        try {
            // Stop the background music
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            // Load HomePage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/consolecardgame/FXML/HomePageFXML.fxml"));
            Parent homePageRoot = loader.load();

            // Optionally, you can get the controller of HomePage and pass data if needed
            // HomePageController homeController = loader.getController();
            // homeController.setSomeData(someData);

            // Get the current stage from the exit button
            Stage stage = (Stage) exitButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(homePageRoot);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            appendToTerminal("Failed to exit the game.");
            // Optionally, show an alert to the user
            // showAlert("Failed to exit the game.");
        }
    }
}
