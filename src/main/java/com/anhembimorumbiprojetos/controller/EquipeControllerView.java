package com.anhembimorumbiprojetos.controller;

import java.time.LocalDate;

import com.anhembimorumbiprojetos.MainApp;
import com.anhembimorumbiprojetos.model.entities.Equipe;
import com.anhembimorumbiprojetos.model.entities.Membro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class EquipeControllerView {

    @FXML private TextField nomeEquipeField;
    @FXML private TextField nomeMembroField;
    @FXML private TextField emailMembroField;

    @FXML private TableView<Equipe> tabelaEquipes;
    @FXML private TableColumn<Equipe, Integer> colEquipeId;
    @FXML private TableColumn<Equipe, String> colEquipeNome;

    @FXML private TableView<Membro> tabelaMembros;
    @FXML private TableColumn<Membro, Integer> colMembroId;
    @FXML private TableColumn<Membro, String> colMembroNome;
    @FXML private TableColumn<Membro, String> colMembroEmail;

    private EquipeController equipeController;
    private ObservableList<Equipe> equipes;
    private ObservableList<Membro> membros;
    private Equipe equipeSelecionada;
    private Membro membroSelecionado;

    public EquipeControllerView() {
        this.equipeController = new EquipeController();
    }

    @FXML
    public void initialize() {
        colEquipeId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colEquipeNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));

        equipes = FXCollections.observableArrayList(equipeController.listarEquipes());
        tabelaEquipes.setItems(equipes);

        tabelaEquipes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            equipeSelecionada = newVal;
            if (newVal != null) {
                nomeEquipeField.setText(newVal.getNome());
                membros.setAll(equipeController.listarMembrosPorEquipe(newVal.getId()));
            } else {
                nomeEquipeField.clear();
                membros.clear();
            }
        });

        colMembroId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colMembroNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
        colMembroEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));

        membros = FXCollections.observableArrayList();
        tabelaMembros.setItems(membros);

        tabelaMembros.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            membroSelecionado = newVal;
            if (newVal != null) {
                nomeMembroField.setText(newVal.getNome());
                emailMembroField.setText(newVal.getEmail());
            } else {
                nomeMembroField.clear();
                emailMembroField.clear();
            }
        });
    }

    @FXML
    public void onAdicionarEquipe() {
        Equipe equipe = new Equipe();
        equipe.setNome(nomeEquipeField.getText());
        equipe.setDataCriacao(LocalDate.now());

        equipeController.adicionarEquipe(equipe);
        equipes.setAll(equipeController.listarEquipes());
        nomeEquipeField.clear();
        tabelaEquipes.getSelectionModel().clearSelection();

    }

    @FXML
    public void onAtualizarEquipe() {
        if (equipeSelecionada == null) return;

        equipeSelecionada.setNome(nomeEquipeField.getText());
        equipeController.atualizarEquipe(equipeSelecionada);
        equipes.setAll(equipeController.listarEquipes());
        nomeEquipeField.clear();
        tabelaEquipes.getSelectionModel().clearSelection();
    }

    @FXML
    public void onExcluirEquipe() {
        if (equipeSelecionada == null) return;

        equipeController.removerEquipe(equipeSelecionada.getId());
        equipes.setAll(equipeController.listarEquipes());
        membros.clear();
        nomeEquipeField.clear();
        tabelaEquipes.getSelectionModel().clearSelection();
    }

    @FXML
    public void onAdicionarMembro() {
        if (equipeSelecionada == null) return;

        Membro membro = new Membro();
        membro.setNome(nomeMembroField.getText());
        membro.setEmail(emailMembroField.getText());

        try {
            equipeController.adicionarMembro(equipeSelecionada.getId(), membro);
            membros.setAll(equipeController.listarMembrosPorEquipe(equipeSelecionada.getId()));
            nomeMembroField.clear();
            emailMembroField.clear();
            tabelaMembros.getSelectionModel().clearSelection();
        }
        catch (RuntimeException ex) {
            String msg = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
            mostrarAlerta("Não foi possível adicionar membro", msg);
        }
    }

    @FXML
    public void onRemoverMembro() {
        if (equipeSelecionada == null || membroSelecionado == null) return;

        try {
            equipeController.removerMembro(equipeSelecionada.getId(), membroSelecionado.getId());
            membros.setAll(equipeController.listarMembrosPorEquipe(equipeSelecionada.getId()));
            nomeMembroField.clear();
            emailMembroField.clear();
            tabelaMembros.getSelectionModel().clearSelection();
        }
        catch (RuntimeException ex) {
            String msg = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
            mostrarAlerta("Não foi possível remover membro", msg);
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    @FXML
    public void onVoltarDashboard() {
        MainApp.changeScene("/view/dashboard.fxml", "Dashboard");
    }
}

