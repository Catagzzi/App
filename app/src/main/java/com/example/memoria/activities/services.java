package com.example.memoria.activities;

public class services {
    public String name;
    public double throughput;
    public int icon;

    public services(){

    }

    public services(String name, double throughput, int icon) {
        this.name = name;
        this.throughput = throughput;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getThroughput() {
        return throughput;
    }

    public void setThroughput(int throughput) {
        this.throughput = throughput;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
