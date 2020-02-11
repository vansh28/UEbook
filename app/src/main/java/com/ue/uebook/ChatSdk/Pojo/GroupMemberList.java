package com.ue.uebook.ChatSdk.Pojo;

import java.io.Serializable;

public class GroupMemberList implements Serializable {
    public  String id;
    public  String user_name;
    public  String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
