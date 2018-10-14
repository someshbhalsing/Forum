package com.example.forum;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class QuestionRecycler extends RecyclerView.Adapter<QuestionRecycler.QuestionHolder> {

    private Context context;
    private List<Question> list;
    private OnItemClickListener mOnClickListener;
    private static final FirebaseDatabase db = FirebaseDatabase.getInstance();

    public QuestionRecycler(Context context, List<Question> list, OnItemClickListener mOnClickListener) {
        this.context = context;
        this.list = list;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_view_holder,viewGroup,false);
        return new QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionHolder questionHolder, int i) {
        final Question question = list.get(i);
        questionHolder.questionId = question.getQuestionId();
        questionHolder.Quesion.setText(question.getmQuestion());
        db.getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.getKey().equals(question.getUserId())) {
                        User mUser = new User();
                        mUser.setName((String) snapshot.getValue());
                        mUser.setUid(snapshot.getKey());
                        questionHolder.mUserName = mUser.name;
                        questionHolder.askedBy.append(mUser.name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {

        TextView askedBy;
        TextView Quesion;
        EditText Answer;
        Button submit;
        String questionId;
        String mUserName;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            askedBy = itemView.findViewById(R.id.question_asked_by);
            Quesion = itemView.findViewById(R.id.question_question);
            Answer = itemView.findViewById(R.id.question_answer_text);
            submit = itemView.findViewById(R.id.question_ans_submit_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickListener.onViewClicked(questionId);
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Answer.getText().length() > 0){
                        mOnClickListener.onSubmitClicked(Answer.getText().toString(),questionId);
                        Answer.getText().clear();
                    }
                }
            });

        }
    }
}