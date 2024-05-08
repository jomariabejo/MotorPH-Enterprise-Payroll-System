package com.jomariabejo.motorph.entity;

import java.math.BigDecimal;
import java.sql.Date;

public class Deduction {
    private int deductionID;
    private int employeeID;
    private BigDecimal sss;
    private BigDecimal philhealth;
    private BigDecimal pagibig;
    private Date dateCreated;

    public Deduction() {
    }

    public Deduction(int deductionID, int employeeID, BigDecimal sss, BigDecimal philhealth, BigDecimal pagibig, Date dateCreated) {
        this.deductionID = deductionID;
        this.employeeID = employeeID;
        this.sss = sss;
        this.philhealth = philhealth;
        this.pagibig = pagibig;
        this.dateCreated = dateCreated;
    }

    public Deduction(int employeeID, BigDecimal sss, BigDecimal philhealth, BigDecimal pagibig, Date dateCreated) {
        this.employeeID = employeeID;
        this.sss = sss;
        this.philhealth = philhealth;
        this.pagibig = pagibig;
        this.dateCreated = dateCreated;
    }

    public int getDeductionID() {
        return deductionID;
    }

    public void setDeductionID(int deductionID) {
        this.deductionID = deductionID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public BigDecimal getSss() {
        return sss;
    }

    public void setSss(BigDecimal sss) {
        this.sss = sss;
    }

    public BigDecimal getPhilhealth() {
        return philhealth;
    }

    public void setPhilhealth(BigDecimal philhealth) {
        this.philhealth = philhealth;
    }

    public BigDecimal getPagibig() {
        return pagibig;
    }

    public void setPagibig(BigDecimal pagibig) {
        this.pagibig = pagibig;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public BigDecimal totalContributions() {
        return this.pagibig.add(philhealth).add(sss);
    }

    @Override
    public String toString() {
        return "Deduction{" +
                "deductionID=" + deductionID +
                ", employeeID=" + employeeID +
                ", sss=" + sss +
                ", philhealth=" + philhealth +
                ", pagibig=" + pagibig +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
