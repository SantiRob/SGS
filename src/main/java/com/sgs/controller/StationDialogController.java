package com.sgs.controller;

import com.sgs.model.Station;
import com.sgs.repository.StationRepository;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class StationDialogController {

    @FXML private TextField tfMalla, tfStation, tfAddress, tfMunicipio, tfEmail;
    @FXML private ChoiceBox<String> cbType, cbStatus;

    private Station station;
    private final StationRepository stationRepo = new StationRepository();

    @FXML
    public void initialize() {
        cbType.getItems().addAll("Confinada", "Semiconfinada", "A nivel de piso");
        cbStatus.getItems().addAll("Activa", "Inactiva", "En mantenimiento");
        cbStatus.setValue("Activa");

        tfMalla.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                tfMalla.setText(newVal.replaceAll("[^\\d]", ""));
            }

            if (tfMalla.getText().length() > 12) {
                tfMalla.setText(tfMalla.getText().substring(0, 12));
            }
        });

        Platform.runLater(this::setupDialog);
    }

    public void setupDialog() {
        DialogPane dialogPane = tfStation.getScene().getRoot() instanceof DialogPane
                ? (DialogPane) tfStation.getScene().getRoot()
                : null;

        if (dialogPane != null) {
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.setText("Guardar");
            okButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white;");
            okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                event.consume();
                onSave();
            });
        }
    }

    public void setStation(Station station) {
        this.station = station;
        if (station != null) {
            tfMalla.setText(String.valueOf(station.getMalla()));
            tfStation.setText(station.getStation());
            tfAddress.setText(station.getAddress());
            tfMunicipio.setText(station.getMunicipio());
            tfEmail.setText(station.getContactEmail());
            cbType.setValue(station.getType());
            cbStatus.setValue(station.getStatus());

            tfMalla.setDisable(true);
            tfStation.setDisable(true);
            tfAddress.setDisable(true);
            tfMunicipio.setDisable(true);
            cbType.setDisable(true);
        }
    }

    @FXML
    public void onSave() {
        if (tfStation.getText().isBlank() || tfMalla.getText().isBlank() ||
                tfAddress.getText().isBlank() || tfMunicipio.getText().isBlank() ||
                cbType.getValue() == null || cbStatus.getValue() == null) {
            showAlert("Por favor completa todos los campos.");
            return;
        }

        if (!tfMalla.getText().matches("\\d+")) {
            showAlert("Malla debe contener solo números.");
            return;
        }

        if (!tfStation.getText().matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")) {
            showAlert("Nombre de estación solo puede contener letras.");
            return;
        }

        if (!tfAddress.getText().matches("[\\w\\s#\\-áéíóúÁÉÍÓÚüÜñÑ]+")) {
            showAlert("Dirección contiene caracteres inválidos.");
            return;
        }

        if (!tfEmail.getText().matches("[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}")) {
            showAlert("Email inválido.");
            return;
        }

        if (station == null) {
            station = new Station();
        }

        station.setMalla(tfMalla.getText());
        station.setStation(tfStation.getText());
        station.setAddress(tfAddress.getText());
        station.setMunicipio(tfMunicipio.getText());
        station.setType(cbType.getValue());
        station.setContactEmail(tfEmail.getText());
        station.setStatus(cbStatus.getValue());

        if (station.getIdStation() == 0) {
            stationRepo.save(station);
        } else {
            stationRepo.update(station);
        }

        close();
    }

    private void close() {
        Stage stage = (Stage) tfStation.getScene().getWindow();
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