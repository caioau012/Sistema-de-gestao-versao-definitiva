package com.anhembimorumbiprojetos.controller;

import com.anhembimorumbiprojetos.MainApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MembroControllerView {
    
	@FXML
    private Label labelStatus;

    @FXML
    public void onVoltarDashboard(ActionEvent event) {
        System.out.println("Voltando ao Dashboard...");
        MainApp.changeScene("view/dashboard.fxml", "Dashboard");
    }

	@FXML
    public void initialize() {
        // Inicializações quando a tela carrega
    }
}
