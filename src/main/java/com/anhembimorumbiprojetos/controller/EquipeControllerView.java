package com.anhembimorumbiprojetos.controller;

import java.time.LocalDate;

import com.anhembimorumbiprojetos.MainApp;
import com.anhembimorumbiprojetos.db.DB;
import com.anhembimorumbiprojetos.model.dao.EquipeDao;
import com.anhembimorumbiprojetos.model.entities.Equipe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class EquipeControllerView {

    @FXML private TextField nomeField;
    @FXML private TextField descricaoField;
    @FXML private DatePicker dataCriacaoField;

    @FXML private TableView<Equipe> tabelaEquipes;
    @FXML private TableColumn<Equipe, Integer> colId;
    @FXML private TableColumn<Equipe, String> colNome;
    @FXML private TableColumn<Equipe, String> colDescricao;

    private EquipeDao equipeDao;
    private ObservableList<Equipe> equipes;

    public EquipeControllerView() {
        this.equipeDao = new EquipeDao(DB.getConnection());
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
        colDescricao.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescricao()));

        equipes = FXCollections.observableArrayList(equipeDao.listarTodos());
        tabelaEquipes.setItems(equipes);
    }

    @FXML
    public void onAdicionarEquipe() {
        String nome = nomeField.getText();
        String descricao = descricaoField.getText();
        LocalDate dataCriacao = dataCriacaoField.getValue();

        if (nome.isEmpty() || dataCriacao == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha os campos obrigatórios: Nome e Data de Criação.");
            alert.show();
            return;
        }

        Equipe equipe = new Equipe();
        equipe.setNome(nome);
        equipe.setDescricao(descricao);
        equipe.setDataCriacao(dataCriacao);

        equipeDao.adicionarEquipe(equipe);
        equipes.setAll(equipeDao.listarTodos());

        nomeField.clear();
        descricaoField.clear();
        dataCriacaoField.setValue(null);
    }
    
    @FXML
    public void onVoltarDashboard() {
        MainApp.changeScene("view/dashboard.fxml", "Dashboard");
    }

}

