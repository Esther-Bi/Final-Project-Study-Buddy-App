package com.example.studybuddy.adapter;
//import android.support.annotation.NonNull;
//import android.support.v7.androidwidget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.objects.Class;
import com.example.studybuddy.R;
import com.example.studybuddy.viewModel.HomeActivity;
import com.example.studybuddy.viewModel.RecyclerViewInterface;
import com.example.studybuddy.viewModel.StudentHomeActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassHolder> {
//    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    List<Class> items;
    HomeActivity activity;

//    public ClassAdapter(@NonNull FirestoreRecyclerOptions<Class> options , RecyclerViewInterface recyclerViewInterface) {
//        super(options);
//        this.recyclerViewInterface = recyclerViewInterface;
//    }
public ClassAdapter(Context context, List<Class> items, HomeActivity activity) {
    this.context = context;
    this.items = items;
    this.activity = activity;
}

    @NonNull
    @Override
    public ClassHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new ClassHolder(LayoutInflater.from(context).inflate(R.layout.class_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClassHolder holder, int position) {
        holder.subject.setText(items.get(position).getSubject());
        holder.date.setText(items.get(position).getDate());
        holder.studentName.setText(items.get(position).getStudentName());
        holder.whatsapp_message.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                activity.onWhatsAppMessageClick(items.get(position).getStudentName(),items.get(position).getSubject(), items.get(position).getDate() );
            }
        });
        holder.cancel_class.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                activity.onCancelClassClick(items.get(position).getStudentName(),items.get(position).getSubject(), items.get(position).getDate() );
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

//    @NonNull
//    @Override
//    public ClassHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item,
//                parent, false);
//        return new ClassHolder(v, this.recyclerViewInterface);
//    }


}