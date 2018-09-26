package com.example.forum;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class QuestionFragment extends android.support.v4.app.Fragment {

    private FirebaseDatabase db;
    private OnItemClickListener mOnClickListener;

    public QuestionFragment() {
        db = FirebaseDatabase.getInstance();
        mOnClickListener = new OnItemClickListener() {
            @Override
            public void onSubmitClicked(String s, String questionId) {
                Answer ans = new Answer(FirebaseAuth.getInstance().getCurrentUser().getUid(),s);
                db.getReference("Questions").child(questionId).child("Answers").push().setValue(ans)
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Answer recorded", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onViewClicked(String questionId) {
                startActivity(new Intent(getActivity(),ThreadActivity.class).putExtra("questionId",questionId));
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_question, container, false);
        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AddQuestionActivity.class));
            }
        });
        final RecyclerView recyclerView = view.findViewById(R.id.question_recycler);
        db.getReference("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    List<Question> list = new ArrayList<>();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Question question = snap.getValue(Question.class);
                        question.setQuestionId(snap.getKey());
                        list.add(question);
                    }
                    if (list.size()>0){
                        recyclerView.setAdapter(new QuestionRecycler(getActivity(), list, mOnClickListener));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

}
