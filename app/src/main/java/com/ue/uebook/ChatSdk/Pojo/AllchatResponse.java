package com.ue.uebook.ChatSdk.Pojo;

import java.util.List;

public class AllchatResponse {
    private List<UserchatList>userList;

    public List<UserchatList> getUserList() {
        return userList;
    }

    public void setUserList(List<UserchatList> userList) {
        this.userList = userList;
    }
}
