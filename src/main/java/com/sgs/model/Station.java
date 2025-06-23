package com.sgs.model;

public class Station {
    private int idStation;
    private String station;
    private String address;
    private String type;
    private String contactEmail;
    private String status;
    private String malla;
    private String municipio;

    public Station() {}

    public Station(int id, String station, String address, String type, String email, String status, String malla, String municipio) {
        this.idStation = id;
        this.station = station;
        this.address = address;
        this.type = type;
        this.contactEmail = email;
        this.status = status;
        this.malla = malla;
        this.municipio = municipio;
    }

    public int getIdStation() { return idStation; }
    public void setIdStation(int idStation) { this.idStation = idStation; }

    public String getStation() { return station; }
    public void setStation(String station) { this.station = station; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMalla() { return malla; }
    public void setMalla(String malla) { this.malla = malla; }

    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }
}
