package com.dev334.blood.model;

import com.google.gson.annotations.SerializedName;

public class BloodBank {

    @SerializedName("_blood_bank_name")
    private String bankName;

    @SerializedName("_longitude")
    private Double longitude;

    @SerializedName("_latitude")
    private Double latitude;

    @SerializedName("_contact_no")
    private String contact;

    @SerializedName("_address")
    private String address;

    @SerializedName("sr_no")
    private String id;

    private double distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
