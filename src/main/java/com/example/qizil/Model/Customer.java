package com.example.qizil.Model;

import java.text.DecimalFormat;

public class Customer {
    private String name;
    private double borc_585;
    private double borc_750;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBorc_585() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedValue = decimalFormat.format(borc_585);
        return Double.parseDouble(formattedValue);
    }

    public void setBorc_585(double borc_585) {
        this.borc_585 = borc_585;
    }

    public double getBorc_750() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedValue = decimalFormat.format(borc_750);
        return Double.parseDouble(formattedValue);
    }

    public void setBorc_750(double borc_750) {
        this.borc_750 = borc_750;
    }
}
