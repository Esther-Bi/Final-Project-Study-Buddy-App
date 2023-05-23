package com.example.studybuddy.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.R;
import com.example.studybuddy.viewModel.RecyclerViewInterface;

public class ClassHolder extends RecyclerView.ViewHolder {
    TextView studentName;
    TextView subject;
    TextView date;
    Button cancel_class, whatsapp_message;


    public ClassHolder(View itemView) {
        super(itemView);
        studentName = itemView.findViewById(R.id.teacherName);
        subject  = itemView.findViewById(R.id.subject);
        date = itemView.findViewById(R.id.date);
        cancel_class = itemView.findViewById(R.id.cancel_class);
        whatsapp_message = itemView.findViewById(R.id.whatsapp_message);
//        itemView.findViewById(R.id.cancel_class).setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("RestrictedApi")
//            @Override
//            public void onClick(View view) {
//                if(recyclerViewInterface != null){
//                    int pos = getAdapterPosition();
//                    if(pos != RecyclerView.NO_POSITION){
//                        recyclerViewInterface.onCancelClassClick(studentName.getText().toString(),subject.getText().toString(),date.getText().toString());
//                    }
//                }
//            }
//        });
//        itemView.findViewById(R.id.whatsapp_message).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(recyclerViewInterface != null){
//                    int pos = getAdapterPosition();
//                    if(pos != RecyclerView.NO_POSITION){
//                        recyclerViewInterface.onWhatsAppMessageClick(studentName.getText().toString(),subject.getText().toString(),date.getText().toString());
//                    }
//                }
//            }
//        });
    }
}
