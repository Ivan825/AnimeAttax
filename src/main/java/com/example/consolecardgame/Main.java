package com.example.consolecardgame;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {
    private void showSplashScreen(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/consolecardgame/FXML/SplashScreenFXML.fxml"));
        Scene splashScene = new Scene(loader.load());
        primaryStage.setTitle("My Game");
        primaryStage.setResizable(false);
        primaryStage.setScene(splashScene);
        primaryStage.show();

        // Use PauseTransition to wait for 2 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            // Fade out the splash screen
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), splashScene.getRoot());
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {
                try {
                    showHomePage(primaryStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            fadeOut.play();
        });
        pause.play();
    }

    private void showHomePage(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/consolecardgame/FXML/HomePageFXML.fxml"));
        Scene homeScene = new Scene(loader.load());
        primaryStage.setTitle("My Game");
        primaryStage.setResizable(false);
        primaryStage.setScene(homeScene);
    }
    public void start(Stage primaryStage) throws Exception {
        showSplashScreen(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
