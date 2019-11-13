package com.ue.uebook.ChatSdk.Pojo;

public class UserList {
    private String  chid;
    private String  id;
    private String  channel_id;
    private String  sender;
    private String  receiver;
    private String  type;
    private String  message;
    private String  created;
    private String  modified;
    private String  read_msg;
    private String  is_active;
    private UserDataResponse send_detail;
    private UserDataResponse rec_detail;


    public UserList(String chid, String id, String channel_id, String sender, String receiver, String type, String message, String created, String modified, String read_msg, String is_active, UserDataResponse send_detail, UserDataResponse rec_detail) {
        this.chid = chid;
        this.id = id;
        this.channel_id = channel_id;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.message = message;
        this.created = created;
        this.modified = modified;
        this.read_msg = read_msg;
        this.is_active = is_active;
        this.send_detail = send_detail;
        this.rec_detail = rec_detail;
    }

    public String getChid() {
        return chid;
    }

    public void setChid(String chid) {
        this.chid = chid;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getRead_msg() {
        return read_msg;
    }

    public void setRead_msg(String read_msg) {
        this.read_msg = read_msg;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public UserDataResponse getSend_detail() {
        return send_detail;
    }

    public void setSend_detail(UserDataResponse send_detail) {
        this.send_detail = send_detail;
    }

    public UserDataResponse getRec_detail() {
        return rec_detail;
    }

    public void setRec_detail(UserDataResponse rec_detail) {
        this.rec_detail = rec_detail;
    }
}
