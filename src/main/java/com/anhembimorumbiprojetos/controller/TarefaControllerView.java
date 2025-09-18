package com.anhembimorumbiprojetos.controller;

import com.anhembimorumbiprojetos.MainApp;
import com.anhembimorumbiprojetos.db.DB;
import com.anhembimorumbiprojetos.model.dao.MembroDao;
import com.anhembimorumbiprojetos.model.dao.ProjetoDao;
import com.anhembimorumbiprojetos.model.entities.Membro;
import com.anhembimorumbiprojetos.model.entities.Projeto;
import com.anhembimorumbiprojetos.model.entities.Tarefa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TarefaControllerView {

    @FXML private TextField tituloField;
    @FXML private TextArea descricaoField;
    @FXML private DatePicker dataInicioField;
    @FXML private DatePicker dataTerminoField;
    @FXML private TextField statusField;
    @FXML private ComboBox<Projeto> projetoComboBox;
    @FXML private ComboBox<Membro> membroComboBox;
    @FXML private ComboBox<Projeto> filtroProjetoComboBox;
    @FXML private ComboBox<Membro> filtroMembroComboBox;

    @FXML private TableView<Tarefa> tabelaTarefas;
    @FXML private TableColumn<Tarefa, Integer> colId;
    @FXML private TableColumn<Tarefa, String> colTitulo;
    @FXML private TableColumn<Tarefa, String> colStatus;

    private TarefaController tarefaController;
    private ProjetoDao projetoDao;
    private MembroDao membroDao;
    private ObservableList<Tarefa> tarefas;
    private ObservableList<Projeto> projetos;
    private ObservableList<Membro> membros;
    private Tarefa tarefaSelecionada;

    public TarefaControllerView() {
        this.tarefaController = new TarefaController();
        this.projetoDao = new ProjetoDao(DB.getConnection());
        this.membroDao = new MembroDao(DB.getConnection());
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colTitulo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitulo()));
        colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));

        projetos = FXCollections.observableArrayList(projetoDao.listarTodos());
        membros = FXCollections.observableArrayList(membroDao.listarTodos());

        projetoComboBox.setItems(projetos);
        filtroProjetoComboBox.setItems(projetos);
        membroComboBox.setItems(membros);
        filtroMembroComboBox.setItems(membros);

        projetoComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Projeto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });
        projetoComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Projeto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });

        filtroProjetoComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Projeto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });
        filtroProjetoComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Projeto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });

        membroComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Membro item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });
        membroComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Membro item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });

        filtroMembroComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Membro item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });
        filtroMembroComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Membro item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });

        tarefas = FXCollections.observableArrayList(tarefaController.listarTarefas());
        tabelaTarefas.setItems(tarefas);

        tabelaTarefas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            tarefaSelecionada = newVal;
            if (newVal != null) {
                tituloField.setText(newVal.getTitulo());
                descricaoField.setText(newVal.getDescricao());
                dataInicioField.setValue(newVal.getDataInicio());
                dataTerminoField.setValue(newVal.getDataTermino());
                statusField.setText(newVal.getStatus());
                projetoComboBox.getSelectionModel().select(projetoDao.buscarPorId(newVal.getProjetoId()));
                membroComboBox.getSelectionModel().select(membroDao.buscarPorId(newVal.getMembroId()));
            } else {
                limparCampos();
            }
        });
    }
    @FXML
    public void onAdicionarTarefa() {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(tituloField.getText());
        tarefa.setDescricao(descricaoField.getText());
        tarefa.setDataInicio(dataInicioField.getValue());
        tarefa.setDataTermino(dataTerminoField.getValue());
        tarefa.setStatus(statusField.getText());
        tarefa.setProjetoId(projetoComboBox.getValue() != null ? projetoComboBox.getValue().getId() : 0);
        tarefa.setMembroId(membroComboBox.getValue() != null ? membroComboBox.getValue().getId() : 0);

        tarefaController.adicionarTarefa(tarefa);
        tarefas.setAll(tarefaController.listarTarefas());
        limparCampos();
    }

    @FXML
    public void onAtualizarTarefa() {
        if (tarefaSelecionada == null) return;

        tarefaSelecionada.setTitulo(tituloField.getText());
        tarefaSelecionada.setDescricao(descricaoField.getText());
        tarefaSelecionada.setDataInicio(dataInicioField.getValue());
        tarefaSelecionada.setDataTermino(dataTerminoField.getValue());
        tarefaSelecionada.setStatus(statusField.getText());
        tarefaSelecionada.setProjetoId(projetoComboBox.getValue() != null ? projetoComboBox.getValue().getId() : 0);
        tarefaSelecionada.setMembroId(membroComboBox.getValue() != null ? membroComboBox.getValue().getId() : 0);

        tarefaController.atualizarTarefa(tarefaSelecionada);
        tarefas.setAll(tarefaController.listarTarefas());
        limparCampos();
    }

    @FXML
    public void onExcluirTarefa() {
        if (tarefaSelecionada == null) return;

        tarefaController.removerTarefa(tarefaSelecionada.getId());
        tarefas.setAll(tarefaController.listarTarefas());
        limparCampos();
    }

    @FXML
    public void onFiltrarPorProjeto() {
        Projeto projeto = filtroProjetoComboBox.getValue();
        if (projeto != null) {
            tarefas.setAll(tarefaController.listarTarefasPorProjeto(projeto.getId()));
        }
    }

    @FXML
    public void onFiltrarPorMembro() {
        Membro membro = filtroMembroComboBox.getValue();
        if (membro != null) {
            tarefas.setAll(tarefaController.listarTarefasPorMembro(membro.getId()));
        }
    }

    @FXML
    public void onVoltarDashboard() {
        MainApp.changeScene("/view/dashboard.fxml", "Dashboard");
    }

    private void limparCampos() {
        tituloField.clear();
        descricaoField.clear();
        dataInicioField.setValue(null);
        dataTerminoField.setValue(null);
        statusField.clear();
        projetoComboBox.getSelectionModel().clearSelection();
        membroComboBox.getSelectionModel().clearSelection();
        tabelaTarefas.getSelectionModel().clearSelection();
        tarefaSelecionada = null;
    }
}

