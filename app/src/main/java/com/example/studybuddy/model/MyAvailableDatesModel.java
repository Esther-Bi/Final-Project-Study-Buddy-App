package com.example.studybuddy.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.viewModel.MyAvailableDatesActivity;
import com.example.studybuddy.objects.Teacher;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAvailableDatesModel {

    private MyAvailableDatesActivity activity;
    private FirebaseFirestore db;
    private CollectionReference teachersRef;
    private String userID;
    private ArrayList<String> datesList = new ArrayList<>();

    public MyAvailableDatesModel(MyAvailableDatesActivity activity, String id) {
        this.activity = activity;
        this.db = FirebaseFirestore.getInstance();
        this.teachersRef = this.db.collection("teachers");
        this.userID = id;

    }

    public GoogleSignInClient googleSignInClient() {
        return GoogleSignIn.getClient(this.activity, GoogleSignInOptions.DEFAULT_SIGN_IN);
    }

    public CollectionReference getTeachersRef() {
        return teachersRef;
    }

    public void modelSetData() {
        Call<ArrayList<String>> call = RetrofitClient.getInstance().getAPI().getTeacherDates(userID);
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                datesList = response.body();
                if (datesList == null){
                    datesList = new ArrayList<>();
                }
                activity.setList(datesList);
                activity.setUpOnclickListener();
            }
            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Log.d("failed dates", t.getMessage());
            }
        });
    }

    public void upDateTime(String time, String date) {
        String date_and_time = date + " - " + time;
        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().addDateToTeacher(userID, date_and_time);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("done", "done");
                    Toast.makeText(activity, date_and_time + " have been added successfully", Toast.LENGTH_SHORT).show();
                    activity.setData();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Failed to add date", t.getMessage());
                Toast.makeText(activity, "error in adding new available date", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeDate(String date) {
        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().deleteDateFromTeacher(userID, date);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("done", "done");
                    Toast.makeText(activity, date + " have been deleted successfully", Toast.LENGTH_SHORT).show();
                    activity.setData();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Failed to delete date", t.getMessage());
                Toast.makeText(activity, "error in deleting " + date, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
