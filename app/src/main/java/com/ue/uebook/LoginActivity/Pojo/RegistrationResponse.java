package com.ue.uebook.LoginActivity.Pojo;

import java.util.List;

public class RegistrationResponse {
    private String error;
    private String user_id;
    private String email;
    private String phone_no;
    private String full_name;
public List<UserInfoPojo>user_data;
private String  message;

    public RegistrationResponse(String error, String user_id, String email, String phone_no, String full_name, List<UserInfoPojo> user_data, String message) {
        this.error = error;
        this.user_id = user_id;
        this.email = email;
        this.phone_no = phone_no;
        this.full_name = full_name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public List<UserInfoPojo> getUser_data() {
        return user_data;
    }

    public void setUser_data(List<UserInfoPojo> user_data) {
        this.user_data = user_data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
