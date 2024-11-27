package com.example.consolecardgame.Controllers;

import com.example.consolecardgame.game.Game;
import com.example.consolecardgame.Controllers.GameSettings;
import com.example.consolecardgame.utility.ConsoleStream;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Controller for the Terminal Page, handling the display of console outputs and user interactions.
 */
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
    private Game game;

    /**
     * Piped streams for communicating with the Game class.
     */
    private PipedInputStream pipedIn;
    private PipedOutputStream pipedOut;
    private Thread gameThread;

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
        // Redirect System.out and System.err to the terminalTextArea
        //gameSettings=new GameSettings();
//        gameSettings.setNumberOfRounds(3);
//        gameSettings.setPlayer1Name("a");
//        gameSettings.setPlayer2Name("b");
        //redirectOutputToTerminal();

        // Initialize piped streams for input redirection
//        try {
//            pipedOut = new PipedOutputStream();
//            pipedIn = new PipedInputStream(pipedOut);
//        } catch (IOException e) {
//            appendToTerminal("Error initializing input streams: " + e.getMessage());
//            e.printStackTrace();
//            return;
//        }
//
//        // Create a Scanner from the pipedIn stream
//        Scanner gameScanner = new Scanner(pipedIn);
//
//        // Initialize the Game instance with appropriate parameters
//        int gameId = 1; // Example game ID
//        int numPlayers = 2; // Example number of players
//        int maxRounds = gameSettings.getNumberOfRounds(); // Example max rounds
//        boolean gameMode = true; // Example game mode (true for Human vs. Computer)
//        game = new Game(gameId, numPlayers, maxRounds, gameMode, gameScanner,gameSettings.getPlayer1Name(),gameSettings.getPlayer2Name());
//
//        // Start the Game in a new thread to prevent blocking the JavaFX Application Thread
//        gameThread = new Thread(game::start);
//        gameThread.setDaemon(true); // Ensures the thread exits when the application closes
//        gameThread.start();
//
//        // Handle Up and Down arrow keys for command history
//        inputTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
//            if (event.getCode() == KeyCode.UP) {
//                if (historyIndex < commandHistory.size() - 1) {
//                    historyIndex++;
//                    inputTextField.setText(commandHistory.get(commandHistory.size() - 1 - historyIndex));
//                    inputTextField.positionCaret(inputTextField.getText().length());
//                }
//                event.consume();
//            } else if (event.getCode() == KeyCode.DOWN) {
//                if (historyIndex > 0) {
//                    historyIndex--;
//                    inputTextField.setText(commandHistory.get(commandHistory.size() - 1 - historyIndex));
//                    inputTextField.positionCaret(inputTextField.getText().length());
//                } else {
//                    historyIndex = -1;
//                    inputTextField.clear();
//                }
//                event.consume();
//            }
//        });

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
     * Redirects System.out and System.err to the terminalTextArea using ConsoleStream.
     */
    private void redirectOutputToTerminal() {
        ConsoleStream consoleStream = new ConsoleStream(terminalTextArea);
        PrintStream printStream = new PrintStream(consoleStream, true);
        System.setOut(printStream);
        System.setErr(printStream);
    }

    /**
     * Handles user input from the TextField.
     * Sends the input to the Game class via the piped output stream.
     */
    @FXML
    private void handleUserInput() {
        String userInput = inputTextField.getText();

        // Append user input to the terminalTextArea, prefixed with ">> "
        // If the input is empty, still append ">> " to indicate Enter was pressed
        if (userInput.isEmpty()) {
            appendToTerminal(">> ");
        } else {
            appendToTerminal(">> " + userInput);
        }

        // Add to command history only if input is not empty
        if (!userInput.trim().isEmpty()) {
            commandHistory.add(userInput);
            historyIndex = -1;
        }

        try {
            // Write the user input followed by a newline to simulate pressing Enter
            pipedOut.write((userInput + "\n").getBytes());
            pipedOut.flush();
        } catch (IOException e) {
            appendToTerminal("Error sending input to game: " + e.getMessage());
            e.printStackTrace();
        }

        // Clear the input field after sending
        inputTextField.clear();
    }

    /**
     * Appends text to the terminal TextArea.
     *
     *  The text to append.
     */
    public void start(){
        redirectOutputToTerminal();
        try {
            pipedOut = new PipedOutputStream();
            pipedIn = new PipedInputStream(pipedOut);
        } catch (IOException e) {
            appendToTerminal("Error initializing input streams: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Create a Scanner from the pipedIn stream
        Scanner gameScanner = new Scanner(pipedIn);

        // Initialize the Game instance with appropriate parameters
        int gameId = 1; // Example game ID
        int numPlayers = 2; // Example number of players
        int maxRounds = gameSettings.getNumberOfRounds(); // Example max rounds
        boolean gameMode = true; // Example game mode (true for Human vs. Computer)
        game = new Game(gameId, numPlayers, maxRounds, gameMode, gameScanner,gameSettings.getPlayer1Name(),gameSettings.getPlayer2Name());

        // Start the Game in a new thread to prevent blocking the JavaFX Application Thread
        gameThread = new Thread(game::start);
        gameThread.setDaemon(true); // Ensures the thread exits when the application closes
        gameThread.start();

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

    }
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
        // Optionally, send initial settings to the Game if needed
        String greeting = "Hello, " + gameSettings.getPlayer1Name() + " and " + gameSettings.getPlayer2Name() + "!";
        greeting += "\nWelcome to Anime Attax.";
        appendToTerminal(greeting);
    }

    /**
     * Handles the Exit Game button action.
     * Stops background music, closes streams, and navigates back to the Home Page.
     */
    @FXML
    private void handleExitGame() {
        try {
            // Stop the background music
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            // Close the piped output stream to signal the Game thread to stop
            if (pipedOut != null) {
                pipedOut.close();
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

    /**
     * Initializes and starts the background music.
     */
    private void initializeBackgroundMusic() {
        try {
            // Path to the music file
            // Ensure the music file is located at src/main/resources/com/example/consolecardgame/Music/InGameBackground.mp3
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
}
