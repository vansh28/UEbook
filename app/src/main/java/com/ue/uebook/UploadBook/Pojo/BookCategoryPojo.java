package com.ue.uebook.UploadBook.Pojo;

public class BookCategoryPojo {
    private String id;
    private String category_name;
    private String slug_url;
    private String status;
    private String thum_image;
    private String created_at;
    private String updated_at;

    public String getBook_count() {
        return book_count;
    }

    public void setBook_count(String book_count) {
        this.book_count = book_count;
    }

    private String book_count;

    public BookCategoryPojo(String id, String category_name, String slug_url, String status, String thum_image, String created_at, String updated_at) {
        this.id = id;
        this.category_name = category_name;
        this.slug_url = slug_url;
        this.status = status;
        this.thum_image = thum_image;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSlug_url() {
        return slug_url;
    }

    public void setSlug_url(String slug_url) {
        this.slug_url = slug_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThum_image() {
        return thum_image;
    }

    public void setThum_image(String thum_image) {
        this.thum_image = thum_image;
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
