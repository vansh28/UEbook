package com.ue.uebook.ChatSdk.Pojo;

public class Chathistory {
    private String id;
    private String sender;
    private String receiver;
    private String channelId;
    private String type;
    private String created;
    private String message;
    private String chat_status_comm_msg;
    private String chat_status_id;
    private Integer favorite;

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    public String getChat_status_comm_msg() {
        return chat_status_comm_msg;
    }

    public void setChat_status_comm_msg(String chat_status_comm_msg) {
        this.chat_status_comm_msg = chat_status_comm_msg;
    }

    public String getChat_status_id() {
        return chat_status_id;
    }

    public void setChat_status_id(String chat_status_id) {
        this.chat_status_id = chat_status_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
