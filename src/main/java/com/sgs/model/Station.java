package com.sgs.model;

public class Station {
    private int idStation;
    private String name;
    private String address;
    private String type;
    private String contactEmail;
    private String status;

    public Station() {}

    public Station(int id, String name, String address, String type, String email, String status) {
        this.idStation = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.contactEmail = email;
        this.status = status;
    }

    public int getIdStation() { return idStation; }
    public void setIdStation(int id) { this.idStation = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
