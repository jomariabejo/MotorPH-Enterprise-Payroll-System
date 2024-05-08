package com.jomariabejo.motorph.entity;

public class Permission {
    private int permissionID;
    private String name;
    private String description;

    public Permission() {

    }

    public Permission(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Permission(int permissionID, String name, String description) {
        this.permissionID = permissionID;
        this.name = name;
        this.description = description;
    }

    public int getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(int permissionID) {
        this.permissionID = permissionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
