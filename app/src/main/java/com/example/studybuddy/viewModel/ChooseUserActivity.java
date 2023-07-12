package com.example.studybuddy.viewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.studybuddy.R;

public class ChooseUserActivity extends AppCompatActivity {

    Button sign_as_student , sign_as_teacher , first_sign_as_student , first_sign_as_teacher, sign_as_group , first_sign_as_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);

        sign_as_student = findViewById(R.id.sign_as_student);
        sign_as_teacher = findViewById(R.id.sign_as_teacher);
        first_sign_as_student = findViewById(R.id.first_sign_as_student);
        first_sign_as_teacher = findViewById(R.id.first_sign_as_teacher);
        sign_as_group = findViewById(R.id.sign_as_group);
        first_sign_as_group = findViewById(R.id.first_sign_as_group);

        sign_as_student.setOnClickListener(v -> {
            startActivity(new Intent(ChooseUserActivity.this, StudentHomeActivity.class));
        });

        sign_as_teacher.setOnClickListener(v -> {
            startActivity(new Intent(ChooseUserActivity.this, HomeActivity.class));
        });

        first_sign_as_student.setOnClickListener(v -> {
            Intent i = new Intent(this, StudentProfileActivity.class);
            i.putExtra("KEY","first");
            startActivity(i);
        });

        first_sign_as_teacher.setOnClickListener(v -> {
            startActivity(new Intent(ChooseUserActivity.this, FirstTeacherLoginActivity.class));
        });

        sign_as_group.setOnClickListener(v -> {
            startActivity(new Intent(ChooseUserActivity.this, GroupHomeActivity.class));
        });

        first_sign_as_group.setOnClickListener(v -> {
            startActivity(new Intent(ChooseUserActivity.this, GroupHomeActivity.class));
        });

    }
}