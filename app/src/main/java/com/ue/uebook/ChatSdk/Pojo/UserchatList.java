package com.ue.uebook.ChatSdk.Pojo;

public class UserchatList {
    private String  channel_id;
    private String  id;
    private String  email;
    private String  full_name;
    private String  user_name;
    private String  user_pic;
    private String  publisher_type;
    private String  created;
    private String  type;
    private String  message;

    public UserchatList(String channel_id, String id, String email, String full_name, String user_name, String user_pic, String publisher_type, String created, String type, String message) {
        this.channel_id = channel_id;
        this.id = id;
        this.email = email;
        this.full_name = full_name;
        this.user_name = user_name;
        this.user_pic = user_pic;
        this.publisher_type = publisher_type;
        this.created = created;
        this.type = type;
        this.message = message;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getPublisher_type() {
        return publisher_type;
    }

    public void setPublisher_type(String publisher_type) {
        this.publisher_type = publisher_type;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
