package com.anhembimorumbiprojetos.view;

import com.anhembimorumbiprojetos.controller.ProjetoController;
import com.anhembimorumbiprojetos.model.entities.Projeto;

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

public class ProjetoViewController {

    @FXML private TextField campoPesquisa;
    @FXML private TableView<Projeto> tabelaProjetos;
    @FXML private TableColumn<Projeto, Integer> colunaId;
    @FXML private TableColumn<Projeto, String> colunaNome;
    @FXML private TableColumn<Projeto, String> colunaDescricao;
    @FXML private TableColumn<Projeto, String> colunaDataInicio;

    private ProjetoController projetoController = new ProjetoController();
    private ObservableList<Projeto> listaProjetos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colunaId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colunaNome.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNome()));
        colunaDescricao.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescricao()));
        colunaDataInicio.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDataInicio().toString()));

        atualizarTabela();
    }

    public void voltar(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void pesquisarProjetos(ActionEvent event) {
        String termo = campoPesquisa.getText().toLowerCase();
        listaProjetos.clear();
        for (Projeto projeto : projetoController.listarProjetos()) {
            if (projeto.getNome().toLowerCase().contains(termo)) {
                listaProjetos.add(projeto);
            }
        }
        tabelaProjetos.setItems(listaProjetos);
    }

    public void novoProjeto(ActionEvent event) {
        Projeto novo = new Projeto();
        novo.setNome("Novo Projeto");
        novo.setDescricao("Descrição padrão");
        novo.setDataInicio(java.time.LocalDate.now());
        projetoController.adicionarProjeto(novo);
        atualizarTabela();
    }

    public void editarProjeto(ActionEvent event) {
        Projeto selecionado = tabelaProjetos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            selecionado.setNome(selecionado.getNome() + " (Editado)");
            projetoController.atualizarProjeto(selecionado);
            atualizarTabela();
        } else {
            mostrarAlerta("Selecione um projeto para editar.");
        }
    }

    public void excluirProjeto(ActionEvent event) {
        Projeto selecionado = tabelaProjetos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            projetoController.removerProjeto(selecionado.getId());
            atualizarTabela();
        } else {
            mostrarAlerta("Selecione um projeto para excluir.");
        }
    }

    private void atualizarTabela() {
        listaProjetos.setAll(projetoController.listarProjetos());
        tabelaProjetos.setItems(listaProjetos);
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}