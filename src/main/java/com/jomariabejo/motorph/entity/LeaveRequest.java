package com.jomariabejo.motorph.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class LeaveRequest {

    private int leaveRequestID;
    private int employeeID;
    private int leaveRequestCategoryId;
    private Date startDate;
    private Date endDate;
    private Timestamp dateCreated;
    private String reason;
    private LeaveRequestStatus status;

    public enum LeaveRequestStatus {
        Approved,
        Disapproved,
        Pending
    }

    public LeaveRequest() {
    }

    public LeaveRequest(int employeeID, int getLeaveRequestID, Date startDate, Date endDate, Timestamp dateCreated, String reason, LeaveRequestStatus status) {
        this.employeeID = employeeID;
        this.leaveRequestCategoryId = getLeaveRequestID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateCreated = dateCreated;
        this.reason = reason;
        this.status = status;
    }

    public LeaveRequest(int leaveRequestID, int employeeID, int getLeaveRequestID, Date startDate, Date endDate, Timestamp dateCreated, String reason, LeaveRequestStatus status) {
        this.leaveRequestID = leaveRequestID;
        this.employeeID = employeeID;
        this.leaveRequestCategoryId = getLeaveRequestID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateCreated = dateCreated;
        this.reason = reason;
        this.status = status;
    }

    public int getLeaveRequestID() {
        return leaveRequestID;
    }

    public void setLeaveRequestID(int leaveRequestID) {
        this.leaveRequestID = leaveRequestID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getLeaveRequestCategoryId() {
        return leaveRequestCategoryId;
    }

    public void setLeaveRequestCategoryId(int leaveRequestCategoryId) {
        this.leaveRequestCategoryId = leaveRequestCategoryId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LeaveRequestStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveRequestStatus status) {
        this.status = status;
    }
}
