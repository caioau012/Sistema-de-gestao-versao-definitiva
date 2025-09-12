package com.anhembimorumbiprojetos.view;

import com.anhembimorumbiprojetos.controller.MembroController;
import com.anhembimorumbiprojetos.model.entities.Membro;

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

public class MembroViewController {

    @FXML private TextField campoPesquisa;
    @FXML private TableView<Membro> tabelaMembros;
    @FXML private TableColumn<Membro, Integer> colunaId;
    @FXML private TableColumn<Membro, String> colunaNome;
    @FXML private TableColumn<Membro, String> colunaEmail;

    private MembroController membroController = new MembroController();
    private ObservableList<Membro> listaMembros = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colunaId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colunaNome.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNome()));
        colunaEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));

        atualizarTabela();
    }

    public void voltar(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void pesquisarMembros(ActionEvent event) {
        String termo = campoPesquisa.getText().toLowerCase();
        listaMembros.clear();
        for (Membro membro : membroController.listarMembros()) {
            if (membro.getNome().toLowerCase().contains(termo)) {
                listaMembros.add(membro);
            }
        }
        tabelaMembros.setItems(listaMembros);
    }

    public void novoMembro(ActionEvent event) {
        Membro novo = new Membro();
        novo.setNome("Novo Membro");
        novo.setEmail("email@exemplo.com");
        membroController.adicionarMembro(novo);
        atualizarTabela();
    }

    public void editarMembro(ActionEvent event) {
        Membro selecionado = tabelaMembros.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            selecionado.setNome(selecionado.getNome() + " (Editado)");
            membroController.atualizarMembro(selecionado);
            atualizarTabela();
        } else {
            mostrarAlerta("Selecione um membro para editar.");
        }
    }

    public void excluirMembro(ActionEvent event) {
        Membro selecionado = tabelaMembros.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            membroController.removerMembro(selecionado.getId());
            atualizarTabela();
        } else {
            mostrarAlerta("Selecione um membro para excluir.");
        }
    }

    private void atualizarTabela() {
        listaMembros.setAll(membroController.listarMembros());
        tabelaMembros.setItems(listaMembros);
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
