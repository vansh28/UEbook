package com.ue.uebook.HomeActivity.HomeFragment.Pojo;

public class NotepadUserList {
    private String id;
    private String description;
    private String title;
    private String created_at;

    public NotepadUserList(String id, String description, String title, String created_at) {
        this.id = id;
        this.description = description;
        this.title = title;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
