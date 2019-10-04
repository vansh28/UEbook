package com.ue.uebook.DeatailActivity.Pojo;

import java.io.Serializable;

public class Assignment implements Serializable {
     private String id;
     private String book_id;
     private String question;
     private String  answer;

    public Assignment(String id, String book_id, String question, String answer) {
        this.id = id;
        this.book_id = book_id;
        this.question = question;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


}