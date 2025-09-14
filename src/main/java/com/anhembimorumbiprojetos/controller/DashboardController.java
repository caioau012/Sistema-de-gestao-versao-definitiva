package com.anhembimorumbiprojetos.controller;

import com.anhembimorumbiprojetos.MainApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DashboardController {

    @FXML
    public void onDashboard(ActionEvent e) {
        // já está no dashboard, pode recarregar ou ignorar
        MainApp.changeScene("/view/dashboard.fxml", "Dashboard");
    }

    @FXML
    public void onProjetos(ActionEvent e) {
        MainApp.changeScene("/view/projeto.fxml", "Projetos");
    }

    @FXML
    public void onTarefas(ActionEvent e) {
        MainApp.changeScene("/view/tarefa.fxml", "Tarefas");
    }

    @FXML
    public void onEquipes(ActionEvent e) {
        MainApp.changeScene("/view/equipe.fxml", "Equipes");
    }

    @FXML
    public void onControlePonto(ActionEvent e) {
        MainApp.changeScene("/view/controle_ponto.fxml", "Controle de Ponto");
    }

    @FXML
    public void onAgenda(ActionEvent e) {
        MainApp.changeScene("/view/agenda.fxml", "Agenda");
    }

    @FXML
    public void onRelatorios(ActionEvent e) {
        MainApp.changeScene("/view/relatorios.fxml", "Relatórios");
    }

    @FXML
    public void onConfiguracoes(ActionEvent e) {
        MainApp.changeScene("/view/configuracoes.fxml", "Configurações");
    }
}
