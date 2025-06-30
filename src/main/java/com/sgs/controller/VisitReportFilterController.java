package com.sgs.controller;

import com.sgs.model.Visit;
import com.sgs.repository.MaintenanceTypeRepository;
import com.sgs.repository.VisitRepository;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitReportFilterController {

    @FXML
    private ComboBox<String> filterTypeCombo;
    @FXML
    private VBox filterInputContainer;
    @FXML
    private Button generateReportBtn;
    @FXML
    private Button applyFilterBtn;
    @FXML
    private TableView<Visit> filteredVisitsTable;
    @FXML
    private TableColumn<Visit, Integer> colId;
    @FXML
    private TableColumn<Visit, String> colTypeMainteinance;
    @FXML
    private TableColumn<Visit, String> colDate;
    @FXML
    private TableColumn<Visit, String> colResults;
    @FXML
    private TableColumn<Visit, String> colObservations;

    private final MaintenanceTypeRepository maintenanceRepo = new MaintenanceTypeRepository();
    private final VisitRepository visitRepo = new VisitRepository();

    private ComboBox<String> maintenanceTypeCombo;
    private ComboBox<String> resultCombo;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    private final Map<Integer, String> typeMap = new HashMap<>();
    private List<Visit> currentVisits = List.of();

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

        maintenanceRepo.findAll().forEach((type -> typeMap.put(type.getIdType(), type.getName())));

        filterTypeCombo.setOnAction(e -> handleFilterTypeChange());
        colId.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdVisit()).asObject());
        colTypeMainteinance.setCellValueFactory(data ->
                new SimpleStringProperty(typeMap.getOrDefault(data.getValue().getIdMaintenanceType(), " ")));
        colDate.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDate().toString()));
        colResults.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getResult()));
        colObservations.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getObservations()));

        // Al inicio, deshabilita Generar Reporte
        generateReportBtn.setDisable(true);

        filteredVisitsTable.setPlaceholder(new Label("No hay registros para mostrar."));
    }

    @FXML
    public void onApplyFilter(ActionEvent event) {
        String selectedFilter = filterTypeCombo.getValue();
        if (selectedFilter == null) {
            System.out.println("Debe seleccionar un filtro.");
            return;
        }

        switch (selectedFilter) {
            case "Tipo Mantenimiento":
                applyFilterByMaintenanceType();
                break;
            case "Resultado":
                applyFilterByResult();
                break;
            case "Rango de Fechas":
                applyFilterByDateRange();
                break;
            default:
                System.out.println("Filtro no reconocido.");
        }
        generateReportBtn.setDisable(currentVisits.isEmpty());
    }

    @FXML
    public void onGenerateReport(ActionEvent event) {
        if (currentVisits == null || currentVisits.isEmpty()) {
            System.out.println("No hay registros para generar el reporte.");
            return;
        }

        System.out.println("Generando PDF con " + currentVisits.size() + " registros filtrados...");
        // VisitReportGenerator generator = new VisitReportGenerator();
        // generator.generate(currentVisits, "reportes/visit_filtered.jasper");
    }

    private void applyFilterByMaintenanceType() {
        if (maintenanceTypeCombo == null || maintenanceTypeCombo.getValue() == null) {
            System.out.println("Debe seleccionar un tipo de mantenimiento.");
            return;
        }

        String selectedType = maintenanceTypeCombo.getValue();
        System.out.println("Aplicando filtro: Tipo Mantenimiento = " + selectedType);

        currentVisits = visitRepo.findByMaintenanceType(selectedType);

        filteredVisitsTable.setItems(FXCollections.observableArrayList(currentVisits));
        System.out.println("Tabla actualizada con " + currentVisits.size() + " registros.");
    }

    private void applyFilterByResult() {
        if (resultCombo == null || resultCombo.getValue() == null) {
            System.out.println("Debe seleccionar un resultado.");
            return;
        }

        String selectedResult = resultCombo.getValue();
        System.out.println("Aplicando filtro: Resultado = " + selectedResult);

        currentVisits = visitRepo.findByResult(selectedResult);

        filteredVisitsTable.setItems(FXCollections.observableArrayList(currentVisits));
        System.out.println("Tabla actualizada con " + currentVisits.size() + " registros.");
    }

    private void applyFilterByDateRange() {
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

        System.out.println("Aplicando filtro: Rango de Fechas = " + start + " hasta " + end);

        currentVisits = visitRepo.findByDateRange(start, end);

        filteredVisitsTable.setItems(FXCollections.observableArrayList(currentVisits));
        System.out.println("Tabla actualizada con " + currentVisits.size() + " registros.");
    }
}
