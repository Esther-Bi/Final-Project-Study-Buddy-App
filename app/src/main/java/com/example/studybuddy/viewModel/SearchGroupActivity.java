package com.example.studybuddy.viewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.studybuddy.adapter.FilteredGroupAdapter;
import com.example.studybuddy.adapter.TeacherAdapter;
import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.objects.Teacher;
import com.example.studybuddy.objects.Group;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchGroupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView listView;
    TextView subjectsSpinner, degreesSpinner;
    Spinner yearsSpinner, daysSpinner, timesSpinner,
            languagesSpinner, locationsSpinner, participantsSpinner;
    Button searchGroupsBtn;
    Dialog dialog;
    String subject = "choose course", degree = "Select Degree", year, day, time, language, participants, location;


    ArrayList<Group> filteredGroups = new ArrayList<Group>();

    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group);

        //googleSignInClient = model.googleSignInClient();

        subjectsSpinner = findViewById(R.id.subjectsSpinner);
        degreesSpinner = findViewById(R.id.degreesSpinner);
        yearsSpinner = findViewById(R.id.yearsSpinner);
        daysSpinner = findViewById(R.id.daysSpinner);
        timesSpinner = findViewById(R.id.timesSpinner);
        languagesSpinner = findViewById(R.id.languagesSpinner);
        locationsSpinner = findViewById(R.id.locationsSpinner);
        participantsSpinner = findViewById(R.id.participantsSpinner);
        searchGroupsBtn = findViewById(R.id.search_groups_btn);

        yearsSpinner.setOnItemSelectedListener(this);
        daysSpinner.setOnItemSelectedListener(this);
        timesSpinner.setOnItemSelectedListener(this);
        languagesSpinner.setOnItemSelectedListener(this);
        locationsSpinner.setOnItemSelectedListener(this);
        participantsSpinner.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> coursesSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.courses_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> degreesSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.degrees, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> yearSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
        yearSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsSpinner.setAdapter(yearSpinnerAdapter);

        ArrayAdapter<CharSequence> daySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.days, android.R.layout.simple_spinner_item);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysSpinner.setAdapter(daySpinnerAdapter);

        ArrayAdapter<CharSequence> timeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.times, android.R.layout.simple_spinner_item);
        timeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timesSpinner.setAdapter(timeSpinnerAdapter);

        ArrayAdapter<CharSequence> languageSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languagesSpinner.setAdapter(languageSpinnerAdapter);

        ArrayAdapter<CharSequence> participantsSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.participants, android.R.layout.simple_spinner_item);
        participantsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        participantsSpinner.setAdapter(participantsSpinnerAdapter);

        ArrayAdapter<CharSequence> locationSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.locations, android.R.layout.simple_spinner_item);
        locationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationsSpinner.setAdapter(locationSpinnerAdapter);


        setupData();

        subjectsSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(SearchGroupActivity.this);
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
                        subjectsSpinner.setText(coursesSpinnerAdapter.getItem(position));
                        subject = coursesSpinnerAdapter.getItem(position).toString();
                        dialog.dismiss();
                    }
                });
            }
        });

        degreesSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(SearchGroupActivity.this);
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
                        degreesSpinner.setText(degreesSpinnerAdapter.getItem(position));
                        degree = degreesSpinnerAdapter.getItem(position).toString();
                        dialog.dismiss();
                    }
                });
            }
        });


        searchGroupsBtn.setOnClickListener(v -> {
                initFilteredGroups();
        });

        setUpOnclickListener();
    }

    private void setupData() {
        Call<ArrayList<Group>> call = RetrofitClient.getInstance().getAPI().getAllGroups();
        call.enqueue(new Callback<ArrayList<Group>>() {
            @Override
            public void onResponse(Call<ArrayList<Group>> call, Response<ArrayList<Group>> response) {
                filteredGroups = response.body();
                if (filteredGroups == null){
                    filteredGroups = new ArrayList<Group>();
                }
                FilteredGroupAdapter adapter = new FilteredGroupAdapter(getApplicationContext(), 0, filteredGroups);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Group>> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });
    }

    private void initFilteredGroups(){
        Call<ArrayList<Group>> call = RetrofitClient.getInstance().getAPI().getFilteredGroups(subject, degree, year, day, time, language, participants, location);
        call.enqueue(new Callback<ArrayList<Group>>() {
            @Override
            public void onResponse(Call<ArrayList<Group>> call, Response<ArrayList<Group>> response) {
                filteredGroups = response.body();
                if (filteredGroups == null){
                    filteredGroups = new ArrayList<Group>();
                }
                FilteredGroupAdapter adapter = new FilteredGroupAdapter(getApplicationContext(), 0, filteredGroups);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Group>> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });
    }

    private void setUpOnclickListener() {
        listView = (ListView) findViewById(R.id.groupsListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Group selectedGroup = (Group) (listView.getItemAtPosition(position));
                Intent intent = new Intent(getApplicationContext(), AddGroupActivity.class);

                intent.putExtra("id",selectedGroup);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        if (parent.getId() == R.id.yearsSpinner) {
            year = parent.getItemAtPosition(position).toString();
        }
        if (parent.getId() == R.id.daysSpinner) {
            day = parent.getItemAtPosition(position).toString();
        }
        if (parent.getId() == R.id.timesSpinner) {
            time = parent.getItemAtPosition(position).toString();
        }
        if (parent.getId() == R.id.languagesSpinner) {
            language = parent.getItemAtPosition(position).toString();
        }
        if (parent.getId() == R.id.participantsSpinner) {
            participants = parent.getItemAtPosition(position).toString();
        }
        if (parent.getId() == R.id.locationsSpinner) {
            location = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}