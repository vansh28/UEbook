package com.ue.uebook.LoginActivity.Pojo;

public class RegistrationBody {

    private String user_name;
    private String password;
    private String email;
    private String publisher_type;
    private String gender;

    public RegistrationBody(String user_name, String password, String email, String publisher_type, String gender) {
        this.user_name = user_name;
        this.password = password;
        this.email = email;
        this.publisher_type = publisher_type;
        this.gender = gender;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPublisher_type() {
        return publisher_type;
    }

    public void setPublisher_type(String publisher_type) {
        this.publisher_type = publisher_type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
