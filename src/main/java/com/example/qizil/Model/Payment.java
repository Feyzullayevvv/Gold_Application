package com.example.qizil.Model;

public class Payment {
    private int systemNr;
    private String date;
    private int customerNr;
    private String customerName;
    private String eyar;
    private double gram;

    public int getSystemNr() {
        return systemNr;
    }

    public void setSystemNr(int systemNr) {
        this.systemNr = systemNr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCustomerNr() {
        return customerNr;
    }

    public void setCustomerNr(int customerNr) {
        this.customerNr = customerNr;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEyar() {
        return eyar;
    }

    public void setEyar(String eyar) {
        this.eyar = eyar;
    }

    public double getGram() {
        return gram;
    }

    public void setGram(double gram) {
        this.gram = gram;
    }
}
