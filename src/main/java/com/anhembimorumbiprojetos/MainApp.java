package com.anhembimorumbiprojetos;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/main.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root, 800, 600);
            
            stage.setTitle("Sistema de Gerenciamento");
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();
            
        } catch (IOException e) {
            showError("Erro ao carregar a interface", "Não foi possível carregar a tela principal: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
    	launch(args);
    }

    public static void changeScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/" + fxmlFile));
            Parent root = loader.load();
            
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (IOException e) {
            showError("Erro ao carregar tela", "Não foi possível carregar a tela: " + fxmlFile + "\n" + e.getMessage());
        }
    }

    public static void openNewWindow(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/" + fxmlFile));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
            
        } catch (IOException e) {
            showError("Erro ao abrir janela", "Não foi possível abrir a janela: " + fxmlFile + "\n" + e.getMessage());
        }
    }

    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText(title);
        alert.setContentText(message);
        
        return alert.showAndWait().get().getText().equals("OK");
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void exit() {
        System.exit(0);
    }
}