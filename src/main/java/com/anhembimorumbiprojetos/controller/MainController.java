package com.anhembimorumbiprojetos.controller;

import com.anhembimorumbiprojetos.MainApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainController {

    @FXML
    public void sair(ActionEvent event) {
        System.out.println("Encerrando aplicação...");
        System.exit(0);
    }

    @FXML
    public void abrirTarefas(ActionEvent event) {
        MainApp.changeScene("/view/tarefa.fxml", "Tarefas");
    }

    @FXML
    public void abrirProjetos(ActionEvent event) {
        MainApp.changeScene("/view/projeto.fxml", "Projetos");
    }

    @FXML
    public void abrirMembros(ActionEvent event) {
        MainApp.changeScene("/view/membro.fxml", "Membros");
    }

    @FXML
    public void abrirEquipes(ActionEvent event) {
        MainApp.changeScene("/view/equipe.fxml", "Equipes");
    }

    @FXML
    public void abrirDashboard(ActionEvent event) {
        MainApp.changeScene("/view/dashboard.fxml", "Dashboard");
    }
}

