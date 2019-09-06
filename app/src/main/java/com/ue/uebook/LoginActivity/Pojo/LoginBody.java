package com.ue.uebook.LoginActivity.Pojo;

public class LoginBody {
    private String  user_name;
    private String password;

    public LoginBody(String email, String password) {
        this.user_name = email;
        this.password = password;
    }
}
