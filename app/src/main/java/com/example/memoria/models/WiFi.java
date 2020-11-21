package com.example.memoria.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    public   String MAC; //MAC access point
    public String red;
    public int wifiNets; // number of wifi networks
    public int frequency;
    public  int wifiRSSI;
    public double latency;
    //public String packages; //number of loss packages
    //DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public WiFi(){

    }

    public WiFi(String IMEI){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.IMEI = IMEI;
        this.createdAt = Calendar.getInstance().getTime();//df.parse(String.valueOf(new Date()));//df.parse(String.valueOf(new Date()))   Calendar.getInstance().getTime()
        this.id = IMEI + df.format(createdAt); //DateFormat.getDateInstance().format(createdAt);// DateFormat.getDateInstance().format(createdAt)
        this.MAC = "";
        this.red = "";
        this.wifiNets = 0;
        this.frequency = 0;
        this.wifiRSSI = 0;
        this.latency = 0.0;
        //this.packages = "";
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

    /*public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }*/
}
