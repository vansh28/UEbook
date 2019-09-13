package com.ue.uebook.HomeActivity.HomeFragment.Pojo;

import java.util.List;

public class UserBookmarkResponse {
    private String error;
    private String  message;
    private List<BookmarkBookList>data;

    public UserBookmarkResponse(String error, String message, List<BookmarkBookList> data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BookmarkBookList> getData() {
        return data;
    }

    public void setData(List<BookmarkBookList> data) {
        this.data = data;
    }
}
