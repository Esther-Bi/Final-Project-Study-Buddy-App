package com.example.studybuddy.model;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.objects.Student;
import com.example.studybuddy.viewModel.MyCoursesActivity;
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

public class CoursesModel {
    private MyCoursesActivity activity;
    private String userID;
    private CollectionReference teachersRef;
    private ArrayList<String> courses;
    private ArrayList<Integer> grades;

    public CoursesModel(MyCoursesActivity activity, String userID) {
        this.activity = activity;
        this.userID = userID;
        this.teachersRef = FirebaseFirestore.getInstance().collection("teachers");
    }

    public GoogleSignInClient googleSignInClient() {
        return GoogleSignIn.getClient(this.activity, GoogleSignInOptions.DEFAULT_SIGN_IN);
    }
    public void setData(){
        courses = new ArrayList<>();
        grades = new ArrayList<>();
        Call<ArrayList<String>> call = RetrofitClient.getInstance().getAPI().getTeacherCourses(userID);
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                courses = response.body();

                Call<ArrayList<Integer>> call1 = RetrofitClient.getInstance().getAPI().getTeacherGrades(userID);
                call1.enqueue(new Callback<ArrayList<Integer>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Integer>> call1, Response<ArrayList<Integer>> response1) {
                        grades = response1.body();

                        HashMap<String, String> course_and_grade = new HashMap<>();
                        for (int i = 0; i < grades.size(); i++) {
                            course_and_grade.put(courses.get(i), String.valueOf(grades.get(i)));
                        }
                        activity.IterateData(course_and_grade);
                        activity.setUpOnclickListener();
                    }
                    @Override
                    public void onFailure(Call<ArrayList<Integer>> call1, Throwable t1) {
                        Log.d("failed grades", t1.getMessage());
                    }
                });
            }
            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Log.d("failed courses", t.getMessage());
            }
        });
    }

    public void updateDataBase(String course, String grade, String price) {
        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().addCourseAndGradeToTeacher(userID, course, grade, price);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("done", "done");
                    String course_grade_price = course + " - " + grade + " - " + price + " ₪";
                    Toast.makeText(activity, course_grade_price + " have been added successfully", Toast.LENGTH_SHORT).show();
                    setData();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Failed to add course", t.getMessage());
                Toast.makeText(activity, "error in adding new course to teach", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void yes_click(String course) {
        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().deleteCourseAndGradeFromTeacher(userID, course);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("done", "done");
                    Toast.makeText(activity, course + " have been deleted successfully", Toast.LENGTH_SHORT).show();
                    setData();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Failed to delete course", t.getMessage());
                Toast.makeText(activity, "error in deleting " + course, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
