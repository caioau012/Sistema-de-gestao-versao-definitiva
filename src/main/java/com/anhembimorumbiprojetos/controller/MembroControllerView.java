package com.anhembimorumbiprojetos.controller;

import java.util.List;

import com.anhembimorumbiprojetos.MainApp;
import com.anhembimorumbiprojetos.db.DB;
import com.anhembimorumbiprojetos.model.dao.EquipeDao;
import com.anhembimorumbiprojetos.model.dao.MembroDao;
import com.anhembimorumbiprojetos.model.entities.Equipe;
import com.anhembimorumbiprojetos.model.entities.Membro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MembroControllerView {

    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private ComboBox<Equipe> equipeComboBox;

    @FXML private TableView<Membro> tabelaMembros;
    @FXML private TableColumn<Membro, Integer> colId;
    @FXML private TableColumn<Membro, String> colNome;
    @FXML private TableColumn<Membro, String> colEmail;
    @FXML private TableColumn<Membro, String> colEquipe;

    private MembroDao membroDao;
    private EquipeDao equipeDao;
    private ObservableList<Membro> membros;

    public MembroControllerView() {
        this.membroDao = new MembroDao(DB.getConnection());
        this.equipeDao = new EquipeDao(DB.getConnection());
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
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

        membros = FXCollections.observableArrayList(membroDao.listarTodos());
        tabelaMembros.setItems(membros);
    }

    @FXML
    public void onAdicionarMembro() {
        String nome = nomeField.getText();
        String email = emailField.getText();
        Equipe equipe = equipeComboBox.getValue();

        if (nome.isEmpty() || email.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha os campos obrigatórios: Nome e Email.");
            alert.show();
            return;
        }

        Membro membro = new Membro();
        membro.setNome(nome);
        membro.setEmail(email);
        membro.setEquipeId(equipe != null ? equipe.getId() : 0);

        membroDao.adicionarMembro(membro);
        membros.setAll(membroDao.listarTodos());

        nomeField.clear();
        emailField.clear();
        equipeComboBox.getSelectionModel().clearSelection();
    }
    @FXML
    public void onVoltarDashboard() {
        MainApp.changeScene("view/dashboard.fxml", "Dashboard");
    }

}
