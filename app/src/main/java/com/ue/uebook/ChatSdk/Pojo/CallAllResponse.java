package com.ue.uebook.ChatSdk.Pojo;

import java.util.List;

public class CallAllResponse {
    public boolean error;
    public List<callResponse>data;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<callResponse> getData() {
        return data;
    }

    public void setData(List<callResponse> data) {
        this.data = data;
    }
}
