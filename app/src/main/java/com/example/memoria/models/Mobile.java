package com.example.memoria.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Mobile extends RealmObject {
    @PrimaryKey
    private String id; // IMEI+createdAt
    @Required
    private String IMEI;
    private Date createdAt;
    private String red; //ISP
    private String technology;
    private int mobileRSSI;
    private double latency;
    private double packages; //number of loss packages

    public Mobile(){

    }

    public Mobile(String id){
        this.id = "";
        this.IMEI = "";
        this.createdAt = new Date();
        this.red = "";
        this.technology = "";
        this.mobileRSSI = 0;
        this.latency = 0.0;
        this.packages = 0.0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public int getMobileRSSI() {
        return mobileRSSI;
    }

    public void setMobileRSSI(int mobileRSSI) {
        this.mobileRSSI = mobileRSSI;
    }

    public double getLatency() {
        return latency;
    }

    public void setLatency(double latency) {
        this.latency = latency;
    }

    public double getPackages() {
        return packages;
    }

    public void setPackages(double packages) {
        this.packages = packages;
    }
}
