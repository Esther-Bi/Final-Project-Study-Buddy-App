package com.example.studybuddy.viewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.studybuddy.R;
import com.example.studybuddy.model.StudentProfileModel;
import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.objects.Student;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Intent;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StudentProfileActivity extends AppCompatActivity {

    StudentProfileModel model = new StudentProfileModel(this);

    EditText name, degree, year, age, phone_number;
    Button save;
    RadioGroup gender_group;
    RadioButton gender;
    DocumentReference documentReference;
    FirebaseFirestore db = model.getDb();

    GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        googleSignInClient= model.googleSignInClient();

        FirebaseUser user = model.getUser();
        String userUID = model.getUserUID();
        documentReference = model.getDocumentReference();


        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        degree = findViewById(R.id.degree);
        year = findViewById(R.id.year);
        phone_number = findViewById(R.id.phone);
        save = findViewById(R.id.save);
        gender_group = findViewById(R.id.gender_group);


        //onclick listener for updating profile button
        save.setOnClickListener(v -> {
            //Converting fields to text
            int radioID = gender_group.getCheckedRadioButtonId();
            if (radioID == -1) {
                Toast.makeText(StudentProfileActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
            } else {
                gender = findViewById(radioID);
                String textGender = gender.getText().toString();
                String textAge = age.getText().toString();
                String textName = name.getText().toString();
                String textDegree = degree.getText().toString();
                String textYear = year.getText().toString();
                String textPhone = phone_number.getText().toString();

                if (TextUtils.isEmpty(textName) || TextUtils.isEmpty(textDegree) || TextUtils.isEmpty(textYear) || TextUtils.isEmpty(textGender) || TextUtils.isEmpty(textAge) || TextUtils.isEmpty(textPhone)) {
                    Toast.makeText(StudentProfileActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                }else if (textPhone.length() != 9){
                    Toast.makeText(StudentProfileActivity.this, "phone number is illegal", Toast.LENGTH_SHORT).show();
                } else {
                    updateProfile(textName, textYear, textDegree, textGender, textAge, textPhone, user, db);
                    startActivity(new Intent(this, StudentHomeActivity.class));
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        model.modelOnStart(name, age,year,degree, phone_number, StudentProfileActivity.this);
    }

    //ngfrdtyktufyh
    public void updateProfile(String textName, String textYear, String textDegree, String textGender, String textAge, String textPhone, FirebaseUser user, FirebaseFirestore database) {

        assert user != null;
        String userUID = user.getUid();

        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().updateStudentDetails(userUID, textName, textYear, textDegree, textGender, textAge, textPhone);
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

//        Student studentToAdd = new Student(textName, textYear, textDegree, textGender, textAge, textPhone, userUID); //creating a new user
//        database.collection("students").document(userUID).set(studentToAdd); //adding user data to database

        startActivity(new Intent(StudentProfileActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_student_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_classes:
                startActivity(new Intent(StudentProfileActivity.this, StudentHomeActivity.class));
                return true;
            case R.id.action_edit_profile:
                startActivity(new Intent(StudentProfileActivity.this, StudentProfileActivity.class));
                finish();
                return true;
            case R.id.action_payments:
                startActivity(new Intent(StudentProfileActivity.this, MyPaymentsActivity.class));
                finish();
                return true;
            case R.id.action_book_a_class:
                startActivity(new Intent(StudentProfileActivity.this, BookClass.class));
                finish();
                return true;
            case R.id.action_log_out:
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            // When task is successful, sign out from firebase
                            FirebaseAuth.getInstance().signOut();
                            // Display Toast
                            Toast.makeText(getApplicationContext(), "Logout successfully, See you soon (:", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(StudentProfileActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            case R.id.action_change_user_type:
                startActivity(new Intent(StudentProfileActivity.this, ChooseUserActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
