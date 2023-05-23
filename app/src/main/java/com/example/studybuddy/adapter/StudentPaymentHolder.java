package com.example.studybuddy.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.R;
import com.example.studybuddy.viewModel.RecyclerViewInterface;

public class StudentPaymentHolder extends RecyclerView.ViewHolder {
    TextView teacherName;
    TextView subject;
    TextView date;
    TextView cost;
    Button pay_class, payment_confirmation;

    public StudentPaymentHolder(View itemView){
        super(itemView);
        teacherName = itemView.findViewById(R.id.teacherName);
        subject  = itemView.findViewById(R.id.subject);
        date = itemView.findViewById(R.id.date);
        cost = itemView.findViewById(R.id.cost);
        payment_confirmation = itemView.findViewById(R.id.payment_confirmation);
        pay_class = itemView.findViewById(R.id.pay_class);
//        itemView.findViewById(R.id.payment_confirmation).setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("RestrictedApi")
//            @Override
//            public void onClick(View view) {
//                if(recyclerViewInterface != null){
//                    int pos = getAdapterPosition();
//                    if(pos != RecyclerView.NO_POSITION){
//                        recyclerViewInterface.onApprovePaymentForClassClick(teacherName.getText().toString(),subject.getText().toString(),date.getText().toString());
//                    }
//                }
//            }
//        });
//        itemView.findViewById(R.id.pay_class).setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("RestrictedApi")
//            @Override
//            public void onClick(View view) {
//                if(recyclerViewInterface != null){
//                    int pos = getAdapterPosition();
//                    if(pos != RecyclerView.NO_POSITION){
//                        recyclerViewInterface.onPayForClassClick(teacherName.getText().toString(),subject.getText().toString(),date.getText().toString());
//                    }
//                }
//            }
//        });
    }
}