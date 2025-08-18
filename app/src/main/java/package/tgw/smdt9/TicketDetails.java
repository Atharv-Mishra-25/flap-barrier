package com.dpdtech.application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketDetails {

    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("LastScan")
    @Expose
    private String lastScan;
    @SerializedName("TotalTicket")
    @Expose
    private String totalTicket;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLastScan() {
        return lastScan;
    }

    public void setLastScan(String lastScan) {
        this.lastScan = lastScan;
    }

    public String getTotalTicket() {
        return totalTicket;
    }

    public void setTotalTicket(String totalTicket) {
        this.totalTicket = totalTicket;
    }

}
