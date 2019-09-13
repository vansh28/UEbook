package com.ue.uebook.HomeActivity.HomeFragment.Pojo;

public class BookmarkBookList {
    private String id;
    private String book_title;
    private String thubm_image;
    private String author_name;
    private String book_description;
    private String user_id;
    private String bookmark_id;

    public BookmarkBookList(String id, String book_title, String thubm_image, String author_name, String book_description, String user_id, String bookmark_id, String bm_status) {
        this.id = id;
        this.book_title = book_title;
        this.thubm_image = thubm_image;
        this.author_name = author_name;
        this.book_description = book_description;
        this.user_id = user_id;
        this.bookmark_id = bookmark_id;
        this.bm_status = bm_status;
    }

    private String bm_status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getThubm_image() {
        return thubm_image;
    }

    public void setThubm_image(String thubm_image) {
        this.thubm_image = thubm_image;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getBook_description() {
        return book_description;
    }

    public void setBook_description(String book_description) {
        this.book_description = book_description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBookmark_id() {
        return bookmark_id;
    }

    public void setBookmark_id(String bookmark_id) {
        this.bookmark_id = bookmark_id;
    }

    public String getBm_status() {
        return bm_status;
    }

    public void setBm_status(String bm_status) {
        this.bm_status = bm_status;
    }
}
