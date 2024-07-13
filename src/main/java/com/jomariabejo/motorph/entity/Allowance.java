package com.jomariabejo.motorph.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class Allowance {
    private int allowanceID;
    private int employeeID;
    private int clothingAllowance;
    private int riceAllowance;
    private int phoneAllowance;
    private int totalAmount;
    private Date dateCreated;
    private Timestamp dateModified;

    public Allowance() {

    }

    public Allowance(int employeeID, int clothingAllowance, int riceAllowance, int phoneAllowance, int totalAmount, Date dateCreated, Timestamp dateModified) {
        this.employeeID = employeeID;
        this.clothingAllowance = clothingAllowance;
        this.riceAllowance = riceAllowance;
        this.phoneAllowance = phoneAllowance;
        this.totalAmount = totalAmount;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    public Allowance(int allowanceID, int employeeID, int clothingAllowance, int riceAllowance, int phoneAllowance, int totalAmount, Date dateCreated, Timestamp dateModified) {
        this.allowanceID = allowanceID;
        this.employeeID = employeeID;
        this.clothingAllowance = clothingAllowance;
        this.riceAllowance = riceAllowance;
        this.phoneAllowance = phoneAllowance;
        this.totalAmount = totalAmount;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }


    public int getAllowanceID() {
        return allowanceID;
    }

    public void setAllowanceID(int allowanceID) {
        this.allowanceID = allowanceID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getClothingAllowance() {
        return clothingAllowance;
    }

    public void setClothingAllowance(int clothingAllowance) {
        this.clothingAllowance = clothingAllowance;
    }

    public int getRiceAllowance() {
        return riceAllowance;
    }

    public void setRiceAllowance(int riceAllowance) {
        this.riceAllowance = riceAllowance;
    }

    public int getPhoneAllowance() {
        return phoneAllowance;
    }

    public void setPhoneAllowance(int phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateModified() {
        return dateModified;
    }

    public void setDateModified(Timestamp dateModified) {
        this.dateModified = dateModified;
    }

    @Override
    public String toString() {
        return "Allowance{" +
                "allowanceID=" + allowanceID +
                ", employeeID=" + employeeID +
                ", clothingAllowance=" + clothingAllowance +
                ", riceAllowance=" + riceAllowance +
                ", phoneAllowance=" + phoneAllowance +
                ", totalAmount=" + totalAmount +
                ", dateCreated=" + dateCreated +
                ", dateModified=" + dateModified +
                '}';
    }
}
