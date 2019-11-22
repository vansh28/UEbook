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
    private Mess_count mess_count;

    public String getChid() {
        return chid;
    }

    public String getId() {
        return id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getCreated() {
        return created;
    }

    public String getModified() {
        return modified;
    }

    public String getRead_msg() {
        return read_msg;
    }

    public String getIs_active() {
        return is_active;
    }

    public UserDataResponse getSend_detail() {
        return send_detail;
    }

    public UserDataResponse getRec_detail() {
        return rec_detail;
    }

    public Mess_count getMess_count() {
        return mess_count;
    }
}
