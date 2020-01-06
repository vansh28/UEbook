package com.ue.uebook.DeatailActivity.Pojo;

public class BookDetails {
    private String id;
    private String user_id;
    private String category_id;
    private String book_title;
    private String book_slug;
    private String thubm_image;
    private String book_description;
    private String author_name;
    private String book_image;
    private String video_url;
    private String audio_url;
    private String pdf_url;
    private String bookMark;
    private String mostView;
    private String status;
    private String created_at;
    private String updated_at;
    private String user_name;

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    private String profile_pic;

    public BookDetails(String id, String user_id, String category_id, String book_title, String book_slug, String thubm_image, String book_description, String author_name, String book_image, String video_url, String audio_url, String pdf_url, String bookMark, String mostView, String status, String created_at, String updated_at, String user_name) {
        this.id = id;
        this.user_id = user_id;
        this.category_id = category_id;
        this.book_title = book_title;
        this.book_slug = book_slug;
        this.thubm_image = thubm_image;
        this.book_description = book_description;
        this.author_name = author_name;
        this.book_image = book_image;
        this.video_url = video_url;
        this.audio_url = audio_url;
        this.pdf_url = pdf_url;
        this.bookMark = bookMark;
        this.mostView = mostView;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_slug() {
        return book_slug;
    }

    public void setBook_slug(String book_slug) {
        this.book_slug = book_slug;
    }

    public String getThubm_image() {
        return thubm_image;
    }

    public void setThubm_image(String thubm_image) {
        this.thubm_image = thubm_image;
    }

    public String getBook_description() {
        return book_description;
    }

    public void setBook_description(String book_description) {
        this.book_description = book_description;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getBook_image() {
        return book_image;
    }

    public void setBook_image(String book_image) {
        this.book_image = book_image;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public String getBookMark() {
        return bookMark;
    }

    public void setBookMark(String bookMark) {
        this.bookMark = bookMark;
    }

    public String getMostView() {
        return mostView;
    }

    public void setMostView(String mostView) {
        this.mostView = mostView;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
