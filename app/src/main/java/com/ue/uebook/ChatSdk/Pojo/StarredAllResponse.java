package com.ue.uebook.ChatSdk.Pojo;

public class StarredAllResponse {
    public  String id;
    public  String created_at;
    public Starred_chat_info chat_info;
    public Starred_friend_info friend_info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Starred_chat_info getChat_info() {
        return chat_info;
    }

    public void setChat_info(Starred_chat_info chat_info) {
        this.chat_info = chat_info;
    }

    public Starred_friend_info getFriend_info() {
        return friend_info;
    }

    public void setFriend_info(Starred_friend_info friend_info) {
        this.friend_info = friend_info;
    }
}
