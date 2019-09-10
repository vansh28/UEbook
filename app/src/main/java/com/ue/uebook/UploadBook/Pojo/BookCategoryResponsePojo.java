package com.ue.uebook.UploadBook.Pojo;

import java.util.List;

public class BookCategoryResponsePojo {

    private String error;
    private List<BookCategoryPojo>response;

    public BookCategoryResponsePojo(String error, List<BookCategoryPojo> response) {
        this.error = error;
        this.response = response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<BookCategoryPojo> getResponse() {
        return response;
    }

    public void setResponse(List<BookCategoryPojo> response) {
        this.response = response;
    }
}
