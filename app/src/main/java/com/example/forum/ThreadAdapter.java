package com.example.forum;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ThreadHolder> {

    private List<Answer> list;
    private Context mContext;
    private FirebaseDatabase db;

    public ThreadAdapter(List<Answer> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
        db = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public ThreadHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.thread_holder,viewGroup,false);
        return new ThreadHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ThreadHolder threadHolder, int i) {
        final Answer answer = list.get(i);
        threadHolder.answer.setText(answer.getmAnswer());
        db.getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.getKey().equals(answer.getUserId()))
                        threadHolder.name.append((CharSequence) snapshot.getValue());
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

    public class ThreadHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView answer;

        public ThreadHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.thread_holder_posted_by);
            answer = itemView.findViewById(R.id.thread_holder_answer);
        }
    }
}
