package com.sgs.controller;

import com.sgs.model.Visit;
import com.sgs.repository.UserRepository;
import com.sgs.repository.StationRepository;
import com.sgs.repository.MaintenanceTypeRepository;
import com.sgs.repository.VisitRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class VisitsController {

    @FXML
    private TableView<Visit> tableVisits;
    @FXML
    private TableColumn<Visit, Integer> colId;
    @FXML
    private TableColumn<Visit, String> colStation;
    @FXML
    private TableColumn<Visit, String> colUser;
    @FXML
    private TableColumn<Visit, String> colType;
    @FXML
    private TableColumn<Visit, String> colDate;
    @FXML
    private TableColumn<Visit, String> colResult;
    @FXML
    private TableColumn<Visit, String> colObs;
    @FXML
    private TextField searchBar;

    private final VisitRepository visitRepo = new VisitRepository();
    private final StationRepository stationRepo = new StationRepository();
    private final UserRepository userRepo = new UserRepository();
    private final MaintenanceTypeRepository typeRepo = new MaintenanceTypeRepository();

    private final ObservableList<Visit> visitList = FXCollections.observableArrayList();
    private FilteredList<Visit> filteredVisits; // Lista filtrada

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
        setupSearchFilter();
    }

    private void loadVisits() {
        visitList.setAll(visitRepo.findAll());
        filteredVisits = new FilteredList<>(visitList, v -> true); // Inicializar con todo
        SortedList<Visit> sortedVisits = new SortedList<>(filteredVisits);
        sortedVisits.comparatorProperty().bind(tableVisits.comparatorProperty());
        tableVisits.setItems(sortedVisits);
    }

    private void setupSearchFilter() {
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredVisits.setPredicate(visit -> {
                if (newValue == null || newValue.isBlank()) {
                    return true; // Mostrar todo si está vacío
                }
                String searchText = newValue.toLowerCase();

                // Verificar coincidencias exactas primero
                if (visit.getResult().toLowerCase().equals(searchText)) {
                    return true;
                }

                // Buscar en los campos relevantes
                return visit.getResult().toLowerCase().contains(searchText) ||
                        stationMap.getOrDefault(visit.getIdStation(), "").toLowerCase().contains(searchText) ||
                        userMap.getOrDefault(visit.getIdUser(), "").toLowerCase().contains(searchText) ||
                        typeMap.getOrDefault(visit.getIdMaintenanceType(), "").toLowerCase().contains(searchText);
            });
        });
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

    @FXML
    public void onShowDetails() {
        Visit selected = tableVisits.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aviso", "Selecciona una visita para ver los detalles.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/visit-details-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Detalles de la Visita");
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            VisitDetailsController controller = loader.getController();
            controller.setStage(stage);
            controller.loadData(
                    selected,
                    stationMap.getOrDefault(selected.getIdStation(), "Desconocida"),
                    userMap.getOrDefault(selected.getIdUser(), "Desconocido"),
                    typeMap.getOrDefault(selected.getIdMaintenanceType(), "Desconocido")
            );

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo mostrar la vista de detalles.");
        }
    }
}
