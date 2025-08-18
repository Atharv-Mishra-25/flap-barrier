package com.dpdtech.application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDataResponse {
    @SerializedName("dbtime")
    @Expose
    private Object dbtime;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("Cat")
    @Expose
    private Object cat;
    @SerializedName("Ticket_Details")
    @Expose
    private Object ticketDetails;
    @SerializedName("type")
    @Expose
    private String type;

    public Object getDbtime() {
        return dbtime;
    }

    public void setDbtime(Object dbtime) {
        this.dbtime = dbtime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Object getCat() {
        return cat;
    }

    public void setCat(Object cat) {
        this.cat = cat;
    }

    public Object getTicketDetails() {
        return ticketDetails;
    }

    public void setTicketDetails(Object ticketDetails) {
        this.ticketDetails = ticketDetails;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}