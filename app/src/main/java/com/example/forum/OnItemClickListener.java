package com.example.forum;

interface OnItemClickListener {
    
    void onSubmitClicked(String s, String questionId);

    void onViewClicked(String questionId);
}
