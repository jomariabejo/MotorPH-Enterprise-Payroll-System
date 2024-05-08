package com.jomariabejo.motorph.entity;

import java.sql.Date;

public class Department {
    private int departmentID;
    private String name;
    private String description;
    private Date dateCreated;

    public Department() {
    }

    public Department(String name, String description, Date dateCreated) {
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
    }

    public Department(int departmentID, String name, String description, Date dateCreated) {
        this.departmentID = departmentID;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
