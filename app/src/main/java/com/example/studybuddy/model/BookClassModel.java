package com.example.studybuddy.model;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.objects.Teacher;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookClassModel {

    Activity activity;
    private String courseValueFromSpinner;
    private String dateValueFromButton;
    private String fromHourValueFromSpinner;
    private String toHourValueFromSpinner;
    private ArrayList<Teacher> filteredTeachers;
    public ArrayList<Teacher> teachersList;

    public BookClassModel(Activity activity) {
        this.activity = activity;
        this.teachersList = new ArrayList<Teacher>();
        this.dateValueFromButton = "choose date";
        this.filteredTeachers = new ArrayList<Teacher>();
    }

    public GoogleSignInClient googleSignInClient() {
        return GoogleSignIn.getClient(this.activity, GoogleSignInOptions.DEFAULT_SIGN_IN);
    }

    public void modelOnQueryTextChange(String str) {

        this.filteredTeachers = new ArrayList<Teacher>();

        for (Teacher teacher : this.teachersList) {
            if (teacher.getName().toLowerCase().contains(str.toLowerCase()))
                filteredTeachers.add(teacher);
        }
    }

    public void beforeSetUpData(){
        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().deletePastDates();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("deletePastDates", "done");
                    modelSetupData();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Fail deletePastDates", t.getMessage());
            }
        });
    }

    public void modelSetupData(){

        Call<ArrayList<Teacher>> call = RetrofitClient.getInstance().getAPI().getAllTeachers();
        call.enqueue(new Callback<ArrayList<Teacher>>() {
            @Override
            public void onResponse(Call<ArrayList<Teacher>> call, Response<ArrayList<Teacher>> response) {
                teachersList = response.body();
                Log.d("getAllTeachers", "done");
            }

            @Override
            public void onFailure(Call<ArrayList<Teacher>> call, Throwable t) {
                Log.d("Fail getAllTeachers", t.getMessage());
            }
        });
    }

    public  ArrayList<Teacher> getTeachersList() {
        return teachersList;
    }

    public String getCourseValueFromSpinner() {
        return courseValueFromSpinner;
    }

    public String getDateValueFromButton() {
        return dateValueFromButton;
    }

    public String getFromHourValueFromSpinner() {
        return fromHourValueFromSpinner;
    }

    public String getToHourValueFromSpinner() {
        return toHourValueFromSpinner;
    }

    public ArrayList<Teacher> getFilteredTeachers() {
        return filteredTeachers;
    }

    public void setCourseValueFromSpinner(String courseValueFromSpinner) {
        this.courseValueFromSpinner = courseValueFromSpinner;
    }

    public void setDateValueFromButton(String dateValueFromButton) {
        this.dateValueFromButton = dateValueFromButton;
    }

    public void setFromHourValueFromSpinner(String fromHourValueFromSpinner) {
        this.fromHourValueFromSpinner = fromHourValueFromSpinner;
    }

    public void setToHourValueFromSpinner(String toHourValueFromSpinner) {
        this.toHourValueFromSpinner = toHourValueFromSpinner;
    }
}

