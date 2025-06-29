package com.sgs.controller;

import com.sgs.model.MaintenanceType;
import com.sgs.repository.MaintenanceTypeRepository;
import com.sgs.repository.VisitRepository;
import com.sgs.util.report.VisitReportGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.time.LocalDate;

public class VisitReportDialogController {

    @FXML private ChoiceBox<String> cbFilterType;
    @FXML private ComboBox<MaintenanceType> cbMaintenanceType;
    @FXML private TextField tfResult;
    @FXML private DatePicker dpStartDate;
    @FXML private DatePicker dpEndDate;

    private final MaintenanceTypeRepository typeRepo = new MaintenanceTypeRepository();
    private final VisitRepository visitRepo = new VisitRepository();

    @FXML
    public void initialize() {
        cbFilterType.getItems().addAll(
                "Tipo de Mantenimiento",
                "Resultado",
                "Fecha"
        );

        cbFilterType.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> updateFields(newVal)
        );

        cbMaintenanceType.getItems().addAll(typeRepo.findAll());
        cbMaintenanceType.setConverter(new StringConverter<>() {
            @Override
            public String toString(MaintenanceType type) {
                return type != null ? type.getName() : "";
            }

            @Override
            public MaintenanceType fromString(String s) {
                return null;
            }
        });

        disableAllFields();
    }

    private void updateFields(String selected) {
        disableAllFields();
        if ("Tipo de Mantenimiento".equals(selected)) {
            cbMaintenanceType.setDisable(false);
        } else if ("Resultado".equals(selected)) {
            tfResult.setDisable(false);
        } else if ("Fecha".equals(selected)) {
            dpStartDate.setDisable(false);
            dpEndDate.setDisable(false);
        }
    }

    private void disableAllFields() {
        cbMaintenanceType.setDisable(true);
        tfResult.setDisable(true);
        dpStartDate.setDisable(true);
        dpEndDate.setDisable(true);
    }

    @FXML
    public void onGenerateReport() {
        String selectedFilter = cbFilterType.getValue();
        if (selectedFilter == null) {
            showAlert("Selecciona un tipo de filtro.");
            return;
        }

        DirectoryChooser chooser = new DirectoryChooser();
        File directory = chooser.showDialog(getStage());
        if (directory == null) {
            showAlert("No se seleccionó un directorio.");
            return;
        }

        VisitReportGenerator generator = new VisitReportGenerator();

        switch (selectedFilter) {
            case "Tipo de Mantenimiento" -> {
                MaintenanceType selectedType = cbMaintenanceType.getValue();
                if (selectedType == null) {
                    showAlert("Selecciona un tipo de mantenimiento.");
                    return;
                }
                generator.exportByMaintenanceType(
                        directory.getAbsolutePath(),
                        selectedType.getName(),
                        visitRepo
                );
            }
            case "Resultado" -> {
                String result = tfResult.getText();
                if (result.isBlank()) {
                    showAlert("Ingresa un resultado.");
                    return;
                }
                generator.exportByResult(
                        directory.getAbsolutePath(),
                        result,
                        visitRepo
                );
            }
            case "Fecha" -> {
                LocalDate start = dpStartDate.getValue();
                LocalDate end = dpEndDate.getValue();
                LocalDate today = LocalDate.now();

                if (start == null || end == null) {
                    showAlert("Selecciona ambas fechas.");
                    return;
                }

                if (start.isAfter(end)) {
                    showAlert("La fecha de inicio no puede ser posterior a la fecha de fin.");
                    return;
                }

                if (start.isAfter(today) || end.isAfter(today)) {
                    showAlert("Las fechas no pueden ser mayores a la fecha actual.");
                    return;
                }

                generator.exportByDateRange(
                        directory.getAbsolutePath(),
                        start,
                        end,
                        visitRepo
                );
            }
        }

        showAlert("Reporte generado correctamente.");
        close();
    }

    @FXML
    public void onCancel() {
        close();
    }

    private void close() {
        Stage stage = getStage();
        if (stage != null) {
            stage.close();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private Stage getStage() {
        return (Stage) cbFilterType.getScene().getWindow();
    }
}
