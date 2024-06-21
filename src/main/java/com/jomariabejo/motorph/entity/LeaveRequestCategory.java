package com.jomariabejo.motorph.entity;

public class LeaveRequestCategory {
    private int leaveRequestCategoryID;
    private String categoryName;
    private String description;
    private byte maxCredits;

    public LeaveRequestCategory() {
    }

    public LeaveRequestCategory(String categoryName, String description, byte maxCredits) {
        this.categoryName = categoryName;
        this.description = description;
        this.maxCredits = maxCredits;
    }

    public LeaveRequestCategory(int leaveRequestCategoryID, String categoryName, String description, byte maxCredits) {
        this.leaveRequestCategoryID = leaveRequestCategoryID;
        this.categoryName = categoryName;
        this.description = description;
        this.maxCredits = maxCredits;
    }

    public int getLeaveRequestCategoryID() {
        return leaveRequestCategoryID;
    }

    public void setLeaveRequestCategoryID(int leaveRequestCategoryID) {
        this.leaveRequestCategoryID = leaveRequestCategoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte getMaxCredits() {
        return maxCredits;
    }

    public void setMaxCredits(byte maxCredits) {
        this.maxCredits = maxCredits;
    }
}
