package com.dpdtech.application.network.models.request;

public class LoginRequest {
    private String apkName;
    private String manufacturer;
    private String modelNumber;
    private String password;
    private String plutusTerminalID;
    private String serialNumber;
    private String username;

    // Constructor
    public LoginRequest(String apkName, String manufacturer, String modelNumber,
                        String password, String plutusTerminalID, String serialNumber, String username) {
        this.apkName = apkName;
        this.manufacturer = manufacturer;
        this.modelNumber = modelNumber;
        this.password = password;
        this.plutusTerminalID = plutusTerminalID;
        this.serialNumber = serialNumber;
        this.username = username;
    }

    // Getters and Setters
    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlutusTerminalID() {
        return plutusTerminalID;
    }

    public void setPlutusTerminalID(String plutusTerminalID) {
        this.plutusTerminalID = plutusTerminalID;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // toString Method
    @Override
    public String toString() {
        return "LoginRequest{" +
                "apkName='" + apkName + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", modelNumber='" + modelNumber + '\'' +
                ", password='" + password + '\'' +
                ", plutusTerminalID='" + plutusTerminalID + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}