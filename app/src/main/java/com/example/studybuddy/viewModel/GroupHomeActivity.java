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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupHomeActivity extends AppCompatActivity{


    public GroupAdapter adapter;
    RecyclerView recyclerView;// = findViewById(R.id.recyclerview);

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_home);

        recyclerView = findViewById(R.id.recyclerview);
        this.refresh();

    }

    public void refresh(){
        Call<ArrayList<Group>> call = RetrofitClient.getInstance().getAPI().getMyGroups(FirebaseAuth.getInstance().getCurrentUser().getUid());
        call.enqueue(new Callback<ArrayList<Group>>() {
            @Override
            public void onResponse(Call<ArrayList<Group>> call, Response<ArrayList<Group>> response) {
                ArrayList<Group> myGroups = response.body();
                if (myGroups == null){
                    myGroups = new ArrayList<Group>();
                }
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

    public void popUpDelete(String groupId){

        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.delete_question_popup, null);

        TextView question = popupView.findViewById(R.id.question);
        Button yes_button = popupView.findViewById(R.id.yes_button);
        Button no_button = popupView.findViewById(R.id.no_button);
        question.setText("Are you sure you want to leave this group?'\n");

        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Log.d("want to quit", "delete group");
                Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().deleteFromGroup(FirebaseAuth.getInstance().getCurrentUser().getUid(), groupId);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Log.d("done", "done");
                            refresh();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("Fail", t.getMessage());
                    }
                });
            }
        });
        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("regret quit", "dont want to quit this group");
                dialog.dismiss();
            }
        });


    }

}