package com.ue.uebook.ChatSdk.Pojo;

import java.util.List;

public class StarredResponse {
    public Boolean error;
    public List<StarredAllResponse>data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<StarredAllResponse> getData() {
        return data;
    }

    public void setData(List<StarredAllResponse> data) {
        this.data = data;
    }
}
