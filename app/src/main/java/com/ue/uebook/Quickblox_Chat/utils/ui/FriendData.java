package com.ue.uebook.Quickblox_Chat.utils.ui;

public class FriendData {
    private String id;
    private String user_id;

    private String status;
    private String request_date;
    private String url;
    private String user_name;
    private String chat_id;
    private String publisher_type;

    public FriendData(String id, String user_id, String status, String request_date, String url, String user_name, String chat_id, String publisher_type) {
        this.id = id;
        this.user_id = user_id;
        this.status = status;
        this.request_date = request_date;
        this.url = url;
        this.user_name = user_name;
        this.chat_id = chat_id;
        this.publisher_type = publisher_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequest_date() {
        return request_date;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getPublisher_type() {
        return publisher_type;
    }

    public void setPublisher_type(String publisher_type) {
        this.publisher_type = publisher_type;
    }
}
