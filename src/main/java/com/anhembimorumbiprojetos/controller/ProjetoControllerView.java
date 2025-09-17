package com.anhembimorumbiprojetos.controller;

import java.time.LocalDate;
import java.util.List;

import com.anhembimorumbiprojetos.MainApp;
import com.anhembimorumbiprojetos.db.DB;
import com.anhembimorumbiprojetos.model.dao.EquipeDao;
import com.anhembimorumbiprojetos.model.dao.ProjetoDao;
import com.anhembimorumbiprojetos.model.entities.Equipe;
import com.anhembimorumbiprojetos.model.entities.Projeto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProjetoControllerView {

    @FXML private TextField nomeField;
    @FXML private TextField descricaoField;
    @FXML private DatePicker dataInicioField;
    @FXML private DatePicker dataTerminoField;
    @FXML private TextField statusField;
    @FXML private ComboBox<Equipe> equipeComboBox;

    @FXML private TableView<Projeto> tabelaProjetos;
    @FXML private TableColumn<Projeto, Integer> colId;
    @FXML private TableColumn<Projeto, String> colNome;
    @FXML private TableColumn<Projeto, String> colStatus;
    @FXML private TableColumn<Projeto, String> colEquipe;

    private ProjetoDao projetoDao;
    private EquipeDao equipeDao;
    private ObservableList<Projeto> projetos;

    public ProjetoControllerView() {
        this.projetoDao = new ProjetoDao(DB.getConnection());
        this.equipeDao = new EquipeDao(DB.getConnection());
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
        colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        colEquipe.setCellValueFactory(cellData -> {
            Equipe equipe = equipeDao.buscarPorId(cellData.getValue().getEquipeId());
            String nomeEquipe = (equipe != null) ? equipe.getNome() : "Sem equipe";
            return new javafx.beans.property.SimpleStringProperty(nomeEquipe);
        });

        List<Equipe> equipes = equipeDao.listarTodos();
        equipeComboBox.setItems(FXCollections.observableArrayList(equipes));
        equipeComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Equipe item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });
        equipeComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Equipe item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });

        projetos = FXCollections.observableArrayList(projetoDao.listarTodos());
        tabelaProjetos.setItems(projetos);
    }

    @FXML
    public void onAdicionarProjeto() {
        String nome = nomeField.getText();
        String descricao = descricaoField.getText();
        LocalDate inicio = dataInicioField.getValue();
        LocalDate termino = dataTerminoField.getValue();
        String status = statusField.getText();
        Equipe equipe = equipeComboBox.getValue();

        if (nome.isEmpty() || status.isEmpty() || equipe == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha os campos obrigatórios: Nome, Status e Equipe.");
            alert.show();
            return;
        }

        Projeto projeto = new Projeto();
        projeto.setNome(nome);
        projeto.setDescricao(descricao);
        projeto.setDataInicio(inicio);
        projeto.setDataTermino(termino);
        projeto.setStatus(status);
        projeto.setEquipeId(equipe.getId());

        projetoDao.adicionarProjeto(projeto);
        projetos.setAll(projetoDao.listarTodos());

        nomeField.clear();
        descricaoField.clear();
        dataInicioField.setValue(null);
        dataTerminoField.setValue(null);
        statusField.clear();
        equipeComboBox.getSelectionModel().clearSelection();
    }
    @FXML
    public void onVoltarDashboard() {
        MainApp.changeScene("view/dashboard.fxml", "Dashboard");
    }

}


