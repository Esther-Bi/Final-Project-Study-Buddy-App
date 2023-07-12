package com.example.studybuddy.model;

import static java.lang.Integer.parseInt;

import android.util.Log;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.viewModel.StudentMyPaymentActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().PastCourses();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("PastCourses", "done");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Fail PastCourses", t.getMessage());
            }
        });
//        this.classesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                    if(documentSnapshot.exists()){
//                        Date date = new Date();
//                        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy - HH:mm");
//                        Class current_class = documentSnapshot.toObject(Class.class);
//                        if (current_class.compare(formatter.format(date))){
//                            String dbKey = documentSnapshot.getId();
//                            classesRef.document(dbKey)
//                                    .update("past", "yes");
//                        }
//                    }
//                }
//            }
//        });
    }

    public void pay_for_class(String teacherName, String subject, String date) {
        Call<String> call = RetrofitClient.getInstance().getAPI().payBoxLink(this.userID,teacherName,subject,date);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String pay_box = response.body();
                Call<String> call1 = RetrofitClient.getInstance().getAPI().approvePayment(userID,  teacherName, date, subject);
                call1.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call1, Response<String> response1) {
                        String getStudentApproval = response1.body();
                        if (parseInt(getStudentApproval) == 1) {
                            Toast.makeText(activity, "you already paid", Toast.LENGTH_SHORT).show();
                        } else{
                            activity.openPayBoxApp(pay_box);
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call1, Throwable t1) {
                        Log.d("Fail approvePayment", t1.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Fail payBoxLink", t.getMessage());
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
                Log.d("Fail approvePayment", t.getMessage());
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
                    Log.d("approveYes", "done");
                    activity.setUpRecyclerView();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Fail approveYes", t.getMessage());
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
                    Log.d("updateRate", "done");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Fail updateRate", t.getMessage());
            }
        });
    }
}
