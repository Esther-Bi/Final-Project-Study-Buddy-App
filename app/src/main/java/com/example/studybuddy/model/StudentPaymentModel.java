package com.example.studybuddy.model;

import static java.lang.Integer.parseInt;

import android.util.Log;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studybuddy.adapter.GroupAdapter;
import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.objects.Class;
import com.example.studybuddy.objects.Group;
import com.example.studybuddy.objects.Teacher;
import com.example.studybuddy.viewModel.GroupHomeActivity;
import com.example.studybuddy.viewModel.StudentMyPaymentActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentPaymentModel {
    private StudentMyPaymentActivity activity;
    private String userID;
    private CollectionReference classesRef;
    private CollectionReference teachersRef;

    public StudentPaymentModel(StudentMyPaymentActivity activity, String userID, String classes) {
        this.activity = activity;
        this.userID = userID;
        this.classesRef = FirebaseFirestore.getInstance().collection(classes);
        this.teachersRef = FirebaseFirestore.getInstance().collection("teachers");

    }

    public GoogleSignInClient googleSignInClient() {
        return GoogleSignIn.getClient(this.activity, GoogleSignInOptions.DEFAULT_SIGN_IN);
    }

    public void updatePastCourses() {

//        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().PastCourses();
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
//                if(response.isSuccessful()){
//                    Log.d("done", "done");
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d("Fail", t.getMessage());
//            }
//        });
        this.classesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if(documentSnapshot.exists()){
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy - HH:mm");
                        Class current_class = documentSnapshot.toObject(Class.class);
                        if (current_class.compare(formatter.format(date))){
                            String dbKey = documentSnapshot.getId();
                            classesRef.document(dbKey)
                                    .update("past", "yes");
                        }
                    }
                }
            }
        });
    }

    public Query buildClassQuery(String field){
        return this.classesRef.whereEqualTo(field, this.userID).whereEqualTo("past","yes");
    }

    public void pay_for_class(String teacherName, String subject, String date) {

        Call<String> call = RetrofitClient.getInstance().getAPI().payBoxLink(this.userID,teacherName,subject,date);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String pay_box = response.body();
                activity.openPayBoxApp(pay_box);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });
    }

    public void onApprovePayment(String teacherName, String subject, String date) {

        Call<String> call = RetrofitClient.getInstance().getAPI().approvePayment(this.userID,  teacherName, date, subject);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String getStudentApproval = response.body();
                if (parseInt(getStudentApproval) == 1) {
                    Toast.makeText(activity, "you already paid", Toast.LENGTH_SHORT).show();
                } else{
                    activity.approvePaymentQuestionPopup(teacherName, subject, date);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });
    }

    public void approve_click_yes(String teacherName, String date, String subject) {

        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().approveYes(this.userID,  teacherName, date, subject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(activity, "paid successfully", Toast.LENGTH_SHORT).show();
                    Log.d("done", "done");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });
    }


    public void rate_click_save(String teacherName, String date, String subject, RatingBar rt) {

        double rating = rt.getRating();

        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().updateRate(this.userID,  teacherName, date, subject, rating);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("done", "done");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });
    }
}
