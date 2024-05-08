package com.jomariabejo.motorph.entity;

import java.sql.Timestamp;

public class Position {
    private int positionID;
    private String name;
    private Timestamp dateCreated;
    private String description;

    public Position() {
    }

    public Position(String name, Timestamp dateCreated, String description) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.description = description;
    }

    public Position(int positionID, String name, Timestamp dateCreated, String description) {
        this.positionID = positionID;
        this.name = name;
        this.dateCreated = dateCreated;
        this.description = description;
    }

    public int getPositionID() {
        return positionID;
    }

    public void setPositionID(int positionID) {
        this.positionID = positionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
