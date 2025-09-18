package com.anhembimorumbiprojetos;

import java.net.URL;

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
    
    public static Stage getPrimaryStage() {
    	return primaryStage;
    }

    public static void changeScene(String fxmlPath, String title) {
        try {
            URL location = MainApp.class.getResource(fxmlPath);
            if (location == null) {
                System.err.println("FXML não encontrado: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(location);
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
        URL test = MainApp.class.getResource("/view/dashboard.fxml");
        System.out.println("URL do FXML: " + test);
        launch(args);
    }
}
