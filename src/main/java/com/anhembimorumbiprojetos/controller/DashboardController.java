package com.anhembimorumbiprojetos.controller;

import java.util.List;

import com.anhembimorumbiprojetos.MainApp;
import com.anhembimorumbiprojetos.db.DB;
import com.anhembimorumbiprojetos.model.dao.EquipeDao;
import com.anhembimorumbiprojetos.model.dao.MembroDao;
import com.anhembimorumbiprojetos.model.dao.ProjetoDao;
import com.anhembimorumbiprojetos.model.entities.Projeto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardController {

	@FXML private Label totalProjetosLabel;
	@FXML private Label projetosConcluidosLabel;
	@FXML private Label totalEquipesLabel;
	@FXML private Label totalMembrosLabel;
	@FXML private VBox projetosRecentesBox;

	private ProjetoDao projetoDao = new ProjetoDao(DB.getConnection());
	private EquipeDao equipeDao = new EquipeDao(DB.getConnection());
	private MembroDao membroDao = new MembroDao(DB.getConnection());
	
	@FXML
	public void initialize() {
	    atualizarIndicadores();
	    carregarProjetosRecentes();
	}
	
	private void atualizarIndicadores() {
	    totalProjetosLabel.setText(String.valueOf(projetoDao.listarTodos().size()));
	    projetosConcluidosLabel.setText(String.valueOf(projetoDao.listarPorStatus("Concluído").size()));
	    totalEquipesLabel.setText(String.valueOf(equipeDao.listarTodos().size()));
	    totalMembrosLabel.setText(String.valueOf(membroDao.listarTodos().size()));
	}

	private void carregarProjetosRecentes() {
	    projetosRecentesBox.getChildren().clear();
	    List<Projeto> recentes = projetoDao.listarPorStatus("Em andamento");
	    for (Projeto projeto : recentes) {
	        Label label = new Label(projeto.getNome() + " - " + projeto.getStatus());
	        label.setStyle("-fx-padding: 5px; -fx-font-size: 14px;");
	        projetosRecentesBox.getChildren().add(label);
	    }
	}
	
    @FXML
    public void onDashboard(ActionEvent e) {
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
    public void onVoltarDashboard() {
        MainApp.changeScene("/view/dashboard.fxml", "Dashboard");
    }
    
    @FXML
    public void onMembros(ActionEvent e) {
        MainApp.changeScene("/view/membro.fxml", "Membros");
    }

}
