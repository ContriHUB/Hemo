package com.dev334.blood.model;

import java.util.Date;

public class Blood {
    private String _id;


    private String user;
    private Double latitude;
    private Double longitude;
    private String location;
    private Integer quantity;
    private String blood;
    private Date created;
    private String info;
    private String file;
    private String name;
    private String phone;



    public Blood(){
        //empty constructor
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





    public Blood(String user, Double latitude, Double longitude, String location, Integer quantity, String blood, String file) {
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.quantity = quantity;
        this.blood = blood;
        this.file=file;

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFile() {
        return file;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


    public void setFile(String file) {
        this.file = file;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getCreated() {
        return created;
    }

    public String getUser() {
        return user;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getLocation() {
        return location;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getBlood() {
        return blood;
    }
}
