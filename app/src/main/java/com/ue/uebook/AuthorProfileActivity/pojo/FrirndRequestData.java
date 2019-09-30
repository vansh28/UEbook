package com.ue.uebook.AuthorProfileActivity.pojo;

import java.util.List;

public class FrirndRequestData {
    private Boolean error;
    private List<RequestData>data;

    public FrirndRequestData(Boolean error, List<RequestData> data) {
        this.error = error;
        this.data = data;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<RequestData> getData() {
        return data;
    }

    public void setData(List<RequestData> data) {
        this.data = data;
    }
}
