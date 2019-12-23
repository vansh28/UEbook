package com.ue.uebook.HomeActivity.HomeFragment.Pojo;

import java.util.List;

public class HomeListingResponse {
    private String error;
    private List<HomeListing>data;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<HomeListing> getData() {
        return data;
    }

    public void setData(List<HomeListing> data) {
        this.data = data;
    }
}
