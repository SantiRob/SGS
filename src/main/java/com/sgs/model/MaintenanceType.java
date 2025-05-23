package com.sgs.model;

public class MaintenanceType {
    private int idType;
    private String name;
    private String description;

    public MaintenanceType() {}

    public MaintenanceType(int id, String name, String description) {
        this.idType = id;
        this.name = name;
        this.description = description;
    }

    public int getIdType() { return idType; }
    public void setIdType(int idType) { this.idType = idType; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
