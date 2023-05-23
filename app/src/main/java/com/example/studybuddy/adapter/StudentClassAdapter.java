package com.example.studybuddy.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.R;
import com.example.studybuddy.objects.Class;
import com.example.studybuddy.viewModel.StudentHomeActivity;

import java.util.List;

public class StudentClassAdapter extends RecyclerView.Adapter<StudentClassHolder>  {

    Context context;
    List<Class> items;
    StudentHomeActivity activity;

    public StudentClassAdapter(Context context, List<Class> items, StudentHomeActivity activity) {
        this.context = context;
        this.items = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public StudentClassHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new StudentClassHolder(LayoutInflater.from(context).inflate(R.layout.class_item,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull StudentClassHolder holder, int position) {
        holder.subject.setText(items.get(position).getSubject());
        holder.date.setText(items.get(position).getDate());
        holder.teacherName.setText(items.get(position).getTeacherName());
        holder.whatsapp_message.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                activity.onWhatsAppMessageClick(items.get(position).getTeacherName(),items.get(position).getSubject(), items.get(position).getDate() );
            }
        });
        holder.cancel_class.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                activity.onCancelClassClick(items.get(position).getTeacherName(),items.get(position).getSubject(), items.get(position).getDate() );
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}