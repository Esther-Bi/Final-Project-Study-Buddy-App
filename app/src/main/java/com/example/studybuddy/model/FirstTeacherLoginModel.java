package com.example.studybuddy.model;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.viewModel.FirstTeacherLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstTeacherLoginModel {

    Activity activity;
    private FirebaseUser user;

    public FirstTeacherLoginModel (Activity activity){
        this.activity = activity;
        this.user  = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void newTeacherModel(String textName, String textYear, String textDegree, String textGender, String textAge, String textPhone, String textPayBox) {

        assert user != null;

        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().newTeacher(user.getUid(), textName, textYear, textDegree, textGender, textAge, textPhone, textPayBox);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("done", "done");

                    Toast.makeText(activity, "Updated Profile successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });

    }
}
