package com.ue.uebook.DeatailActivity.Pojo;

public class ReviewPojo {
    private String ReviewId;
    private String comment;
    private String rating;
    private String created_at;
    private String user_name;
    private String url;

    public ReviewPojo(String reviewId, String comment, String rating, String created_at, String user_name, String url) {
        ReviewId = reviewId;
        this.comment = comment;
        this.rating = rating;
        this.created_at = created_at;
        this.user_name = user_name;
        this.url = url;
    }

    public String getReviewId() {
        return ReviewId;
    }

    public void setReviewId(String reviewId) {
        ReviewId = reviewId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
