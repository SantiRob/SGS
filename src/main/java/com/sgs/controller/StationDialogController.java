package com.sgs.controller;
import com.sgs.model.Station;
import com.sgs.repository.StationRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
public class StationDialogController {
    @FXML private TextField tfName, tfAddress, tfType, tfEmail;
    @FXML private ChoiceBox<String> cbStatus;
    private Station station;
    private final StationRepository stationRepo = new StationRepository();

    @FXML
    public void initialize() {
        cbStatus.getItems().addAll("Activa", "Inactiva", "En mantenimiento");
        cbStatus.setValue("Activa"); // Valor por defecto

    }

    public void setupDialog() {
        DialogPane dialogPane = tfName.getScene().getRoot() instanceof DialogPane ?
                (DialogPane) tfName.getScene().getRoot() : null;

        if (dialogPane != null) {
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.setText("Guardar");
            okButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white;");

            dialogPane.lookupButton(ButtonType.OK).addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                event.consume(); // Evita cierre automático
                onSave();
            });
        }
    }

    public void setStation(Station station) {
        this.station = station;
        if (station != null) {
            tfName.setText(station.getName());
            tfAddress.setText(station.getAddress());
            tfType.setText(station.getType());
            tfEmail.setText(station.getContactEmail());
            cbStatus.setValue(station.getStatus());
        }
    }

    @FXML
    public void onSave() {
        if (tfName.getText().isBlank() || tfAddress.getText().isBlank() ||
                tfType.getText().isBlank() || cbStatus.getValue() == null) {
            showAlert("Por favor completa los campos obligatorios.");
            return;
        }

        if (station == null) station = new Station();
        station.setName(tfName.getText());
        station.setAddress(tfAddress.getText());
        station.setType(tfType.getText());
        station.setContactEmail(tfEmail.getText());
        station.setStatus(cbStatus.getValue());

        if (station.getIdStation() == 0) {
            stationRepo.save(station);
        } else {
            stationRepo.update(station);
        }
        close();
    }

    @FXML
    public void onCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) tfName.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}