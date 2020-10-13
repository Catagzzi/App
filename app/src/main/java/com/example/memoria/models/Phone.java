package com.example.memoria.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Phone extends RealmObject {
    @PrimaryKey
    private String IMEI;
    private RealmList<WiFi> WiFis;
    private RealmList<Mobile> Mobiles;

    public Phone(){

    }

    public Phone(String IMEI){
        this.IMEI = "";
        this.WiFis = new RealmList<WiFi>();
        this.Mobiles = new RealmList<Mobile>();
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public RealmList<WiFi> getWiFis() {
        return WiFis;
    }

    public void setWiFis(RealmList<WiFi> wiFis) {
        WiFis = wiFis;
    }

    public RealmList<Mobile> getMobiles() {
        return Mobiles;
    }

    public void setMobiles(RealmList<Mobile> mobiles) {
        Mobiles = mobiles;
    }
}
