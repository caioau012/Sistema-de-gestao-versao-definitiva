package com.anhembimorumbiprojetos.controller;

import com.anhembimorumbiprojetos.MainApp;

import javafx.fxml.FXML;

public class MainController {

    @FXML
    private void abrirTarefas() {
        MainApp.changeScene("tarefa.fxml", "Gerenciamento de Tarefas");
    }

    @FXML
    private void abrirProjetos() {
        MainApp.changeScene("projeto.fxml", "Gerenciamento de Projetos");
    }

    @FXML
    private void abrirMembros() {
        MainApp.changeScene("membro.fxml", "Gerenciamento de Membros");
    }

    @FXML
    private void abrirEquipes() {
        MainApp.changeScene("equipe.fxml", "Gerenciamento de Equipes");
    }

    @FXML
    private void sair() {
        if (MainApp.showConfirmation("Sair", "Deseja realmente sair do sistema?")) {
            MainApp.exit();
        }
    }
}