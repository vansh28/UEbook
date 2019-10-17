package com.ue.uebook.LoginActivity.Pojo;


public class LoginResponse  {
    private   Boolean error;

    private UserInfoPojo response;
    private String message;

    public LoginResponse(Boolean error, UserInfoPojo response, String message) {
        this.error = error;
        this.response = response;
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public UserInfoPojo getResponse() {
        return response;
    }

    public void setResponse(UserInfoPojo response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
