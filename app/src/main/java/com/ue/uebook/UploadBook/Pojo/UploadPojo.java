package com.ue.uebook.UploadBook.Pojo;

import com.ue.uebook.LoginActivity.Pojo.UserInfoPojo;

public class UploadPojo {

    private   Boolean error;

    private Bookdata data;
    private String message;

    public UploadPojo(Boolean error, Bookdata data, String message) {
        this.error = error;
        this.data = data;
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Bookdata getData() {
        return data;
    }

    public void setData(Bookdata data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
