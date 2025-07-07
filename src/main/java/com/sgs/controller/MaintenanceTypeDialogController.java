package com.sgs.controller;

import com.sgs.model.MaintenanceType;
import com.sgs.repository.MaintenanceTypeRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.application.Platform;

public class MaintenanceTypeDialogController {
    @FXML private TextField tfName;
    @FXML private TextArea taDescription;
    @FXML private DialogPane dialogPane;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private MaintenanceType type;
    private final MaintenanceTypeRepository repo = new MaintenanceTypeRepository();

    @FXML
    public void initialize() {
        // Configurar tooltips para los botones
        Platform.runLater(() -> {
            if (saveButton != null) {
                saveButton.setTooltip(new Tooltip("Guardar tipo de mantenimiento"));
            }

            if (cancelButton != null) {
                cancelButton.setTooltip(new Tooltip("Cancelar"));
            }
        });
    }

    public void setType(MaintenanceType type) {
        this.type = type;
        if (type != null) {
            tfName.setText(type.getName());
            taDescription.setText(type.getDescription());
        }
    }

    @FXML
    public void onSave() {
        String name = tfName.getText();
        String desc = taDescription.getText();
        if (name.isBlank()) {
            showAlert("El nombre es obligatorio.");
            return;
        }

        if (type == null) type = new MaintenanceType();
        type.setName(name);
        type.setDescription(desc);

        if (type.getIdType() == 0) {
            repo.save(type);
        } else {
            repo.update(type);
        }

        close();
    }

    @FXML
    public void onCancel() {
        close();
    }

    private void close() {
        ((Stage) dialogPane.getScene().getWindow()).close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}