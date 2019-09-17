package com.ue.uebook.DeatailActivity.Pojo;

import java.util.List;

public class DetailsResponse {
    private String error;
    private BookDetails data;
    private List<ReviewPojo>review;
    private String averaVal;

    public DetailsResponse(String error, BookDetails data, List<ReviewPojo> review, String averaVal) {
        this.error = error;
        this.data = data;
        this.review = review;
        this.averaVal = averaVal;
    }

    public String getAveraVal() {
        return averaVal;
    }

    public void setAveraVal(String averaVal) {
        this.averaVal = averaVal;
    }


    public List<ReviewPojo> getReview() {
        return review;
    }

    public void setReview(List<ReviewPojo> review) {
        this.review = review;
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
