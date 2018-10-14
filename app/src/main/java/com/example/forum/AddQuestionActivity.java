package com.example.forum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AddQuestionActivity extends AppCompatActivity {

    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        text = findViewById(R.id.question_text);
    }

    public void AddQuestion(final View view) {
        view.setEnabled(false);
        if (text.getText().length() < 1){
            text.setError("Invalid Question");
        }else{
            Question question = new Question(
                    text.getText().toString(),
                    FirebaseAuth.getInstance().getCurrentUser().getUid()
            );
            FirebaseDatabase.getInstance().getReference("Questions").push().setValue(question)
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            view.setEnabled(true);
                            Toast.makeText(AddQuestionActivity.this, "Question added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddQuestionActivity.this, "Failed to add question", Toast.LENGTH_SHORT).show();
                            view.setEnabled(true);
                        }
                    });
        }
    }
}
