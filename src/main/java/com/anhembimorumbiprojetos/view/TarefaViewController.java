package com.anhembimorumbiprojetos.view;

import com.anhembimorumbiprojetos.controller.TarefaController;
import com.anhembimorumbiprojetos.model.entities.Tarefa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TarefaViewController {

    @FXML private TextField campoPesquisa;
    @FXML private TableView<Tarefa> tabelaTarefas;
    @FXML private TableColumn<Tarefa, Integer> colunaId;
    @FXML private TableColumn<Tarefa, String> colunaTitulo;
    @FXML private TableColumn<Tarefa, String> colunaDescricao;
    @FXML private TableColumn<Tarefa, String> colunaStatus;

    private TarefaController tarefaController = new TarefaController();
    private ObservableList<Tarefa> listaTarefas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colunaId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colunaTitulo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitulo()));
        colunaDescricao.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescricao()));
        colunaStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        atualizarTabela();
    }

    public void voltar(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void pesquisarTarefas(ActionEvent event) {
        String termo = campoPesquisa.getText().toLowerCase();
        listaTarefas.clear();
        for (Tarefa tarefa : tarefaController.listarTarefas()) {
            if (tarefa.getTitulo().toLowerCase().contains(termo)) {
                listaTarefas.add(tarefa);
            }
        }
        tabelaTarefas.setItems(listaTarefas);
    }

    public void novaTarefa(ActionEvent event) {
        Tarefa nova = new Tarefa();
        nova.setTitulo("Nova Tarefa");
        nova.setDescricao("Descrição padrão");
        nova.setStatus("Pendente");
        tarefaController.adicionarTarefa(nova);
        atualizarTabela();
    }

    public void editarTarefa(ActionEvent event) {
        Tarefa selecionada = tabelaTarefas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            selecionada.setTitulo(selecionada.getTitulo() + " (Editada)");
            tarefaController.atualizarTarefa(selecionada);
            atualizarTabela();
        } else {
            mostrarAlerta("Selecione uma tarefa para editar.");
        }
    }

    public void excluirTarefa(ActionEvent event) {
        Tarefa selecionada = tabelaTarefas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            tarefaController.removerTarefa(selecionada.getId());
            atualizarTabela();
        } else {
            mostrarAlerta("Selecione uma tarefa para excluir.");
        }
    }

    private void atualizarTabela() {
        listaTarefas.setAll(tarefaController.listarTarefas());
        tabelaTarefas.setItems(listaTarefas);
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
