package com.example.studybuddy.model;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.viewModel.HomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeModel {
    private HomeActivity activity;
    public String userID;
    private CollectionReference classesRef;
    private Query query;


    public HomeModel(HomeActivity activity, String userID, String classes, String students, String teachers) {
        this.activity = activity;
        this.userID = userID;
        this.classesRef = FirebaseFirestore.getInstance().collection(classes);
    }

    public Query buildClassQuery(String field){
//        Call<Query> call = RetrofitClient.getInstance().getAPI().getClassQuery(userID);
//        call.enqueue(new Callback<Query>() {
//            @Override
//            public void onResponse(Call<Query> call, Response<Query> response) {
//                query = response.body();
//                Log.d("check meeee", query.toString());
//
//            }
//            @Override
//            public void onFailure(Call<Query> call, Throwable t) {
//                Log.d("failed phone", t.getMessage());
//            }
//        });
//        return this.query;
        return this.classesRef.whereEqualTo(field, this.userID);
    }

    public GoogleSignInClient googleSignInClient() {
        return GoogleSignIn.getClient(this.activity, GoogleSignInOptions.DEFAULT_SIGN_IN);
    }

    public void onWhatsAppMessageClick(String name, String subject, String date) {
        Call<String> call = RetrofitClient.getInstance().getAPI().getStudentMobileNumber(userID, name, subject, date);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String mobile_number = response.body();
                openWhatsApp(mobile_number);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("failed phone", t.getMessage());
            }
        });
    }

    private void openWhatsApp(String mobile_number){
        boolean installed = appInstalledOrNot("com.whatsapp");
        if (installed){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData ( Uri.parse ( "https://wa.me/" + "+972" + mobile_number + "/?text=" + "" ) );
            this.activity.startActivity(intent);
        } else {
            Toast.makeText(this.activity.getApplicationContext(), "whatsApp is not installed on this device", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean appInstalledOrNot(String url){
        boolean app_installed;
        try{
            this.activity.getPackageManager().getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void click_yes(String name, String subject, String date){
        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().deleteClassTeacher(userID, name, subject, date);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("done", "done");
                    Toast.makeText(activity, "class have been canceled successfully", Toast.LENGTH_SHORT).show();
                    activity.adapter.notifyDataSetChanged();
                    activity.setUpRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Failed to delete class", t.getMessage());
                Toast.makeText(activity, "error in removing class", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
