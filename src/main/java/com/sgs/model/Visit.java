package com.sgs.model;

import java.time.LocalDate;

public class Visit {
    private int idVisit;
    private int idStation;
    private int idUser;
    private int idMaintenanceType;
    private LocalDate date;
    private String result;
    private String observations;

    public Visit() {}

    public Visit(int idVisit, int idStation, int idUser, int idMaintenanceType, LocalDate date, String result, String observations) {
        this.idVisit = idVisit;
        this.idStation = idStation;
        this.idUser = idUser;
        this.idMaintenanceType = idMaintenanceType;
        this.date = date;
        this.result = result;
        this.observations = observations;
    }

    public int getIdVisit() { return idVisit; }
    public void setIdVisit(int idVisit) { this.idVisit = idVisit; }

    public int getIdStation() { return idStation; }
    public void setIdStation(int idStation) { this.idStation = idStation; }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public int getIdMaintenanceType() { return idMaintenanceType; }
    public void setIdMaintenanceType(int idMaintenanceType) { this.idMaintenanceType = idMaintenanceType; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
}
