package com.example.studybuddy.adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.R;


class StudentClassHolder extends RecyclerView.ViewHolder {
    TextView teacherName;
    TextView subject;
    TextView date;
    ImageButton cancel_class, whatsapp_message;

    public StudentClassHolder(View itemView) { // , RecyclerViewInterface recyclerViewInterface
        super(itemView);
        teacherName = itemView.findViewById(R.id.teacherName);
        subject  = itemView.findViewById(R.id.subject);
        date = itemView.findViewById(R.id.date);
        cancel_class = itemView.findViewById(R.id.cancel_class);
        whatsapp_message = itemView.findViewById(R.id.whatsapp_message);
    }
}
