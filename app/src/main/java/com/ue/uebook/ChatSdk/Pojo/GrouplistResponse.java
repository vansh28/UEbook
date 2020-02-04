package com.ue.uebook.ChatSdk.Pojo;

import java.util.List;

public class GrouplistResponse {

    public Boolean error;
    public List<Grouplist> data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Grouplist> getGroup_details() {
        return data;
    }

    public void setGroup_details(List<Grouplist> group_details) {
        this.data = group_details;
    }
}
