package com.dev334.blood.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GovApiResponse {

    @SerializedName("records")
    private List<BloodBank> response;

    @SerializedName("total")
    private Integer total;

    public GovApiResponse() {
    }

    public List<BloodBank> getResponse() {
        return response;
    }

    public void setResponse(List<BloodBank> response) {
        this.response = response;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
