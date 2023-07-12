package com.example.studybuddy.viewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studybuddy.R;
import com.example.studybuddy.model.DetailTeacherModel;
import com.example.studybuddy.objects.Teacher;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class DetailActivityTeacher extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DetailTeacherModel model = new DetailTeacherModel(this,FirebaseAuth.getInstance().getCurrentUser().getUid());
    Teacher currentTeacher;
    private TextView chosenCourse, chosenCost, chosenDate;
    private Spinner coursesSpinner, datesSpinner;

    String courseValueFromSpinner, dateValueFromSpinner;
    private Button bookClass, teacherDetails, back;

    String teacherID;
    String[] classes;
    Integer[] prices;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_teacher);

        Bundle bundle = getIntent().getExtras();
        currentTeacher = (Teacher) bundle.getParcelable("id");

//        Get teacher's ID
        teacherID = currentTeacher.getId();

        chosenCourse = findViewById(R.id.chosenCourse);
        chosenCost = findViewById(R.id.chosenCost);
        chosenDate = findViewById(R.id.chosenDate);
        coursesSpinner = findViewById(R.id.coursesSpinner);
        datesSpinner = findViewById(R.id.datesSpinner);
        bookClass = findViewById(R.id.bookClass);
        back = findViewById(R.id.backToBook);
        teacherDetails = findViewById(R.id.teacherDetails);

        coursesSpinner.setOnItemSelectedListener(this);
        datesSpinner.setOnItemSelectedListener(this);

        classes = currentTeacher.getCourses().toArray((new String[currentTeacher.getCourses().size()]));
        prices = currentTeacher.getPrices().toArray((new Integer[currentTeacher.getPrices().size()]));

        ArrayAdapter courseAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, classes);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coursesSpinner.setAdapter(courseAdapter);

        String[] dates = currentTeacher.getDates().toArray((new String[currentTeacher.getDates().size()]));
        ArrayAdapter datesAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, dates);
        datesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        datesSpinner.setAdapter(datesAdapter);


        bookClass.setOnClickListener(v -> {
            model.bookClass(classes, courseValueFromSpinner, prices,currentTeacher, dateValueFromSpinner, teacherID);
        });

        back.setOnClickListener(v -> {
            super.finish();
        });

        teacherDetails.setOnClickListener(v -> {
            openTeacherDetails();
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.coursesSpinner) {
            courseValueFromSpinner = parent.getItemAtPosition(position).toString();
            chosenCourse.setText("Chosen course: " + courseValueFromSpinner);
            int index_of_course = Arrays.asList(classes).indexOf(courseValueFromSpinner);
            int cost = prices[index_of_course];
            chosenCost.setText("Price: " + cost + " ₪");
        }
        if (parent.getId() == R.id.datesSpinner) {
            dateValueFromSpinner = parent.getItemAtPosition(position).toString();
            chosenDate.setText("Chosen date: " + dateValueFromSpinner);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void start_activity() {
        startActivity(new Intent(DetailActivityTeacher.this, BookClass.class));
        finish();
    }

    public void openTeacherDetails() {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.teacher_details_popup, null);

        TextView name = popupView.findViewById(R.id.popup_name);
        TextView degree = popupView.findViewById(R.id.popup_degree);
        TextView grade = popupView.findViewById(R.id.popup_grade);
        RatingBar rating = popupView.findViewById(R.id.popup_rating);
        TextView age = popupView.findViewById(R.id.popup_age);
        ImageButton popup_close = (ImageButton) popupView.findViewById(R.id.popup_teacher_details_close);
        ImageView male = popupView.findViewById(R.id.popup_teacher_details_male);
        ImageView female = popupView.findViewById(R.id.popup_teacher_details_female);

        name.setText(""+currentTeacher.getName());
        degree.setText(""+currentTeacher.getDegree()+" , "+currentTeacher.getYear());
        int index_of_course = Arrays.asList(classes).indexOf(courseValueFromSpinner);
        grade.setText(""+currentTeacher.getGrades().get(index_of_course));
        rating.setRating((float) currentTeacher.getRating());
        age.setText(""+currentTeacher.getAge());

        if (currentTeacher.getGender().equals("Male")) {
            female.setVisibility(View.INVISIBLE);
        } else {
            male.setVisibility(View.INVISIBLE);
        }

        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        popup_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


}