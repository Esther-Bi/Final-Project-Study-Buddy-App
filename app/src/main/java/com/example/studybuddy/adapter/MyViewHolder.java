package com.example.studybuddy.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.R;
import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.viewModel.GroupHomeActivity;
import com.google.firebase.auth.FirebaseAuth;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageButton delete_Button, edit_Button, whatsapp_Button, participants_Button;
    TextView subject, day, time;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        delete_Button = itemView.findViewById(R.id.delete_Button);
        edit_Button = itemView.findViewById(R.id.edit_Button);
        whatsapp_Button = itemView.findViewById(R.id.whatsapp_Button);
        participants_Button = itemView.findViewById(R.id.participants_Button);
        subject = itemView.findViewById(R.id.subject);
        day = itemView.findViewById(R.id.day);
        time = itemView.findViewById(R.id.time);
        itemView.findViewById(R.id.delete_Button).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                Log.d("check me!!!!" , "_______");
            }
        });
    }
}
