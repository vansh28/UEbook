package com.ue.uebook.ChatSdk.Pojo;

import java.util.List;

public class MemberListResponse {
    public  Boolean error;
    public List<GroupMemberList>user_list;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<GroupMemberList> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<GroupMemberList> user_list) {
        this.user_list = user_list;
    }
}
