package com.example.studybuddy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.R;
import com.example.studybuddy.viewModel.HomeActivity;
import com.example.studybuddy.viewModel.MyPaymentsActivity;
import com.example.studybuddy.viewModel.RecyclerViewInterface;
import com.example.studybuddy.objects.Class;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentHolder> {

    Context context;
    List<Class> items;
    MyPaymentsActivity activity;

    public PaymentAdapter(Context context, List<Class> items, MyPaymentsActivity activity) {
        this.context = context;
        this.items = items;
        this.activity = activity;
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHolder holder, int position) {
        holder.studentName.setText(items.get(position).getStudentName());
        holder.subject.setText(items.get(position).getSubject());
        holder.date.setText(items.get(position).getDate());
        holder.cost.setText(items.get(position).getCost() + " ₪");
        holder.payment_confirmation.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                activity.onApprovePaymentForClassClick(items.get(position).getTeacherName(),items.get(position).getSubject(), items.get(position).getDate() );
            }
        });
    }


    @NonNull
    @Override
    public PaymentHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new PaymentHolder(LayoutInflater.from(context).inflate(R.layout.payment_item,parent,false));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}