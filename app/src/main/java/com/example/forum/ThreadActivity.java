package com.example.forum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ThreadActivity extends AppCompatActivity {

    private TextView name;
    private TextView question;
    private EditText comment;
    private RecyclerView recyclerView;
    private static final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static String questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        name = findViewById(R.id.thread_asked_by);
        question = findViewById(R.id.thread_question);
        comment = findViewById(R.id.thread_answer);
        recyclerView = findViewById(R.id.thread_recycler);
        questionId = getIntent().getStringExtra("questionId");
        DatabaseReference ref = db.getReference("Questions").child(questionId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Question question1 = dataSnapshot.getValue(Question.class);
                question.setText(question1.getmQuestion());
                db.getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if (snapshot.getKey().equals(question1.getUserId()))
                                name.append((CharSequence) snapshot.getValue());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ref.child("Answers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Answer> answers = new ArrayList<>();
                if (dataSnapshot.getChildrenCount() > 0){
                    for (DataSnapshot snap : dataSnapshot.getChildren()){
                        Answer answer = snap.getValue(Answer.class);
                        answers.add(answer);
                    }
                    recyclerView.setAdapter(new ThreadAdapter(answers,ThreadActivity.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void postComment(View view) {
        if (comment.getText().length() > 0){
            Answer ans = new Answer(FirebaseAuth.getInstance().getCurrentUser().getUid(),comment.getText().toString());
            comment.getText().clear();
            db.getReference("Questions").child(questionId).child("Answers").push().setValue(ans)
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ThreadActivity.this, "Answer recorded", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}