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
import com.example.studybuddy.viewModel.MyPaymentsActivity;
import com.example.studybuddy.viewModel.RecyclerViewInterface;
import com.example.studybuddy.objects.Class;
import com.example.studybuddy.viewModel.StudentMyPaymentActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class StudentPaymentAdapter extends RecyclerView.Adapter<StudentPaymentHolder> {
//    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    List<Class> items;
    StudentMyPaymentActivity activity;

    public StudentPaymentAdapter(Context context, List<Class> items, StudentMyPaymentActivity activity) {
        this.context = context;
        this.items = items;
        this.activity = activity;
    }
//    public StudentPaymentAdapter(@NonNull FirestoreRecyclerOptions<Class> options , RecyclerViewInterface recyclerViewInterface) {
//        super(options);
//        this.recyclerViewInterface = recyclerViewInterface;
//    }

    public void onBindViewHolder(@NonNull StudentPaymentHolder holder, int position) {
        holder.teacherName.setText(items.get(position).getTeacherName());
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
        holder.pay_class.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                activity.onApprovePaymentForClassClick(items.get(position).getTeacherName(),items.get(position).getSubject(), items.get(position).getDate() );
            }
        });
    }
    @NonNull
    @Override
    public StudentPaymentHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new StudentPaymentHolder(LayoutInflater.from(context).inflate(R.layout.student_payment_item,parent,false));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

//    @Override
//    protected void onBindViewHolder(@NonNull StudentPaymentHolder holder, int position, @NonNull Class model) {
//        holder.teacherName.setText(model.getTeacherName());
//        holder.subject.setText(model.getSubject());
//        holder.date.setText(model.getDate());
//        holder.cost.setText(model.getCost() + " ₪");
//    }
//
//    @NonNull
//    @Override
//    public StudentPaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_payment_item,
//                parent, false);
//        return new StudentPaymentHolder(v, this.recyclerViewInterface);
//    }


}