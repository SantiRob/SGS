package com.sgs.model;

public class User {
    private int idUser;
    private String sapUser;
    private String name;
    private String email;
    private String role;
    private String password;

    public User() {
    }

    public User(int idUser, String sapUser, String name, String email, String role, String password) {
        this.idUser = idUser;
        this.sapUser = sapUser;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getSapUser() {return sapUser;}

    public void setSapUser(String sapUser) { this.sapUser = sapUser; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
