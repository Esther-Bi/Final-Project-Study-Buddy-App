package com.example.studybuddy.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.R;

public class PaymentHolder extends RecyclerView.ViewHolder {
    TextView studentName;
    TextView subject;
    TextView date;
    TextView cost;
    Button payment_confirmation;
    ImageButton whatsapp_Button;

    public PaymentHolder(View itemView) {
        super(itemView);
        studentName = itemView.findViewById(R.id.studentName);
        subject  = itemView.findViewById(R.id.subject);
        date = itemView.findViewById(R.id.date);
        cost = itemView.findViewById(R.id.cost);
        payment_confirmation = itemView.findViewById(R.id.payment_confirmation);
        whatsapp_Button = itemView.findViewById(R.id.whatsapp_Button);
    }
}
