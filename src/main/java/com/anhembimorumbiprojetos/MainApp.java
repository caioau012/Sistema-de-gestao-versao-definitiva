package com.anhembimorumbiprojetos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
	private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void changeScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao trocar de cena para: " + fxmlPath);
        }
    }

    @Override
    public void start(Stage stage) {
        setPrimaryStage(stage);
        changeScene("/view/dashboard.fxml", "Dashboard");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
