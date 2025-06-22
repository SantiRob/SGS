package com.sgs.controller;

import com.sgs.model.MaintenanceType;
import com.sgs.repository.MaintenanceTypeRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

public class MaintenanceTypesController {

    @FXML private TableView<MaintenanceType> tableTypes;
    @FXML private TableColumn<MaintenanceType, Integer> colId;
    @FXML private TableColumn<MaintenanceType, String> colName;
    @FXML private TableColumn<MaintenanceType, String> colDescription;

    private final ObservableList<MaintenanceType> typeList = FXCollections.observableArrayList();
    private final MaintenanceTypeRepository typeRepo = new MaintenanceTypeRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdType()).asObject());
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colDescription.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));

        loadTypes();
    }

    private void loadTypes() {
        typeList.setAll(typeRepo.findAll());
        tableTypes.setItems(typeList);
    }

    @FXML
    public void onCreateType() {
        openDialog(null);
    }

    @FXML
    public void onEditType() {
        MaintenanceType selected = tableTypes.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openDialog(selected);
        } else {
            showAlert("Error", "Selecciona un tipo para editar.");
        }
    }

    private void openDialog(MaintenanceType typeToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/maintenance-type-dialog.fxml"));
            DialogPane dialogPane = loader.load();

            MaintenanceTypeDialogController controller = loader.getController();
            controller.setType(typeToEdit);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle(typeToEdit == null ? "Nuevo Tipo" : "Editar Tipo");
            dialog.showAndWait();

            loadTypes(); // Recargar lista
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el diálogo.");
        }
    }

    @FXML
    public void onDeleteType() {
        MaintenanceType selected = tableTypes.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = typeRepo.deleteById(selected.getIdType());
            if (success) {
                showAlert("Eliminado", "Tipo eliminado correctamente.");
                loadTypes();
            } else {
                showAlert("Error", "No se pudo eliminar.");
            }
        } else {
            showAlert("Error", "Selecciona un tipo para eliminar.");
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
