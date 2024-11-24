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

    @FXML
    private StackPane rootPane;

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

        // Initialize Spinner with values from 1 to 10
        roundsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 3));

        // Disable back button on the first step
        backButton.setDisable(true);
        // Load images
        nextIcon = new Image(getClass().getResourceAsStream("/com/example/consolecardgame/Images/Backgrounds/Next.png"));
        startGameIcon = new Image(getClass().getResourceAsStream("/com/example/consolecardgame/Images/Backgrounds/StartGameb.png"));

        // Set initial icon for nextButtonImage
        nextButtonImage.setImage(nextIcon);

        if (nextButtonImage == null) {
            System.out.println("nextButtonImage is null");
        } else {
            System.out.println("nextButtonImage is initialized");
        }


        // Set initial step
        updateStep();
    }

    @FXML
    private void handleNext() {
        if (currentStep < totalSteps) {
            if (collectDataFromStep(currentStep)) {
                currentStep++;
                updateStep();
            }
        } else {
            if (collectDataFromStep(currentStep)) {
               // startGame();
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
            // Optionally, display an error dialog
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
                break;
            case 2:
                step2Pane.setVisible(true);
                backButton.setDisable(false);
                break;
            case 3:
                step3Pane.setVisible(true);
                backButton.setDisable(false);
                nextButtonImage.setImage(startGameIcon);
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

    private boolean collectStep1Data() {
        RadioButton selectedMode = (RadioButton) modeToggleGroup.getSelectedToggle();
        if (selectedMode == null) {
            showAlert("Please select a game mode.");
            return false;
        }
        gameSettings.setGameMode(selectedMode.getText());
        return true;
    }

    private boolean collectStep2Data() {
        int rounds = roundsSpinner.getValue();
        gameSettings.setNumberOfRounds(rounds);
        return true;
    }

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

//    private void startGame() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/consolecardgame/FXML/GameScene.fxml"));
//            Parent gameRoot = loader.load();
//
//            // Pass the game settings to the game controller
//            GameController gameController = loader.getController();
//            gameController.initGame(gameSettings);
//
//            // Set the new scene
//            Stage stage = (Stage) rootPane.getScene().getWindow();
//            Scene scene = new Scene(gameRoot);
//            stage.setScene(scene);
//            stage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            showAlert("Failed to start the game.");
//        }
//    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Required");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
