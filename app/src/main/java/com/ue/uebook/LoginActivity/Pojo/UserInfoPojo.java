package com.ue.uebook.LoginActivity.Pojo;

public class UserInfoPojo {
    private String id;
    private String register_id;
    private String full_name;
    private String user_name;
    private String email;
    private String gender;
    private String phone_no;
    private String about_me;
    private String country;
    private String password;
    private String confirmation_key;
    private Integer date_added;
    private String date_edited;
    private String status;
    private String message_status;
    private String publisher_type;
    private String device_token;
    private String device_type;
    private String address;
    private String thumb_image;
    private String global_posting;

    public UserInfoPojo(String id, String register_id, String full_name, String user_name, String email, String gender, String phone_no, String about_me, String country, String password, String confirmation_key, Integer date_added, String date_edited, String status, String message_status, String publisher_type, String device_token, String device_type, String address, String thumb_image, String global_posting) {
        this.id = id;
        this.register_id = register_id;
        this.full_name = full_name;
        this.user_name = user_name;
        this.email = email;
        this.gender = gender;
        this.phone_no = phone_no;
        this.about_me = about_me;
        this.country = country;
        this.password = password;
        this.confirmation_key = confirmation_key;
        this.date_added = date_added;
        this.date_edited = date_edited;
        this.status = status;
        this.message_status = message_status;
        this.publisher_type = publisher_type;
        this.device_token = device_token;
        this.device_type = device_type;
        this.address = address;
        this.thumb_image = thumb_image;
        this.global_posting = global_posting;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegister_id() {
        return register_id;
    }

    public void setRegister_id(String register_id) {
        this.register_id = register_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmation_key() {
        return confirmation_key;
    }

    public void setConfirmation_key(String confirmation_key) {
        this.confirmation_key = confirmation_key;
    }

    public Integer getDate_added() {
        return date_added;
    }

    public void setDate_added(Integer date_added) {
        this.date_added = date_added;
    }

    public String getDate_edited() {
        return date_edited;
    }

    public void setDate_edited(String date_edited) {
        this.date_edited = date_edited;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage_status() {
        return message_status;
    }

    public void setMessage_status(String message_status) {
        this.message_status = message_status;
    }

    public String getPublisher_type() {
        return publisher_type;
    }

    public void setPublisher_type(String publisher_type) {
        this.publisher_type = publisher_type;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getGlobal_posting() {
        return global_posting;
    }

    public void setGlobal_posting(String global_posting) {
        this.global_posting = global_posting;
    }
}

