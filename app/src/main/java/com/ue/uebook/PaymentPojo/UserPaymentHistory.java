package com.ue.uebook.PaymentPojo;
public class UserPaymentHistory {
    private String is_payment_done;
    private String  payment_status;

    public String getIs_payment_done() {
        return is_payment_done;
    }

    public void setIs_payment_done(String is_payment_done) {
        this.is_payment_done = is_payment_done;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }
}