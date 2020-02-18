package com.ue.uebook.ChatSdk.Pojo;

public class GroupNameProfileResponse {
    public Boolean error;
    public Grouplist group_detail;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Grouplist getGroup_detail() {
        return group_detail;
    }

    public void setGroup_detail(Grouplist group_detail) {
        this.group_detail = group_detail;
    }
}
