package com.dev334.blood.model;

public class User {
    private String name;
    private String email;
    private String password;
    private Integer weight;
    private String gender;
    private String dob;
    private String blood;
    private String location;
    private String _id;
    private String token;
    private String phone;

    public User(){
        //empty
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String token, boolean random) {
        this.email = email;
        this.password = password;
        this.token=token;
    }

    public User(Integer weight, String location, String phone,int x,String uid) {
        this.weight = weight;
        this.location = location;
        this.phone = phone;
        this._id=uid;
    }

    public User(String email, Integer weight, String gender, String dob, String bloodGroup, String location, String phone) {
        this.email=email;
        this.weight = weight;
        this.gender = gender;
        this.dob = dob;
        this.blood = bloodGroup;
        this.location = location;
        this.phone = phone;
    }


    public void setUserData(String name, String email, Integer weight, String gender, String dob, String blood, String location, String phone) {
        this.name = name;
        this.email = email;
        this.weight = weight;
        this.gender = gender;
        this.dob = dob;
        this.blood = blood;
        this.location = location;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getWeight() {
        return weight;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getBloodGroup() {
        return blood;
    }

    public String getLocation() {
        return location;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
