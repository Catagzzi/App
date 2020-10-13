package com.example.memoria.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class WiFi extends RealmObject {
    @PrimaryKey
    private String id;
    @Required
    private String IMEI;
    private Date createdAt;
    @Required
    private  String MAC; //MAC access point
    private String red;
    private int wifiNets; // number of wifi networks
    private int frequency;
    private  int wifiRSSI;
    private double latency;
    private double packages; //number of loss packages

    public WiFi(){

    }

    public WiFi(String id){
        this.id = "";
        this.IMEI = "";
        this.createdAt = new Date();
        this.MAC = "";
        this.red = "";
        this.wifiNets = 0;
        this.frequency = 0;
        this.wifiRSSI = 0;
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

    /*public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }*/

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

    public int getWifiNets() {
        return wifiNets;
    }

    public void setWifiNets(int wifiNets) {
        this.wifiNets = wifiNets;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getWifiRSSI() {
        return wifiRSSI;
    }

    public void setWifiRSSI(int wifiRSSI) {
        this.wifiRSSI = wifiRSSI;
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
