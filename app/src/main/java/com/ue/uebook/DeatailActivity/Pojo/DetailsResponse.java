package com.ue.uebook.DeatailActivity.Pojo;

import com.ue.uebook.UploadBook.Pojo.Bookdata;

public class DetailsResponse {
    private String error;
    private BookDetails data;

    public DetailsResponse(String error, BookDetails data) {
        this.error = error;
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public BookDetails getData() {
        return data;
    }

    public void setData(BookDetails data) {
        this.data = data;
    }
}
