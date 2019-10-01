package com.ue.uebook.DeatailActivity.Pojo;

public class BookmarkPojo {
    private String bookmarkId;
    private String bookmarkStatus;

    public BookmarkPojo(String bookmarkId, String bookmarkStatus) {
        this.bookmarkId = bookmarkId;
        this.bookmarkStatus = bookmarkStatus;
    }

    public String getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(String bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public String getBookmarkStatus() {
        return bookmarkStatus;
    }
    public void setBookmarkStatus(String bookmarkStatus) {
        this.bookmarkStatus = bookmarkStatus;
    }
}
