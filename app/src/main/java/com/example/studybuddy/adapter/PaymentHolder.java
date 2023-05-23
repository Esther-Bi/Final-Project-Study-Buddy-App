package com.example.studybuddy.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.R;
import com.example.studybuddy.viewModel.RecyclerViewInterface;

public class PaymentHolder extends RecyclerView.ViewHolder {
    TextView studentName;
    TextView subject;
    TextView date;
    TextView cost;
    Button payment_confirmation;

    public PaymentHolder(View itemView) {
        super(itemView);
        studentName = itemView.findViewById(R.id.studentName);
        subject  = itemView.findViewById(R.id.subject);
        date = itemView.findViewById(R.id.date);
        cost = itemView.findViewById(R.id.cost);
        payment_confirmation = itemView.findViewById(R.id.payment_confirmation);
    }
}
