package com.ue.uebook.ChatSdk.Pojo;

import java.util.List;

public class ChatResponse {
    private boolean error;
    private List<Chathistory>chat_list;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Chathistory> getChat_list() {
        return chat_list;
    }

    public void setChat_list(List<Chathistory> chat_list) {
        this.chat_list = chat_list;
    }
}
