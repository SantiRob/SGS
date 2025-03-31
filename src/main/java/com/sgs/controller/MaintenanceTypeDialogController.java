package com.sgs.controller;
import com.sgs.model.MaintenanceType;
import com.sgs.repository.MaintenanceTypeRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MaintenanceTypeDialogController {
    @FXML private TextField tfName;
    @FXML private TextArea taDescription;
    @FXML private DialogPane dialogPane;

    private MaintenanceType type;
    private final MaintenanceTypeRepository repo = new MaintenanceTypeRepository();

    @FXML
    public void initialize() {
        javafx.application.Platform.runLater(() -> {
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.setText("Guardar");
            okButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white;");

            dialogPane.lookupButton(ButtonType.OK).addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                event.consume(); // Evita cierre automático
                onSave();
            });
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