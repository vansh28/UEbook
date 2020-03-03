package com.ue.uebook.PaymentPojo;

public class PaymentResponse {

    private String  status;
    private Payment_result  result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Payment_result getResult() {
        return result;
    }

    public void setResult(Payment_result result) {
        this.result = result;
    }
}
