package com.jomariabejo.motorph.entity;

public class Role {
    private int roleID;
    private String name;
    private String description;
    private int permissionID;

    public Role() {}

    public Role(String name, String description, int permissionID) {
        this.name = name;
        this.description = description;
        this.permissionID = permissionID;
    }

    public Role(int roleID, String name, String description, int permissionID) {
        this.roleID = roleID;
        this.name = name;
        this.description = description;
        this.permissionID = permissionID;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
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

    public int getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(int permissionID) {
        this.permissionID = permissionID;
    }
}
