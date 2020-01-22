package com.ue.uebook.AuthorProfileActivity.pojo;

import java.util.List;

public class StatusPojo {

    private Boolean error;
    private List<RequestData> data;
     private  String channelId;

    public List<RequestData> getData() {
        return data;
    }

    public void setData(List<RequestData> data) {
        this.data = data;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }




}
