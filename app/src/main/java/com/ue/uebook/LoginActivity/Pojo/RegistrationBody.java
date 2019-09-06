package com.ue.uebook.LoginActivity.Pojo;

public class RegistrationBody {
    public String user_name;
    public String password;
    public String email;
    public String publisher_type;
    public String gender;

    public RegistrationBody(String user_name, String password, String email, String publisher_type, String gender) {
        this.user_name = user_name;
        this.password = password;
        this.email = email;
        this.publisher_type = publisher_type;
        this.gender = gender;
    }
}
