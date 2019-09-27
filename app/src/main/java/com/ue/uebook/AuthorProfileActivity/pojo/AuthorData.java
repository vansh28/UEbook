package com.ue.uebook.AuthorProfileActivity.pojo;

import java.util.List;

public class AuthorData {

    private Boolean error;
    private AuthorProfileData data;
    private List<AuthorBookList>booklist;
    private String  message;

    public AuthorData(Boolean error, AuthorProfileData data, List<AuthorBookList> booklist, String message) {
        this.error = error;
        this.data = data;
        this.booklist = booklist;
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public AuthorProfileData getData() {
        return data;
    }

    public void setData(AuthorProfileData data) {
        this.data = data;
    }

    public List<AuthorBookList> getBooklist() {
        return booklist;
    }

    public void setBooklist(List<AuthorBookList> booklist) {
        this.booklist = booklist;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
