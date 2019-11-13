package com.ue.uebook.ChatSdk.Pojo;

public class Data {
    private String id;
    private String user_name;
    private String chat_id;

    private String url;
    private String email;
    private String about_me;

    private String publisher_type;
    private String device_token;
    private String device_type;

    public Data(String id, String user_name, String chat_id, String url, String email, String about_me, String publisher_type, String device_token, String device_type) {
        this.id = id;
        this.user_name = user_name;
        this.chat_id = chat_id;
        this.url = url;
        this.email = email;
        this.about_me = about_me;
        this.publisher_type = publisher_type;
        this.device_token = device_token;
        this.device_type = device_type;
    }

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

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getPublisher_type() {
        return publisher_type;
    }

    public void setPublisher_type(String publisher_type) {
        this.publisher_type = publisher_type;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }
}
