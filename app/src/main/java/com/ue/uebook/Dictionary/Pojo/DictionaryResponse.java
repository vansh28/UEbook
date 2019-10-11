package com.ue.uebook.Dictionary.Pojo;

import java.util.List;

public class DictionaryResponse {
    private String error;
    private String  message;
    private List<Defination>response;

    public DictionaryResponse(String error, String message, List<Defination> response) {
        this.error = error;
        this.message = message;
        this.response = response;
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

    public List<Defination> getResponse() {
        return response;
    }

    public void setResponse(List<Defination> response) {
        this.response = response;
    }
}
