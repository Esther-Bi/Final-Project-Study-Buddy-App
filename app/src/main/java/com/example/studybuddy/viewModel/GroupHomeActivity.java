package com.example.studybuddy.viewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.studybuddy.R;
import com.example.studybuddy.adapter.GroupAdapter;
import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.objects.Group;
import com.example.studybuddy.objects.Teacher;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupHomeActivity extends AppCompatActivity{


    public GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_home);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        Call<ArrayList<Group>> call = RetrofitClient.getInstance().getAPI().getMyGroups(FirebaseAuth.getInstance().getCurrentUser().getUid());
        call.enqueue(new Callback<ArrayList<Group>>() {
            @Override
            public void onResponse(Call<ArrayList<Group>> call, Response<ArrayList<Group>> response) {
                ArrayList<Group> myGroups = response.body();
                Toast.makeText(GroupHomeActivity.this, myGroups.get(0).getSubject()+ "!!!", Toast.LENGTH_SHORT).show();
                recyclerView.setLayoutManager(new LinearLayoutManager(GroupHomeActivity.this));
                adapter = new GroupAdapter(getApplicationContext(),myGroups, GroupHomeActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Group>> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });
    }

    public void refresh(){
        startActivity(new Intent(GroupHomeActivity.this, GroupHomeActivity.class));

    }
//    public void popUpDelete(){
//        dialogBuilder = new AlertDialog.Builder(this);
//        final View popupView = getLayoutInflater().inflate(R.layout.delete_question_popup, null);
//
//        TextView question = popupView.findViewById(R.id.question);
//        Button yes_button = popupView.findViewById(R.id.yes_button);
//        Button no_button = popupView.findViewById(R.id.no_button);
//        question.setText("Are you sure you paid for\n" + subject + " class\nwith " + name + "\nat " + date + "?");
//
//        dialogBuilder.setView(popupView);
//        dialog = dialogBuilder.create();
//        dialog.show();
//
//        yes_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                Log.d(TAG, "class paid");
//                model.approve_click_yes(name, date, subject);
//                rateTeacherPopup(name, subject, date);
//            }
//        });
//
//    }

}