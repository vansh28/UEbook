package com.ue.uebook.ChatSdk.Pojo;

public class callResponse {
    public  String  id;
    public  String  channel_id;
    public  String  sender;
    public  String  receiver;
    public  String  type;
    public  String  created;
    public CallSender sender_info;
    private CallReceiver receiver_info;

    public CallSender getSender_info() {
        return sender_info;
    }

    public void setSender_info(CallSender sender_info) {
        this.sender_info = sender_info;
    }

    public CallReceiver getReceiver_info() {
        return receiver_info;
    }

    public void setReceiver_info(CallReceiver receiver_info) {
        this.receiver_info = receiver_info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
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
}
