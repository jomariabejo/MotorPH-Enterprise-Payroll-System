package com.jomariabejo.motorph.entity;

public class User {
    private int userID;
    private int employeeID;
    private int roleID;
    private String username;
    private String password;
    private int verificationCode;

    public User() {
    }

    public User(int employeeID, int roleID, String username, String password) {
        this.employeeID = employeeID;
        this.roleID = roleID;
        this.username = username;
        this.password = password;
    }

    public User(int userID, int employeeID, int roleID,
                String username, String password) {
        this.userID = userID;
        this.employeeID = employeeID;
        this.roleID = roleID;
        this.username = username;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }
}
