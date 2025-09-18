package com.anhembimorumbiprojetos.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.anhembimorumbiprojetos.MainApp;
import com.anhembimorumbiprojetos.db.DB;
import com.anhembimorumbiprojetos.model.dao.EquipeDao;
import com.anhembimorumbiprojetos.model.entities.Equipe;
import com.anhembimorumbiprojetos.model.entities.Projeto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
    @FXML private TextField filtroField;
    @FXML private ComboBox<String> statusFiltroComboBox;

    @FXML private TableView<Projeto> tabelaProjetos;
    @FXML private TableColumn<Projeto, Integer> colId;
    @FXML private TableColumn<Projeto, String> colNome;
    @FXML private TableColumn<Projeto, String> colStatus;
    @FXML private TableColumn<Projeto, String> colEquipe;

    private ProjetoController projetoController;
    private EquipeDao equipeDao;
    private ObservableList<Projeto> projetos;
    private Projeto projetoSelecionado;

    public ProjetoControllerView() {
        this.projetoController = new ProjetoController();
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

        equipeComboBox.setItems(FXCollections.observableArrayList(equipeDao.listarTodos()));
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

        statusFiltroComboBox.setItems(FXCollections.observableArrayList(
            "Pendente", "Em andamento", "Concluído", "Revisão", "Cancelado"
        ));

        projetos = FXCollections.observableArrayList(projetoController.listarProjetos());
        tabelaProjetos.setItems(projetos);

        tabelaProjetos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            projetoSelecionado = newVal;
            if (newVal != null) {
                nomeField.setText(newVal.getNome());
                descricaoField.setText(newVal.getDescricao());
                dataInicioField.setValue(newVal.getDataInicio());
                dataTerminoField.setValue(newVal.getDataTermino());
                statusField.setText(newVal.getStatus());
                equipeComboBox.getSelectionModel().select(equipeDao.buscarPorId(newVal.getEquipeId()));
            }
        });
    }

    @FXML
    public void onAdicionarProjeto() {
        Projeto projeto = new Projeto();
        projeto.setNome(nomeField.getText());
        projeto.setDescricao(descricaoField.getText());
        projeto.setDataInicio(dataInicioField.getValue());
        projeto.setDataTermino(dataTerminoField.getValue());
        projeto.setStatus(statusField.getText());
        projeto.setEquipeId(equipeComboBox.getValue() != null ? equipeComboBox.getValue().getId() : 0);

        projetoController.adicionarProjeto(projeto);
        projetos.setAll(projetoController.listarProjetos());
        limparCampos();
    }

    @FXML
    public void onAtualizarProjeto() {
        if (projetoSelecionado == null) return;

        projetoSelecionado.setNome(nomeField.getText());
        projetoSelecionado.setDescricao(descricaoField.getText());
        projetoSelecionado.setDataInicio(dataInicioField.getValue());
        projetoSelecionado.setDataTermino(dataTerminoField.getValue());
        projetoSelecionado.setStatus(statusField.getText());
        projetoSelecionado.setEquipeId(equipeComboBox.getValue() != null ? equipeComboBox.getValue().getId() : 0);

        projetoController.atualizarProjeto(projetoSelecionado);
        projetos.setAll(projetoController.listarProjetos());
        limparCampos();
    }

    @FXML
    public void onExcluirProjeto() {
        if (projetoSelecionado == null) return;

        projetoController.removerProjeto(projetoSelecionado.getId());
        projetos.setAll(projetoController.listarProjetos());
        limparCampos();
    }

    @FXML
    public void onFiltrarProjetos() {
        String termo = filtroField.getText().toLowerCase();
        List<Projeto> filtrados = projetoController.listarProjetos().stream()
            .filter(p -> p.getNome().toLowerCase().contains(termo))
            .collect(Collectors.toList());
        projetos.setAll(filtrados);
    }
    
    @FXML
    public void onFiltrarPorStatus() {
        String statusSelecionado = statusFiltroComboBox.getValue();
        if (statusSelecionado != null && !statusSelecionado.isEmpty()) {
            List<Projeto> filtrados = projetoController.listarProjetosPorStatus(statusSelecionado);
            projetos.setAll(filtrados);
        }
    }
    @FXML
    public void onVoltarDashboard() {
        MainApp.changeScene("/view/dashboard.fxml", "Dashboard");
    }

    private void limparCampos() {
        nomeField.clear();
        descricaoField.clear();
        dataInicioField.setValue(null);
        dataTerminoField.setValue(null);
        statusField.clear();
        equipeComboBox.getSelectionModel().clearSelection();
        tabelaProjetos.getSelectionModel().clearSelection();
        projetoSelecionado = null;
    }
}


