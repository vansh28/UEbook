package com.ue.uebook.UploadBook.Pojo;

public class PendingData {
    private boolean error;
    private Pendingbookdata response;

    public Pendingbookdata getResponse() {
        return response;
    }

    public void setResponse(Pendingbookdata response) {
        this.response = response;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }


}
