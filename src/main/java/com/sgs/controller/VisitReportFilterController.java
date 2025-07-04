package com.sgs.controller;

import com.sgs.model.Visit;
import com.sgs.model.report.VisitReportBean;
import com.sgs.repository.MaintenanceTypeRepository;
import com.sgs.repository.VisitRepository;
import com.sgs.util.report.VisitReportGenerator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;
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
    private String lastFilterDescription = "";

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
    public void onApplyFilter() {
        String selectedFilter = filterTypeCombo.getValue();
        if (selectedFilter == null) {
            System.out.println("Debe seleccionar un filtro.");
            return;
        }

        switch (selectedFilter) {
            case "Tipo Mantenimiento":
                applyFilterByMaintenanceType();
                lastFilterDescription = "Tipo de Mantenimiento: " + maintenanceTypeCombo.getValue();
                break;
            case "Resultado":
                applyFilterByResult();
                lastFilterDescription = "Resultado: " + resultCombo.getValue();
                break;
            case "Rango de Fechas":
                applyFilterByDateRange();
                lastFilterDescription = "Rango de Fechas: " + startDatePicker.getValue() + " a " + endDatePicker.getValue();
                break;
            default:
                System.out.println("Filtro no reconocido.");
        }
        generateReportBtn.setDisable(currentVisits.isEmpty());
    }

    @FXML
    public void onGenerateReport(ActionEvent event) {
        List<VisitReportBean> beans = currentVisits.stream().map(v -> {
            VisitReportBean bean = new VisitReportBean();
            bean.setIdVisit(v.getIdVisit());
            bean.setMaintenanceTypeName(typeMap.getOrDefault(v.getIdMaintenanceType(), "N/A"));
            bean.setDate(v.getDate());
            bean.setResult(v.getResult());
            bean.setObservations(v.getObservations());
            return bean;
        }).toList();

        if (beans.isEmpty()) {
            System.out.println("No hay registros para exportar.");
            return;
        }

        String fileName = askFileName();
        if (fileName == null) return;

        String outputPath = chooseDirectoryAndBuildPath(event, fileName);
        if (outputPath == null) return;

        File outputFile = new File(outputPath);
        if (outputFile.exists()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Archivo existente");
            alert.setHeaderText(null);
            alert.setContentText("Ya existe un archivo con ese nombre. Elimina el archivo o elige otro nombre.");
            alert.showAndWait();
            return;
        }

        System.out.println("Generando PDF con " + beans.size() + " registros filtrados...");
        VisitReportGenerator generator = new VisitReportGenerator();
        generator.export(beans, outputPath, lastFilterDescription);
    }

    private String askFileName() {

        TextInputDialog inputDialog = new TextInputDialog("visits-report");
        inputDialog.setTitle("Guardar Reporte");
        inputDialog.setHeaderText("Ingresa el nombre del archivo PDF:");
        inputDialog.setContentText("Nombre:");

        String fileName = inputDialog.showAndWait().orElse("").trim();
        if (fileName.isBlank()) {
            System.out.println("Operación cancelada: nombre vacío.");
            return null;
        }
        return fileName;
    }

    private String chooseDirectoryAndBuildPath(ActionEvent event, String fileName) {
        DirectoryChooser chooser = new DirectoryChooser();
        File directory = chooser.showDialog(((javafx.scene.Node) event.getSource()).getScene().getWindow());
        if (directory == null) {
            System.out.println("Operación cancelada: carpeta no seleccionada.");
            return null;
        }
        return directory.getAbsolutePath() + "/" + fileName + ".pdf";
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
