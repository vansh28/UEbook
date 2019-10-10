package com.ue.uebook.AuthorProfileActivity.pojo;

public class AuthorProfileData {
    private String id;
    private String user_name;
    private String url;
    private String email;
    private String about_me;
    private String chat_id;
    private String publisher_type;

    public AuthorProfileData(String id, String user_name, String url, String email, String about_me, String chat_id, String publisher_type) {
        this.id = id;
        this.user_name = user_name;
        this.url = url;
        this.email = email;
        this.about_me = about_me;
        this.chat_id = chat_id;
        this.publisher_type = publisher_type;
    }

    public String getPublisher_type() {
        return publisher_type;
    }

    public void setPublisher_type(String publisher_type) {
        this.publisher_type = publisher_type;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
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
}
