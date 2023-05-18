package com.example.studybuddy.viewModel;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studybuddy.R;
import com.example.studybuddy.model.api.RetrofitClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGroupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    GoogleSignInClient googleSignInClient;
    // Initialize variable
    TextView subjectSpinner;
    TextView degreeSpinner;
    EditText linkSpinner;
    private Spinner yearSpinner, daySpinner, timeSpinner, languageSpinner,
            minParticipantsSpinner, maxParticipantsSpinner, locationSpinner;
    Dialog dialog;
    Button openGroup;
    String subject = "choose course", degree = "Select Degree", year, day, time, language, min, max, location, link = "Insert WhatsApp Group Link";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleSignInClient = GoogleSignIn.getClient(AddGroupActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        setContentView(R.layout.activity_add_group);

        subjectSpinner = findViewById(R.id.subjectSpinner);
        ArrayAdapter<CharSequence> coursesSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.courses_array, android.R.layout.simple_spinner_item);
        degreeSpinner = findViewById(R.id.degreeSpinner);
        ArrayAdapter<CharSequence> degreesSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.degrees, android.R.layout.simple_spinner_item);

        yearSpinner = findViewById(R.id.yearSpinner);
        yearSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> yearSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
        yearSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearSpinnerAdapter);

        daySpinner = findViewById(R.id.daySpinner);
        daySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> daySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.days, android.R.layout.simple_spinner_item);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(daySpinnerAdapter);

        timeSpinner = findViewById(R.id.timeSpinner);
        timeSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> timeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.times, android.R.layout.simple_spinner_item);
        timeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeSpinnerAdapter);

        languageSpinner = findViewById(R.id.languageSpinner);
        languageSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> languageSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageSpinnerAdapter);

        minParticipantsSpinner = findViewById(R.id.minParticipantsSpinner);
        minParticipantsSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> minParticipantsSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.numbers_min, android.R.layout.simple_spinner_item);
        minParticipantsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minParticipantsSpinner.setAdapter(minParticipantsSpinnerAdapter);

        maxParticipantsSpinner = findViewById(R.id.maxParticipantsSpinner);
        maxParticipantsSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> maxParticipantsSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.numbers_max, android.R.layout.simple_spinner_item);
        maxParticipantsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxParticipantsSpinner.setAdapter(maxParticipantsSpinnerAdapter);

        locationSpinner = findViewById(R.id.locationSpinner);
        locationSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> locationSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.locations, android.R.layout.simple_spinner_item);
        locationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationSpinnerAdapter);

        linkSpinner = findViewById(R.id.linkSpinner);

        openGroup = findViewById(R.id.open_group);

        subjectSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(AddGroupActivity.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(800,1000);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.search_subject_degree);
                ListView listView = dialog.findViewById(R.id.search_subject_degree_list_view);

                listView.setAdapter(coursesSpinnerAdapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        coursesSpinnerAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        subjectSpinner.setText(coursesSpinnerAdapter.getItem(position));
                        subject = coursesSpinnerAdapter.getItem(position).toString();
                        dialog.dismiss();
                    }
                });
            }
        });

        degreeSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(AddGroupActivity.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(800,1000);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.search_subject_degree);
                ListView listView = dialog.findViewById(R.id.search_subject_degree_list_view);

                listView.setAdapter(degreesSpinnerAdapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        degreesSpinnerAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        degreeSpinner.setText(degreesSpinnerAdapter.getItem(position));
                        degree = degreesSpinnerAdapter.getItem(position).toString();
                        dialog.dismiss();
                    }
                });
            }
        });

        openGroup.setOnClickListener(v -> {
            link = linkSpinner.getText().toString();
            if (subject.equals("choose course")){
                Toast.makeText(this, "select subject", Toast.LENGTH_SHORT).show();
            }else if (degree.equals("Select Degree")){
                Toast.makeText(this, "select degree", Toast.LENGTH_SHORT).show();
            }else if (year.equals("Select Year Of Study")){
            Toast.makeText(this, "select year", Toast.LENGTH_SHORT).show();
            }else if (day.equals("Select Day")){
            Toast.makeText(this, "select day", Toast.LENGTH_SHORT).show();
            }else if (time.equals("Select Time")){
                Toast.makeText(this, "select time", Toast.LENGTH_SHORT).show();
            }else if (language.equals("Select Language")){
                Toast.makeText(this, "select language", Toast.LENGTH_SHORT).show();
            }else if (min.equals("Select Min Participants")){
                Toast.makeText(this, "select min participants", Toast.LENGTH_SHORT).show();
            }else if (max.equals("Select Max Participants")){
                Toast.makeText(this, "select max participants", Toast.LENGTH_SHORT).show();
            }else if(parseInt(min)>parseInt(max)){
                Toast.makeText(this, "min participants number should by less than max participants number", Toast.LENGTH_SHORT).show();
            }else if (location.equals("Select Location")){
                Toast.makeText(this, "select location", Toast.LENGTH_SHORT).show();
            }else if (link.equals("Insert WhatsApp Group Link")){
                Toast.makeText(this, "insert whatsApp group link", Toast.LENGTH_SHORT).show();
            }else{
                Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().openNewGroup(FirebaseAuth.getInstance().getCurrentUser().getUid(), subject,degree, year, day, time, language, min, max, location, link);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Log.d("done", "done");
                            Toast.makeText(AddGroupActivity.this, "group is available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("Fail", t.getMessage());
                        Toast.makeText(AddGroupActivity.this, "error in opening group", Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(new Intent(AddGroupActivity.this, ChooseUserActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        if (parent.getId() == R.id.yearSpinner) {
            year = parent.getItemAtPosition(position).toString();
        }
        if (parent.getId() == R.id.daySpinner) {
            day = parent.getItemAtPosition(position).toString();
        }
        if (parent.getId() == R.id.timeSpinner) {
            time = parent.getItemAtPosition(position).toString();
        }
        if (parent.getId() == R.id.languageSpinner) {
            language = parent.getItemAtPosition(position).toString();
        }
        if (parent.getId() == R.id.minParticipantsSpinner) {
            min = parent.getItemAtPosition(position).toString();
        }
        if (parent.getId() == R.id.maxParticipantsSpinner) {
            max = parent.getItemAtPosition(position).toString();
        }
        if (parent.getId() == R.id.locationSpinner) {
            location = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_groups:
                startActivity(new Intent(AddGroupActivity.this, GroupHomeActivity.class));
                return true;
            case R.id.action_edit_profile:
                startActivity(new Intent(AddGroupActivity.this, GroupProfileActivity.class));
                finish();
                return true;
            case R.id.action_search_group:
                startActivity(new Intent(AddGroupActivity.this, SearchGroupActivity.class));
                finish();
                return true;
            case R.id.action_open_group:
                startActivity(new Intent(AddGroupActivity.this, AddGroupActivity.class));
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
                            startActivity(new Intent(AddGroupActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            case R.id.action_change_user_type:
                startActivity(new Intent(AddGroupActivity.this, ChooseUserActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}