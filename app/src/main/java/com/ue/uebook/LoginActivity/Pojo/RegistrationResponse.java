package com.ue.uebook.LoginActivity.Pojo;
public class RegistrationResponse {
    private String error;
    private String user_id;
   public UserInfoPojo user_data;
       private String  message;

    public RegistrationResponse(String error, String user_id, UserInfoPojo user_data, String message) {
        this.error = error;
        this.user_id = user_id;
        this.user_data = user_data;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public UserInfoPojo getUser_data() {
        return user_data;
    }

    public void setUser_data(UserInfoPojo user_data) {
        this.user_data = user_data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
