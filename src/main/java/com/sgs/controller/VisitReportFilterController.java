package com.sgs.controller;

import com.sgs.repository.MaintenanceTypeRepository;
import com.sgs.repository.VisitRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;

public class VisitReportFilterController {

    @FXML
    private ComboBox<String> filterTypeCombo;
    @FXML
    private VBox filterInputContainer;
    @FXML
    private Button generateReportBtn;

    private final MaintenanceTypeRepository maintenanceRepo = new MaintenanceTypeRepository();
    private final VisitRepository visitRepo = new VisitRepository();

    private ComboBox<String> maintenanceTypeCombo;
    private ComboBox<String> resultCombo;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    private ObservableList<String> loadVisitResults() {
        return FXCollections.observableArrayList(
                visitRepo.findDistinctResults()
        );
    }

    public void handleFilterTypeChange() {
        String selectedFilter = filterTypeCombo.getValue();
        filterInputContainer.getChildren().clear();

        switch (selectedFilter) {
            case "Tipo Mantenimiento":
                maintenanceTypeCombo = new ComboBox<>();
                maintenanceTypeCombo.setPromptText("Seleccione tipo de mantenimiento");
                maintenanceTypeCombo.setItems(loadMaintenanceTypes());
                filterInputContainer.getChildren().add(maintenanceTypeCombo);
                break;

            case "Resultado":
                resultCombo = new ComboBox<>();
                resultCombo.setPromptText("Seleccione resultado");
                resultCombo.setItems(loadVisitResults());
                filterInputContainer.getChildren().add(resultCombo);
                break;

            case "Rango de Fechas":
                startDatePicker = new DatePicker();
                startDatePicker.setPromptText("Fecha inicio");
                endDatePicker = new DatePicker();
                endDatePicker.setPromptText("Fecha fin");
                VBox dateBox = new VBox(10, startDatePicker, endDatePicker);
                filterInputContainer.getChildren().add(dateBox);
                break;

            default:
                break;
        }
    }

    private ObservableList<String> loadMaintenanceTypes() {
        return FXCollections.observableArrayList(
                maintenanceRepo.findAllNames()
        );
    }

    @FXML
    public void initialize() {
        filterTypeCombo.setItems(FXCollections.observableArrayList(
                "Tipo Mantenimiento",
                "Resultado",
                "Rango de Fechas"
        ));

        filterTypeCombo.setOnAction(e -> handleFilterTypeChange());
    }

    @FXML
    public void onGenerateReport(ActionEvent event) {
        String selectedFilter = filterTypeCombo.getValue();
        if (selectedFilter == null) {
            System.out.println("Debe seleccionar un filtro.");
            return;
        }

        switch (selectedFilter) {
            case "Tipo Mantenimiento":
                generateByMaintenanceType();
                break;

            case "Resultado":
                generateByResult();
                break;

            case "Rango de Fechas":
                generateByDateRange();
                break;

            default:
                System.out.println("Filtro no reconocido.");
                break;
        }
    }

    private void generateByMaintenanceType() {
        if (maintenanceTypeCombo == null || maintenanceTypeCombo.getValue() == null) {
            System.out.println("Debe seleccionar un tipo de mantenimiento.");
            return;
        }

        String selectedType = maintenanceTypeCombo.getValue();
        System.out.println("Filtro: Tipo Mantenimiento = " + selectedType);

        var visits = visitRepo.findByMaintenanceType(selectedType);
        if (visits.isEmpty()) {
            System.out.println("No hay registros para este tipo de mantenimiento.");
            return;
        }

        // Generar PDF con JasperReports (aquí va tu clase generadora)
        // VisitReportGenerator generator = new VisitReportGenerator();
        // generator.generate(visits, "reportes/visit_by_maintenance.jasper");
        System.out.println("Reporte generado exitosamente (simulado).");
    }


    private void generateByResult() {
        if (resultCombo == null || resultCombo.getValue() == null) {
            System.out.println("Debe seleccionar un resultado.");
            return;
        }

        String selectedResult = resultCombo.getValue();
        System.out.println("Filtro: Resultado = " + selectedResult);

        var visits = visitRepo.findByResult(selectedResult);

        if (visits.isEmpty()) {
            System.out.println("No hay registros para este resultado.");
            return;
        }

        // VisitReportGenerator generator = new VisitReportGenerator();
        // generator.generate(visits, "reportes/visit_by_result.jasper");
        System.out.println("Reporte generado exitosamente (simulado).");
    }


    private void generateByDateRange() {
        if (startDatePicker == null || endDatePicker == null ||
                startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            System.out.println("Debe seleccionar ambas fechas.");
            return;
        }

        var start = startDatePicker.getValue();
        var end = endDatePicker.getValue();

        if (end.isBefore(start)) {
            System.out.println("La fecha fin no puede ser anterior a la fecha inicio.");
            return;
        }

        System.out.println("Filtro: Rango de Fechas = " + start + " hasta " + end);

        var visits = visitRepo.findByDateRange(start, end);

        if (visits.isEmpty()) {
            System.out.println("No hay registros para este rango de fechas.");
            return;
        }

        // VisitReportGenerator generator = new VisitReportGenerator();
        // generator.generate(visits, "reportes/visit_by_date_range.jasper");
        System.out.println("Reporte generado exitosamente (simulado).");
    }
}
