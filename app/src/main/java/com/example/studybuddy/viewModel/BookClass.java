package com.example.studybuddy.viewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studybuddy.R;
import com.example.studybuddy.adapter.TeacherAdapter;
import com.example.studybuddy.model.BookClassModel;
import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.objects.Teacher;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookClass extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    BookClassModel model = new BookClassModel(this);

    ListView listView;
    private Spinner coursesSpinner, fromHourSpinner, toHourSpinner;
    private Button start_filter, datesButton;
    RadioGroup sort_group;
    RadioButton sort;
    String sortId = "3";

    private DatePickerDialog datePickerDialog;
    ArrayList<Teacher> filteredTeachers = new ArrayList<Teacher>();

    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_class);

        googleSignInClient = model.googleSignInClient();

        coursesSpinner = findViewById(R.id.coursesSpinner);
        datesButton = findViewById(R.id.datesButton);
        fromHourSpinner = findViewById(R.id.fromHourSpinner);
        toHourSpinner = findViewById(R.id.toHourSpinner);
        start_filter = findViewById(R.id.filterFromSpinner);
        sort_group = findViewById(R.id.sort_group);


        coursesSpinner.setOnItemSelectedListener(this);
        fromHourSpinner.setOnItemSelectedListener(this);
        toHourSpinner.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> coursesSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.courses_array, android.R.layout.simple_spinner_item);
        coursesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coursesSpinner.setAdapter(coursesSpinnerAdapter);

        ArrayAdapter<CharSequence> fromSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.from_hours_array, android.R.layout.simple_spinner_item);
        fromSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromHourSpinner.setAdapter(fromSpinnerAdapter);

        ArrayAdapter<CharSequence> toSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.to_hours_array, android.R.layout.simple_spinner_item);
        toSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toHourSpinner.setAdapter(toSpinnerAdapter);

        datesButton.setOnClickListener(v -> {
            initDatePicker();
        });

        setupData();

        start_filter.setOnClickListener(v -> {
            if (model.getCourseValueFromSpinner().equals("choose course")){
                Toast.makeText(this, "choose course", Toast.LENGTH_SHORT).show();
            } else {
                int radioID = sort_group.getCheckedRadioButtonId();
                if (radioID == -1) {
                    Toast.makeText(this, "no sort filter", Toast.LENGTH_SHORT).show();
                    initFilteredTeachers();
                }
                else {
                    sort = findViewById(radioID);
                    sortId = sort.getText().toString();
                    initFilteredTeachers();
                }
            }
        });

        initSearchWidgets();
        setUpOnclickListener();
    }

    private void initFilteredTeachers(){

        Call<ArrayList<Teacher>> call = RetrofitClient.getInstance().getAPI().getFilteredTeachers(model.getCourseValueFromSpinner(), model.getDateValueFromButton(), model.getFromHourValueFromSpinner(), model.getToHourValueFromSpinner(), sortId);
        call.enqueue(new Callback<ArrayList<Teacher>>() {
            @Override
            public void onResponse(Call<ArrayList<Teacher>> call, Response<ArrayList<Teacher>> response) {
                filteredTeachers = response.body();
                if( filteredTeachers == null){
                    filteredTeachers = new ArrayList<Teacher>();
                }
                TeacherAdapter adapter = new TeacherAdapter(getApplicationContext(), 0, filteredTeachers, model.getCourseValueFromSpinner());
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Teacher>> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });
    }

    private void initSearchWidgets()
    {
        SearchView searchView = (SearchView) findViewById(R.id.teacherListSearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                model.modelOnQueryTextChange(s);

                TeacherAdapter adapter = new TeacherAdapter(getApplicationContext(), 0, model.getFilteredTeachers());
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

                return false;
            }
        });
    }

    private void setupData() {
        model.beforeSetUpData();
    }


    private void setUpOnclickListener()
    {
        listView = (ListView) findViewById(R.id.teachersListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Teacher selectTeacher = (Teacher) (listView.getItemAtPosition(position));
                popupInfoOrBook(selectTeacher);
            }
        });
    }

    private void popupInfoOrBook(Teacher currentTeacher) {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.info_or_book_popup, null);

        ImageButton info = (ImageButton) popupView.findViewById(R.id.popup_info);
        ImageButton book = (ImageButton) popupView.findViewById(R.id.popup_book);

        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                openTeacherDetails(currentTeacher);
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), DetailActivityTeacher.class);

                intent.putExtra("id",currentTeacher);
                startActivity(intent);
            }
        });
    }

    public void openTeacherDetails(Teacher currentTeacher) {
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
        if (model.getCourseValueFromSpinner().equals("choose course")){
            grade.setText("---");
        } else{
            int index = currentTeacher.getCourses().indexOf(model.getCourseValueFromSpinner());
            grade.setText(""+currentTeacher.getGrades().get(index));
        }
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
                startActivity(new Intent(BookClass.this, StudentHomeActivity.class));
                return true;
            case R.id.action_edit_profile:
                startActivity(new Intent(BookClass.this, StudentProfileActivity.class));
                finish();
                return true;
            case R.id.action_book_a_class:
                startActivity(new Intent(BookClass.this, BookClass.class));
                finish();
                return true;
            case R.id.action_payments:
                startActivity(new Intent(BookClass.this, StudentMyPaymentActivity.class));
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
                            startActivity(new Intent(BookClass.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            case R.id.action_change_user_type:
                startActivity(new Intent(BookClass.this, ChooseUserActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.coursesSpinner) {
            model.setCourseValueFromSpinner(parent.getItemAtPosition(position).toString());
        }
        if (parent.getId() == R.id.fromHourSpinner) {
            model.setFromHourValueFromSpinner(parent.getItemAtPosition(position).toString());
        }
        if (parent.getId() == R.id.toHourSpinner) {
            model.setToHourValueFromSpinner(parent.getItemAtPosition(position).toString());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    // choose date
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                datesButton.setText(date);
                model.setDateValueFromButton(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.show();

    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

}