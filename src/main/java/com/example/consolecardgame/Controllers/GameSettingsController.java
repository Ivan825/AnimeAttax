package com.example.consolecardgame.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import com.example.consolecardgame.Controllers.GameSettings;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameSettingsController implements Initializable {

    // Removed rootPane since we're obtaining Stage via ActionEvent

    // Step panes
    @FXML
    private AnchorPane step1Pane, step2Pane, step3Pane;

    // Navigation buttons
    @FXML
    private Button backButton, nextButton, exitButton;

    // Step 1 controls
    @FXML
    private RadioButton humanVsComputerRadioButton, humanVsHumanRadioButton;

    @FXML
    private ToggleGroup modeToggleGroup;

    // Step 2 controls
    @FXML
    private Spinner<Integer> roundsSpinner;

    // Step 3 controls
    @FXML
    private TextField player1NameField, player2NameField;
    @FXML
    private ImageView nextButtonImage;

    private Image nextIcon;
    private Image startGameIcon;

    private int currentStep = 1;
    private final int totalSteps = 3;

    // Game settings data model
    private GameSettings gameSettings = new GameSettings();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the ToggleGroup and assign to RadioButtons
        modeToggleGroup = new ToggleGroup();
        humanVsComputerRadioButton.setToggleGroup(modeToggleGroup);
        humanVsHumanRadioButton.setToggleGroup(modeToggleGroup);

        // Optionally, set a default selection
        humanVsHumanRadioButton.setSelected(true);

        // Initialize Spinner with values from 1 to 10, default 3
        roundsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 3));

        // Disable back button on the first step
        backButton.setDisable(true);

        // Load images for the Next/Start button
        try {
            nextIcon = new Image(getClass().getResourceAsStream("/com/example/consolecardgame/Images/Backgrounds/Next.png"));
            startGameIcon = new Image(getClass().getResourceAsStream("/com/example/consolecardgame/Images/Backgrounds/StartGameb.png"));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to load button images.");
        }

        // Set initial icon for nextButtonImage
        if (nextButtonImage != null && nextIcon != null) {
            nextButtonImage.setImage(nextIcon);
        } else {
            System.out.println("nextButtonImage or nextIcon is null");
        }

        // Set initial step
        updateStep();
    }

    @FXML
    private void handleNext(ActionEvent event) {
        if (currentStep < totalSteps) {
            if (collectDataFromStep(currentStep)) {
                currentStep++;
                updateStep();
            }
        } else {
            if (collectDataFromStep(currentStep)) {
                startGame(event);
            }
        }
    }

    @FXML
    private void handleBack() {
        if (currentStep > 1) {
            currentStep--;
            updateStep();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/consolecardgame/FXML/HomePageFXML.fxml"));
            Parent homePageRoot = loader.load();

            // Set the new scene
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(homePageRoot);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load the Home Page.");
        }
    }

    private void updateStep() {
        // Hide all steps
        step1Pane.setVisible(false);
        step2Pane.setVisible(false);
        step3Pane.setVisible(false);

        // Show the current step
        switch (currentStep) {
            case 1:
                step1Pane.setVisible(true);
                backButton.setDisable(true);
                nextButtonImage.setImage(nextIcon); // Ensure the Next icon is set
                break;
            case 2:
                step2Pane.setVisible(true);
                backButton.setDisable(false);
                nextButtonImage.setImage(nextIcon);
                break;
            case 3:
                step3Pane.setVisible(true);
                backButton.setDisable(false);
                nextButtonImage.setImage(startGameIcon); // Change icon to Start Game
                break;
        }
    }

    private boolean collectDataFromStep(int step) {
        switch (step) {
            case 1:
                return collectStep1Data();
            case 2:
                return collectStep2Data();
            case 3:
                return collectStep3Data();
            default:
                return false;
        }
    }
    @FXML
    private boolean collectStep1Data() {
        RadioButton selectedMode = (RadioButton) modeToggleGroup.getSelectedToggle();
        if (selectedMode == null) {
            showAlert("Please select a game mode.");
            return false;
        }
        gameSettings.setGameMode(selectedMode.getText());
        return true;
    }
    @FXML
    private boolean collectStep2Data() {
        int rounds = roundsSpinner.getValue();
        gameSettings.setNumberOfRounds(rounds);
        return true;
    }
    @FXML
    private boolean collectStep3Data() {
        String player1Name = player1NameField.getText().trim();
        String player2Name = player2NameField.getText().trim();

        if (player1Name.isEmpty() || player2Name.isEmpty()) {
            showAlert("Please enter names for both players.");
            return false;
        }

        gameSettings.setPlayer1Name(player1Name);
        gameSettings.setPlayer2Name(player2Name);
        return true;
    }

    /**
     * Starts the game by transitioning to the Terminal Page.
     */
    private void startGame(ActionEvent event) {
        try {
            // Load TerminalPage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/consolecardgame/FXML/TerminalPageFXML.fxml"));
            //bug
            Parent terminalPageRoot= loader.load();



            // Get the controller of TerminalPage

            TerminalPageController terminalController = loader.getController();

            // Ensure gameSettings is not null
            if (gameSettings == null) {
                throw new IllegalStateException("GameSettings is null");
            }

            // Pass the GameSettings object to TerminalPageController
            terminalController.setGameSettings(gameSettings);
            terminalController.start();
            // Obtain the Stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(terminalPageRoot);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to start the game: " + e.getMessage());
        } catch (IllegalStateException e) {
            e.printStackTrace();
            showAlert(e.getMessage());
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Required");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
