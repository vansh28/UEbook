package com.ue.uebook.PendingBook.Pojo;

import java.util.List;

public class PendingBookdata {
    private boolean error;
    private List<BookResponse>data;

    public PendingBookdata(boolean error, List<BookResponse> data) {
        this.error = error;
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<BookResponse> getData() {
        return data;
    }

    public void setData(List<BookResponse> data) {
        this.data = data;
    }
}
