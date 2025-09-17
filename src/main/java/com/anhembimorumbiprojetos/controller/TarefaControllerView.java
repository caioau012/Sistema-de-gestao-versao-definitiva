package com.anhembimorumbiprojetos.controller;

import java.time.LocalDate;

import com.anhembimorumbiprojetos.MainApp;
import com.anhembimorumbiprojetos.db.DB;
import com.anhembimorumbiprojetos.model.dao.MembroDao;
import com.anhembimorumbiprojetos.model.dao.ProjetoDao;
import com.anhembimorumbiprojetos.model.dao.TarefaDao;
import com.anhembimorumbiprojetos.model.entities.Membro;
import com.anhembimorumbiprojetos.model.entities.Projeto;
import com.anhembimorumbiprojetos.model.entities.Tarefa;

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

public class TarefaControllerView {

    @FXML private TextField tituloField;
    @FXML private TextField descricaoField;
    @FXML private DatePicker dataInicioField;
    @FXML private DatePicker dataTerminoField;
    @FXML private TextField statusField;
    @FXML private ComboBox<Projeto> projetoComboBox;
    @FXML private ComboBox<Membro> membroComboBox;

    @FXML private TableView<Tarefa> tabelaTarefas;
    @FXML private TableColumn<Tarefa, Integer> colId;
    @FXML private TableColumn<Tarefa, String> colTitulo;
    @FXML private TableColumn<Tarefa, String> colStatus;
    @FXML private TableColumn<Tarefa, String> colProjeto;
    @FXML private TableColumn<Tarefa, String> colResponsavel;

    private TarefaDao tarefaDao;
    private ProjetoDao projetoDao;
    private MembroDao membroDao;
    private ObservableList<Tarefa> tarefas;

    public TarefaControllerView() {
        this.tarefaDao = new TarefaDao(DB.getConnection());
        this.projetoDao = new ProjetoDao(DB.getConnection());
        this.membroDao = new MembroDao(DB.getConnection());
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colTitulo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitulo()));
        colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        colProjeto.setCellValueFactory(cellData -> {
            Projeto projeto = projetoDao.buscarPorId(cellData.getValue().getProjetoId());
            String nomeProjeto = (projeto != null) ? projeto.getNome() : "Sem projeto";
            return new javafx.beans.property.SimpleStringProperty(nomeProjeto);
        });
        colResponsavel.setCellValueFactory(cellData -> {
            Membro membro = membroDao.buscarPorId(cellData.getValue().getMembroId());
            String nomeMembro = (membro != null) ? membro.getNome() : "Sem responsável";
            return new javafx.beans.property.SimpleStringProperty(nomeMembro);
        });

        projetoComboBox.setItems(FXCollections.observableArrayList(projetoDao.listarTodos()));
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

        membroComboBox.setItems(FXCollections.observableArrayList(membroDao.listarTodos()));
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

        tarefas = FXCollections.observableArrayList(tarefaDao.listarTodos());
        tabelaTarefas.setItems(tarefas);
    }

    @FXML
    public void onAdicionarTarefa() {
        String titulo = tituloField.getText();
        String descricao = descricaoField.getText();
        LocalDate inicio = dataInicioField.getValue();
        LocalDate termino = dataTerminoField.getValue();
        String status = statusField.getText();
        Projeto projeto = projetoComboBox.getValue();
        Membro membro = membroComboBox.getValue();

        if (titulo.isEmpty() || status.isEmpty() || projeto == null || membro == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha os campos obrigatórios: Título, Status, Projeto e Responsável.");
            alert.show();
            return;
        }

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(titulo);
        tarefa.setDescricao(descricao);
        tarefa.setDataInicio(inicio);
        tarefa.setDataTermino(termino);
        tarefa.setStatus(status);
        tarefa.setProjetoId(projeto.getId());
        tarefa.setMembroId(membro.getId());

        tarefaDao.adicionarTarefa(tarefa);
        tarefas.setAll(tarefaDao.listarTodos());

        tituloField.clear();
        descricaoField.clear();
        dataInicioField.setValue(null);
        dataTerminoField.setValue(null);
        statusField.clear();
        projetoComboBox.getSelectionModel().clearSelection();
        membroComboBox.getSelectionModel().clearSelection();
    }
    @FXML
    public void onVoltarDashboard() {
        MainApp.changeScene("view/dashboard.fxml", "Dashboard");
    }

}

