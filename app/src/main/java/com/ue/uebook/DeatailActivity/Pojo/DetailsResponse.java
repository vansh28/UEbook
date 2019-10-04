package com.ue.uebook.DeatailActivity.Pojo;

import java.util.List;

public class DetailsResponse {
    private String error;
    private BookDetails data;
    private List<ReviewPojo>review;
    private String averaVal;
    private BookmarkPojo bookMark;
    private List<Assignment>assignment;

    public DetailsResponse(String error, BookDetails data, List<ReviewPojo> review, String averaVal, BookmarkPojo bookMark, List<Assignment> assignment) {
        this.error = error;
        this.data = data;
        this.review = review;
        this.averaVal = averaVal;
        this.bookMark = bookMark;
        this.assignment = assignment;
    }

    public List<Assignment> getAssignment() {
        return assignment;
    }

    public void setAssignment(List<Assignment> assignment) {
        this.assignment = assignment;
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

    public List<ReviewPojo> getReview() {
        return review;
    }

    public void setReview(List<ReviewPojo> review) {
        this.review = review;
    }

    public String getAveraVal() {
        return averaVal;
    }

    public void setAveraVal(String averaVal) {
        this.averaVal = averaVal;
    }

    public BookmarkPojo getBookMark() {
        return bookMark;
    }

    public void setBookMark(BookmarkPojo bookMark) {
        this.bookMark = bookMark;
    }
}
