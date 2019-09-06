package com.ue.uebook.LoginActivity.Pojo;

import java.util.List;

public class LoginResponse  {
    private   Boolean error;
    private String message;
    private List<UserInfoPojo> response;

    public LoginResponse(Boolean error, String message, List<UserInfoPojo> response) {
        this.error = error;
        this.message = message;
        this.response = response;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserInfoPojo> getResponse() {
        return response;
    }

    public void setResponse(List<UserInfoPojo> response) {
        this.response = response;
    }
}
