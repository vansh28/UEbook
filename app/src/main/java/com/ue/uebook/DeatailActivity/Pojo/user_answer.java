package com.ue.uebook.DeatailActivity.Pojo;

import java.io.Serializable;

public class user_answer implements Serializable {
    private String question_id;
    private String books_id;
    private String answer;
    private String question;

    public user_answer(String question_id, String books_id, String answer, String question) {
        this.question_id = question_id;
        this.books_id = books_id;
        this.answer = answer;
        this.question = question;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getBooks_id() {
        return books_id;
    }

    public void setBooks_id(String books_id) {
        this.books_id = books_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
