package com.example.studybuddy.model;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.objects.Student;
import com.example.studybuddy.viewModel.StudentProfileActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
public class StudentProfileModel {


    private Activity activity;
    private String userUID;
    private DocumentReference documentReference;
    private FirebaseUser user;
    private FirebaseFirestore db;


    public StudentProfileModel (Activity activity){
        this.activity = activity;
        this.db = FirebaseFirestore.getInstance();
        this.userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.documentReference = db.collection("students").document(userUID);
        this.user  = FirebaseAuth.getInstance().getCurrentUser();
    }

    public GoogleSignInClient googleSignInClient() {
        return GoogleSignIn.getClient(this.activity, GoogleSignInOptions.DEFAULT_SIGN_IN);
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public String getUserUID() {
        return userUID;
    }

    public DocumentReference getDocumentReference() {
        return documentReference;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void modelOnStart(EditText name, EditText age, EditText year, EditText degree, EditText phone_number, StudentProfileActivity thisactivity) {
        Call<Student> call = RetrofitClient.getInstance().getAPI().getStudentDetails(userUID);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                Student curr_student;
                curr_student = response.body();
                String nameResult = curr_student.getName();
                String ageResult = curr_student.getAge();
                String yearResult = curr_student.getYear();
                String degreeResult = curr_student.getDegree();
                String phoneResult = curr_student.getPhone();
                name.setText(nameResult);
                age.setText(ageResult);
                year.setText(yearResult);
                degree.setText(degreeResult);
                phone_number.setText(phoneResult);
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Log.d("Fail", t.getMessage());
                Toast.makeText(thisactivity, "no profile yet" , Toast.LENGTH_SHORT).show();

            }
        });

//        this.documentReference.get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.getResult().exists()){
//                            String nameResult = task.getResult().getString("name");
//                            String ageResult = task.getResult().getString("age");
//                            String yearResult = task.getResult().getString("year");
//                            String degreeResult = task.getResult().getString("degree");
//                            name.setText(nameResult);
//                            age.setText(ageResult);
//                            year.setText(yearResult);
//                            degree.setText(degreeResult);
//                        }else{
//                            Toast.makeText(thisactivity, "no profile yet" , Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }
}
