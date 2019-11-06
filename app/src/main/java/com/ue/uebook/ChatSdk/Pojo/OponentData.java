package com.ue.uebook.ChatSdk.Pojo;

import java.io.Serializable;

public class OponentData implements Serializable {
    public String  userId;
    public String  name;
    public String  email;
    public String  phone;
    public String  channelId;
    public String  avatar;
    public String  url;
    public String  publisher_type;
    public String  device_token;

    public OponentData(String userId, String name, String email, String phone, String channelId, String avatar, String url, String publisher_type, String device_token) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.channelId = channelId;
        this.avatar = avatar;
        this.url = url;
        this.publisher_type = publisher_type;
        this.device_token = device_token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
