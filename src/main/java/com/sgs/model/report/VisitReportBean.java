package com.sgs.model.report;

import java.time.LocalDate;

public class VisitReportBean {
    private int idVisit;
    private String maintenanceTypeName;
    private LocalDate date;
    private String result;
    private String observations;

    public VisitReportBean() {}

    public void setIdVisit(int idVisit) {
        this.idVisit = idVisit;
    }

    public void setMaintenanceTypeName(String maintenanceTypeName) {
        this.maintenanceTypeName = maintenanceTypeName;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public int getIdVisit() {
        return idVisit;
    }

    public String getMaintenanceTypeName() {
        return maintenanceTypeName;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getResult() {
        return result;
    }

    public String getObservations() {
        return observations;
    }
}
