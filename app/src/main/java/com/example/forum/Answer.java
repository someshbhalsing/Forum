package com.example.forum;

public class Answer {

    private String userId;
    private String mAnswer;

    public Answer() {
    }

    public Answer(String userId, String mAnswer) {
        this.userId = userId;
        this.mAnswer = mAnswer;
    }

    public String getUserId() {
        return userId;
    }

    public String getmAnswer() {
        return mAnswer;
    }
}
