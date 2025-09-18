package com.anhembimorumbiprojetos.controller;

import com.anhembimorumbiprojetos.MainApp;
import com.anhembimorumbiprojetos.db.DB;
import com.anhembimorumbiprojetos.model.dao.EquipeDao;
import com.anhembimorumbiprojetos.model.entities.Equipe;
import com.anhembimorumbiprojetos.model.entities.Membro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MembroControllerView {

    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private ComboBox<Equipe> equipeFiltroComboBox;

    @FXML private TableView<Membro> tabelaMembros;
    @FXML private TableColumn<Membro, Integer> colId;
    @FXML private TableColumn<Membro, String> colNome;
    @FXML private TableColumn<Membro, String> colEmail;

    private MembroController membroController;
    private EquipeDao equipeDao;
    private ObservableList<Membro> membros;
    private ObservableList<Equipe> equipes;
    private Membro membroSelecionado;

    public MembroControllerView() {
        this.membroController = new MembroController();
        this.equipeDao = new EquipeDao(DB.getConnection());
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));

        membros = FXCollections.observableArrayList(membroController.listarMembros());
        tabelaMembros.setItems(membros);

        equipes = FXCollections.observableArrayList(equipeDao.listarTodos());
        equipeFiltroComboBox.setItems(equipes);
        equipeFiltroComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Equipe item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });
        equipeFiltroComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Equipe item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });

        tabelaMembros.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            membroSelecionado = newVal;
            if (newVal != null) {
                nomeField.setText(newVal.getNome());
                emailField.setText(newVal.getEmail());
            } else {
                nomeField.clear();
                emailField.clear();
            }
        });
    }

    @FXML
    public void onAdicionarMembro() {
        Membro membro = new Membro();
        membro.setNome(nomeField.getText());
        membro.setEmail(emailField.getText());

        if (!membroController.validarMembro(membro)) return;

        membroController.adicionarMembro(membro);
        membros.setAll(membroController.listarMembros());
        nomeField.clear();
        emailField.clear();
        tabelaMembros.getSelectionModel().clearSelection();
    }

    @FXML
    public void onAtualizarMembro() {
        if (membroSelecionado == null) return;

        membroSelecionado.setNome(nomeField.getText());
        membroSelecionado.setEmail(emailField.getText());

        if (!membroController.validarMembro(membroSelecionado)) return;

        membroController.atualizarMembro(membroSelecionado);
        membros.setAll(membroController.listarMembros());
        nomeField.clear();
        emailField.clear();
        tabelaMembros.getSelectionModel().clearSelection();
    }

    @FXML
    public void onExcluirMembro() {
        if (membroSelecionado == null) return;

        membroController.removerMembro(membroSelecionado.getId());
        membros.setAll(membroController.listarMembros());
        nomeField.clear();
        emailField.clear();
        tabelaMembros.getSelectionModel().clearSelection();
    }

    @FXML
    public void onFiltrarPorEquipe() {
        Equipe equipe = equipeFiltroComboBox.getValue();
        if (equipe != null) {
            membros.setAll(membroController.listarMembrosPorEquipe(equipe.getId()));
        }
    }

    @FXML
    public void onVoltarDashboard() {
        MainApp.changeScene("/view/dashboard.fxml", "Dashboard");
    }
}
