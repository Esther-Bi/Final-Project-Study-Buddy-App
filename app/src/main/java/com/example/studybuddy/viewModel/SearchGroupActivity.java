package com.example.studybuddy.viewModel;

import androidx.annotation.NonNull;
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
import com.example.studybuddy.adapter.FilteredGroupAdapter;
import com.example.studybuddy.adapter.TeacherAdapter;
import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.objects.Teacher;
import com.example.studybuddy.objects.Group;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

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
    TextView find_text;
    Button open_new_group;

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
        open_new_group = findViewById(R.id.open_new_group);
        open_new_group.setVisibility(View.INVISIBLE);
        find_text = findViewById(R.id.find_text);
        find_text.setVisibility(View.INVISIBLE);

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
            open_new_group.setVisibility(View.INVISIBLE);
            find_text.setVisibility(View.INVISIBLE);
            initFilteredGroups();
        });

        open_new_group.setOnClickListener(v -> {
            startActivity(new Intent(SearchGroupActivity.this, AddGroupActivity.class));
        });

        setUpOnclickListener();
    }

    private void setupData() {
        Call<ArrayList<Group>> call = RetrofitClient.getInstance().getAPI().getAllGroups(FirebaseAuth.getInstance().getCurrentUser().getUid());
        call.enqueue(new Callback<ArrayList<Group>>() {
            @Override
            public void onResponse(Call<ArrayList<Group>> call, Response<ArrayList<Group>> response) {
                filteredGroups = response.body();
                if (filteredGroups == null){
                    filteredGroups = new ArrayList<Group>();
                    find_text.setVisibility(View.VISIBLE);
                    open_new_group.setVisibility(View.VISIBLE);
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
        Call<ArrayList<Group>> call = RetrofitClient.getInstance().getAPI().getFilteredGroups(FirebaseAuth.getInstance().getCurrentUser().getUid(), subject, degree, year, day, time, language, participants, location);
        call.enqueue(new Callback<ArrayList<Group>>() {
            @Override
            public void onResponse(Call<ArrayList<Group>> call, Response<ArrayList<Group>> response) {
                filteredGroups = response.body();
                if (filteredGroups == null){
                    filteredGroups = new ArrayList<Group>();
                    find_text.setVisibility(View.VISIBLE);
                    open_new_group.setVisibility(View.VISIBLE);
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
                Intent intent = new Intent(getApplicationContext(), ChooseGroupActivity.class);

                intent.putExtra("chosen_group",selectedGroup);
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
                startActivity(new Intent(SearchGroupActivity.this, GroupHomeActivity.class));
                return true;
            case R.id.action_edit_profile:
                startActivity(new Intent(SearchGroupActivity.this, GroupProfileActivity.class));
                finish();
                return true;
            case R.id.action_search_group:
                startActivity(new Intent(SearchGroupActivity.this, SearchGroupActivity.class));
                finish();
                return true;
            case R.id.action_open_group:
                startActivity(new Intent(SearchGroupActivity.this, AddGroupActivity.class));
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
                            startActivity(new Intent(SearchGroupActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            case R.id.action_change_user_type:
                startActivity(new Intent(SearchGroupActivity.this, ChooseUserActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}