package com.sgs.controller;

import com.sgs.model.Station;
import com.sgs.repository.StationRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class StationsController {

    @FXML private TableView<Station> tableStations;
    @FXML private TableColumn<Station, Integer> colId;
    @FXML private TableColumn<Station, String> colName;
    @FXML private TableColumn<Station, String> colAddress;
    @FXML private TableColumn<Station, String> colType;
    @FXML private TableColumn<Station, String> colEmail;
    @FXML private TableColumn<Station, String> colStatus;
    @FXML private TextField searchBar;

    private final ObservableList<Station> stationList = FXCollections.observableArrayList();
    private final StationRepository stationRepo = new StationRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdStation()).asObject());
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colAddress.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAddress()));
        colType.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getContactEmail()));
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        loadStations();
        setupSearchFilter();
    }

    private void setupSearchFilter() {
        FilteredList<Station> filtered = new FilteredList<>(stationList, s -> true);

        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal.toLowerCase();
            filtered.setPredicate(station -> {
                if (newVal == null || newVal.isBlank()) return true;
                return station.getName().toLowerCase().contains(lower) ||
                        station.getAddress().toLowerCase().contains(lower) ||
                        station.getType().toLowerCase().contains(lower) ||
                        station.getStatus().toLowerCase().contains(lower) ||
                        station.getContactEmail().toLowerCase().contains(lower);
            });
        });

        SortedList<Station> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableStations.comparatorProperty());
        tableStations.setItems(sorted);
    }

    private void loadStations() {
        stationList.setAll(stationRepo.findAll());
        tableStations.setItems(stationList);
    }

    @FXML
    public void onCreateStation() {
        openDialog(null);
    }

    @FXML
    public void onEditStation() {
        Station selected = tableStations.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openDialog(selected);
        } else {
            showAlert("Error", "Selecciona una estación para editar.");
        }
    }

    @FXML
    public void onDeleteStation() {
        Station selected = tableStations.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = stationRepo.deleteById(selected.getIdStation());
            if (success) {
                showAlert("Eliminada", "Estación eliminada.");
                loadStations();
            } else {
                showAlert("Error", "No se pudo eliminar la estación.");
            }
        } else {
            showAlert("Error", "Selecciona una estación para eliminar.");
        }
    }

    private void openDialog(Station stationToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/station-dialog.fxml"));
            DialogPane dialogPane = loader.load();

            StationDialogController controller = loader.getController();
            controller.setStation(stationToEdit); // null si es nueva

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle(stationToEdit == null ? "Nueva Estación" : "Editar Estación");

            controller.setupDialog();

            dialog.showAndWait();

            loadStations(); // refrescar tabla
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error","No se pudo abrir el formulario: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void onShowDetails() {
        Station selected = tableStations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aviso", "Selecciona una estación para ver los detalles.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/station-details-view.fxml"));
            Parent root = loader.load();

            StationDetailsController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Detalles de Estación");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            controller.setStage(stage);
            controller.loadData(selected);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo mostrar la vista de detalles.");
        }
    }
}
