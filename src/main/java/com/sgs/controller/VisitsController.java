package com.sgs.controller;

import com.sgs.model.Visit;
import com.sgs.repository.UserRepository;
import com.sgs.repository.StationRepository;
import com.sgs.repository.MaintenanceTypeRepository;
import com.sgs.repository.VisitRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.Map;

public class VisitsController {

    @FXML private TableView<Visit> tableVisits;
    @FXML private TableColumn<Visit, Integer> colId;
    @FXML private TableColumn<Visit, String> colStation;
    @FXML private TableColumn<Visit, String> colUser;
    @FXML private TableColumn<Visit, String> colType;
    @FXML private TableColumn<Visit, String> colDate;
    @FXML private TableColumn<Visit, String> colResult;
    @FXML private TableColumn<Visit, String> colObs;

    private final VisitRepository visitRepo = new VisitRepository();
    private final StationRepository stationRepo = new StationRepository();
    private final UserRepository userRepo = new UserRepository();
    private final MaintenanceTypeRepository typeRepo = new MaintenanceTypeRepository();

    private final ObservableList<Visit> visitList = FXCollections.observableArrayList();

    private final Map<Integer, String> stationMap = new HashMap<>();
    private final Map<Integer, String> userMap = new HashMap<>();
    private final Map<Integer, String> typeMap = new HashMap<>();

    @FXML
    public void initialize() {
        // Cargar referencias para mostrar nombres
        stationRepo.findAll().forEach(s -> stationMap.put(s.getIdStation(), s.getName()));
        userRepo.findAll().forEach(u -> userMap.put(u.getIdUser(), u.getName()));
        typeRepo.findAll().forEach(t -> typeMap.put(t.getIdType(), t.getName()));

        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdVisit()).asObject());
        colStation.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                stationMap.getOrDefault(data.getValue().getIdStation(), "Desconocida")));
        colUser.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                userMap.getOrDefault(data.getValue().getIdUser(), "Desconocido")));
        colType.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                typeMap.getOrDefault(data.getValue().getIdMaintenanceType(), "Desconocido")));
        colDate.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDate().toString()));
        colResult.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getResult()));
        colObs.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getObservations()));

        loadVisits();
    }

    private void loadVisits() {
        visitList.setAll(visitRepo.findAll());
        tableVisits.setItems(visitList);
    }

    @FXML
    public void onCreateVisit() {
        openDialog(null);
    }

    @FXML
    public void onEditVisit() {
        Visit selected = tableVisits.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openDialog(selected);
        } else {
            showAlert("Error", "Selecciona una visita para editar.");
        }
    }

    @FXML
    public void onDeleteVisit() {
        Visit selected = tableVisits.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean deleted = visitRepo.deleteById(selected.getIdVisit());
            if (deleted) {
                showAlert("Eliminada", "Visita eliminada.");
                loadVisits();
            } else {
                showAlert("Error", "No se pudo eliminar.");
            }
        } else {
            showAlert("Error", "Selecciona una visita para eliminar.");
        }
    }

    private void openDialog(Visit visitToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/visit-dialog.fxml"));
            DialogPane dialogPane = loader.load();

            VisitDialogController controller = loader.getController();
            controller.setVisit(visitToEdit); // null si es nueva

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle(visitToEdit == null ? "Nueva Visita" : "Editar Visita");
            dialog.showAndWait();

            loadVisits(); // refrescar tabla
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el diálogo.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
