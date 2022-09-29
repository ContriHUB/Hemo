package com.dev334.blood.model;

public class ChangePasswordModel {
    private String user_id, old_password, new_password;

    public ChangePasswordModel(String _id, String old_password, String new_password) {
        this.user_id = _id;
        this.old_password = old_password;
        this.new_password = new_password;
    }

    public String get_id() {
        return user_id;
    }

    public void set_id(String _id) {
        this.user_id = _id;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
