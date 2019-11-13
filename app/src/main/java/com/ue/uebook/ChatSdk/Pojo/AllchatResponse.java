package com.ue.uebook.ChatSdk.Pojo;

import java.util.List;

public class AllchatResponse {
    private Data data;
    private List<UserList> userList;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<UserList> getUserList() {
        return userList;
    }

    public void setUserList(List<UserList> userList) {
        this.userList = userList;
    }
}
