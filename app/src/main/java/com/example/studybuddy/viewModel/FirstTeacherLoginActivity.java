package com.example.studybuddy.viewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.studybuddy.R;
import com.example.studybuddy.model.FirstTeacherLoginModel;
import com.example.studybuddy.objects.Teacher;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Intent;


public class FirstTeacherLoginActivity extends AppCompatActivity {

    FirstTeacherLoginModel model = new FirstTeacherLoginModel(this);

    EditText name, degree, year, age, phone_number, pay_box;
    Button save;
    RadioGroup gender_group;
    RadioButton gender;
    DocumentReference documentReference;

    private String userUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_teacher_login);


        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        degree = findViewById(R.id.degree);
        year = findViewById(R.id.year);
        save = findViewById(R.id.save);
        gender_group = findViewById(R.id.gender_group);
        phone_number = findViewById(R.id.phone);
        pay_box = findViewById(R.id.pay_box);


        //onclick listener for updating profile button
        save.setOnClickListener(v -> {
            //Converting fields to text
            int radioID = gender_group.getCheckedRadioButtonId();
            gender = findViewById(radioID);
            String textGender = gender.getText().toString();
            String textAge = age.getText().toString();
            String textName = name.getText().toString();
            String textDegree = degree.getText().toString();
            String textYear = year.getText().toString();
            String textPhone = phone_number.getText().toString();
            String textPayBox = pay_box.getText().toString();

            if (TextUtils.isEmpty(textName) || TextUtils.isEmpty(textDegree) || TextUtils.isEmpty(textYear) || TextUtils.isEmpty(textGender) || TextUtils.isEmpty(textAge) || TextUtils.isEmpty(textPhone) || TextUtils.isEmpty(textPayBox)) {
                Toast.makeText(FirstTeacherLoginActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
            }else if (textPhone.length() != 9){
                Toast.makeText(FirstTeacherLoginActivity.this, "phone number is illegal", Toast.LENGTH_SHORT).show();
            } else {
                updateProfile(textName, textYear, textDegree, textGender, textAge, textPhone, textPayBox);
                startActivity(new Intent(FirstTeacherLoginActivity.this, ProfileActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    public void updateProfile(String textName, String textYear, String textDegree, String textGender, String textAge, String textPhone, String textPayBox) {

        model.newTeacherModel(textName, textYear, textDegree, textGender, textAge, textPhone, textPayBox);

    }

}
