package com.ue.uebook.ChatSdk.Pojo;

import java.util.List;

public class GroupHistoryResponse {
    private Boolean error;
    private List<GroupMessageLIst>data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<GroupMessageLIst> getData() {
        return data;
    }

    public void setData(List<GroupMessageLIst> data) {
        this.data = data;
    }
}
