package com.ue.uebook.ChatSdk.Pojo;

import java.util.List;

public class ContactListResponse  {

    public Boolean error;
    public UserData data;
    public List<OponentData>userList;

    public ContactListResponse(Boolean error, UserData data, List<OponentData> userList) {
        this.error = error;
        this.data = data;
        this.userList = userList;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public List<OponentData> getUserList() {
        return userList;
    }

    public void setUserList(List<OponentData> userList) {
        this.userList = userList;
    }
}
