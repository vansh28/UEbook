package com.ue.uebook.HomeActivity.HomeFragment.Pojo;

import java.util.List;

public class NotepadResponse {
    private String error;
    private String message;
    private List<NotepadUserList>data;

    public NotepadResponse(String error, String message, List<NotepadUserList> data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NotepadUserList> getData() {
        return data;
    }

    public void setData(List<NotepadUserList> data) {
        this.data = data;
    }
}
