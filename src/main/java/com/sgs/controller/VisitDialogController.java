package com.sgs.controller;

import com.sgs.model.Station;
import com.sgs.model.User;
import com.sgs.model.Visit;
import com.sgs.model.MaintenanceType;
import com.sgs.repository.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.time.LocalDate;
import java.util.List;

public class VisitDialogController {

    @FXML private ComboBox<Station> cbStation;
    @FXML private ComboBox<User> cbUser;
    @FXML private ComboBox<MaintenanceType> cbType;
    @FXML private DatePicker dpDate;
    @FXML private TextField tfResult;
    @FXML private TextArea taObs;
    @FXML private DialogPane dialogPane;

    private Visit visit;
    private final VisitRepository visitRepo = new VisitRepository();
    private final StationRepository stationRepo = new StationRepository();
    private final UserRepository userRepo = new UserRepository();
    private final MaintenanceTypeRepository typeRepo = new MaintenanceTypeRepository();

    @FXML
    public void initialize() {
        cbStation.getItems().addAll(stationRepo.findAll());
        cbUser.getItems().addAll(userRepo.findAll().stream().filter(u -> "technician".equalsIgnoreCase(u.getRole())).toList());
        cbType.getItems().addAll(typeRepo.findAll());

        cbStation.setConverter(new javafx.util.StringConverter<>() {
            public String toString(Station s) {
                return s != null ? s.getStation() : "";
            }

            public Station fromString(String s) {
                return null;
            }
        });

        cbUser.setConverter(new javafx.util.StringConverter<>() {
            public String toString(User u) { return u != null ? u.getName() : ""; }
            public User fromString(String s) { return null; }
        });

        cbType.setConverter(new javafx.util.StringConverter<>() {
            public String toString(MaintenanceType t) { return t != null ? t.getName() : ""; }
            public MaintenanceType fromString(String s) { return null; }
        });

        // Configurar los botones personalizados
        Platform.runLater(() -> {
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.setText("Guardar");
            okButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white;");

            // Añadir manejador de eventos para el botón OK
            dialogPane.lookupButton(ButtonType.OK).addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                event.consume(); // Evitar cierre automático
                onSave();
            });

            // Personalizar el botón Cancel si es necesario
            Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
            cancelButton.setText("Cancelar");

            // Añadir manejador de eventos para el botón Cancel
            dialogPane.lookupButton(ButtonType.CANCEL).addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                event.consume();
                onCancel();
            });
        });
    }

    public void setVisit(Visit visit) {
        this.visit = visit;

        if (visit != null) {
            cbStation.getSelectionModel().select(findById(cbStation.getItems(), visit.getIdStation()));
            cbUser.getSelectionModel().select(findById(cbUser.getItems(), visit.getIdUser()));
            cbType.getSelectionModel().select(findById(cbType.getItems(), visit.getIdMaintenanceType()));
            dpDate.setValue(visit.getDate());
            tfResult.setText(visit.getResult());
            taObs.setText(visit.getObservations());
        }
    }

    @FXML
    public void onSave() {
        Station selectedStation = cbStation.getValue();
        User selectedUser = cbUser.getValue();
        MaintenanceType selectedType = cbType.getValue();
        LocalDate date = dpDate.getValue();

        if (selectedStation == null || selectedUser == null || selectedType == null || date == null) {
            showAlert("Por favor completa todos los campos obligatorios.");
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            showAlert("La fecha de mantenimiento no puede ser anterior a hoy.");
            return;
        }

        if (visit == null) visit = new Visit();

        visit.setIdStation(selectedStation.getIdStation());
        visit.setIdUser(selectedUser.getIdUser());
        visit.setIdMaintenanceType(selectedType.getIdType());
        visit.setDate(date);
        visit.setResult(tfResult.getText());
        visit.setObservations(taObs.getText());

        if (visit.getIdVisit() == 0) {
            visitRepo.save(visit);
        } else {
            visitRepo.update(visit);
        }

        close();
    }

    @FXML
    public void onCancel() {
        close();
    }

    private <T> T findById(List<T> list, int id) {
        for (T item : list) {
            if (item instanceof Station s && s.getIdStation() == id) return item;
            if (item instanceof User u && u.getIdUser() == id) return item;
            if (item instanceof MaintenanceType t && t.getIdType() == id) return item;
        }
        return null;
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