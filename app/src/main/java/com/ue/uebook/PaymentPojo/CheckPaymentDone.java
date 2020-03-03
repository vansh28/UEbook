package com.ue.uebook.PaymentPojo;

public class CheckPaymentDone {
    private Boolean error;
    private UserPaymentHistory user_data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public UserPaymentHistory getUser_data() {
        return user_data;
    }

    public void setUser_data(UserPaymentHistory user_data) {
        this.user_data = user_data;
    }
}
