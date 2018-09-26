package com.example.forum;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class Question {

    private String mQuestion;
    private String userId;
    private String questionId;

    public Question() {
    }

    public Question(String mQuestion, String userId) {
        this.mQuestion = mQuestion;
        this.userId = userId;
    }

    public String getmQuestion() {
        return mQuestion;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
