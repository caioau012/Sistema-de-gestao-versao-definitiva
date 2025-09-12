package com.anhembimorumbiprojetos.view;

import com.anhembimorumbiprojetos.controller.EquipeController;
import com.anhembimorumbiprojetos.db.DB;
import com.anhembimorumbiprojetos.model.entities.Equipe;
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

public class EquipeViewController {

    @FXML private TextField campoPesquisa;
    @FXML private TableView<Equipe> tabelaEquipes;
    @FXML private TableColumn<Equipe, Integer> colunaId;
    @FXML private TableColumn<Equipe, String> colunaNome;
    @FXML private TableColumn<Equipe, String> colunaDescricao;
    @FXML private TableColumn<Equipe, String> colunaDataCriacao;
    @FXML private TableColumn<Equipe, Integer> colunaMembros;

    private EquipeController equipeController = new EquipeController();
    private ObservableList<Equipe> listaEquipes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colunaId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colunaNome.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNome()));
        colunaDescricao.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescricao()));
        colunaDataCriacao.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDataCriacao().toString()));
        colunaMembros.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getMembros().size()).asObject());

        atualizarTabela();
    }

    public void voltar(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void pesquisarEquipes(ActionEvent event) {
        String termo = campoPesquisa.getText().toLowerCase();
        listaEquipes.clear();
        for (Equipe equipe : equipeController.listarEquipes()) {
            if (equipe.getNome().toLowerCase().contains(termo)) {
                listaEquipes.add(equipe);
            }
        }
        tabelaEquipes.setItems(listaEquipes);
    }

    public void novaEquipe(ActionEvent event) {
        Equipe nova = new Equipe();
        nova.setNome("Nova Equipe");
        nova.setDescricao("Descrição padrão");
        nova.setDataCriacao(java.time.LocalDate.now());
        equipeController.adicionarEquipe(nova);
        atualizarTabela();
    }

    public void editarEquipe(ActionEvent event) {
        Equipe selecionada = tabelaEquipes.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            selecionada.setNome(selecionada.getNome() + " (Editada)");
            equipeController.atualizarEquipe(selecionada);
            atualizarTabela();
        } else {
            mostrarAlerta("Selecione uma equipe para editar.");
        }
    }

    public void excluirEquipe(ActionEvent event) {
        Equipe selecionada = tabelaEquipes.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            equipeController.removerEquipe(selecionada.getId());
            atualizarTabela();
        } else {
            mostrarAlerta("Selecione uma equipe para excluir.");
        }
    }

    public void verMembrosEquipe(ActionEvent event) {
        Equipe selecionada = tabelaEquipes.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            for (Membro membro : equipeController.listarMembrosPorEquipe(selecionada.getId())) {
                System.out.println("Membro: " + membro.getNome());
            }
        } else {
            mostrarAlerta("Selecione uma equipe para ver os membros.");
        }
    }

    public void gerenciarMembrosEquipe(ActionEvent event) {
        mostrarAlerta("Funcionalidade de gerenciamento de membros ainda não implementada.");
    }

    private void atualizarTabela() {
        listaEquipes.setAll(equipeController.listarEquipes());
        tabelaEquipes.setItems(listaEquipes);
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
