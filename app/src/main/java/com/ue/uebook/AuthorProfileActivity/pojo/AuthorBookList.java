package com.ue.uebook.AuthorProfileActivity.pojo;

public class AuthorBookList {

    private String id;
    private String book_title;
    private String thubm_image;
    private String author_name;
    private String book_description;
    private String rating;

    public AuthorBookList(String id, String book_title, String thubm_image, String author_name, String book_description, String rating) {
        this.id = id;
        this.book_title = book_title;
        this.thubm_image = thubm_image;
        this.author_name = author_name;
        this.book_description = book_description;
        this.rating = rating;
    }

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
