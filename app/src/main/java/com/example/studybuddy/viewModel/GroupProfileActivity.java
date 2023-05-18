package com.example.studybuddy.viewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studybuddy.R;
import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.objects.Student;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GroupProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText name, age, phone_number;
    TextView degree;
    Spinner year;
    Button save;
    RadioGroup gender_group;
    RadioButton gender;

    Dialog dialogDegree;
    String degreeText = "Select Degree", yearText;

    GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);

        googleSignInClient= GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        degree = findViewById(R.id.degree);
        year = findViewById(R.id.year);
        phone_number = findViewById(R.id.phone);
        save = findViewById(R.id.save);
        gender_group = findViewById(R.id.gender_group);

        ArrayAdapter<CharSequence> degreesSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.degrees, android.R.layout.simple_spinner_item);
        year.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> yearSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
        yearSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(yearSpinnerAdapter);

        degree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDegree = new Dialog(GroupProfileActivity.this);
                dialogDegree.setContentView(R.layout.dialog_searchable_spinner);
                dialogDegree.getWindow().setLayout(800,1000);
                dialogDegree.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDegree.show();

                EditText editText = dialogDegree.findViewById(R.id.search_subject_degree);
                ListView listView = dialogDegree.findViewById(R.id.search_subject_degree_list_view);

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
                        degree.setText(degreesSpinnerAdapter.getItem(position));
                        degreeText = degreesSpinnerAdapter.getItem(position).toString();
                        dialogDegree.dismiss();
                    }
                });
            }
        });

        //onclick listener for updating profile button
        save.setOnClickListener(v -> {
            //Converting fields to text
            int radioID = gender_group.getCheckedRadioButtonId();
            if (radioID == -1) {
                Toast.makeText(GroupProfileActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
            } else {
                gender = findViewById(radioID);
                String textGender = gender.getText().toString();
                String textAge = age.getText().toString();
                String textName = name.getText().toString();
                String textDegree = degree.getText().toString();
                String textYear = yearText;
                String textPhone = phone_number.getText().toString();

                if (TextUtils.isEmpty(textName) || TextUtils.isEmpty(textDegree) || TextUtils.isEmpty(textYear) || TextUtils.isEmpty(textGender) || TextUtils.isEmpty(textAge) || TextUtils.isEmpty(textPhone)) {
                    Toast.makeText(GroupProfileActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                }else if (textPhone.length() != 9){
                    Toast.makeText(GroupProfileActivity.this, "phone number is illegal", Toast.LENGTH_SHORT).show();
                } else {
                    updateProfile(textName, textYear, textDegree, textGender, textAge, textPhone);
                    startActivity(new Intent(this, GroupHomeActivity.class));
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        Call<Student> call = RetrofitClient.getInstance().getAPI().getStudentDetails(FirebaseAuth.getInstance().getCurrentUser().getUid());
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
                //year.setText(yearResult);
                degree.setText(degreeResult);
                phone_number.setText(phoneResult);
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Log.d("Fail", t.getMessage());
                Toast.makeText(GroupProfileActivity.this, "no profile yet" , Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void updateProfile(String textName, String textYear, String textDegree, String textGender, String textAge, String textPhone) {
        assert FirebaseAuth.getInstance().getCurrentUser() != null;

        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().updateStudentDetails(FirebaseAuth.getInstance().getCurrentUser().getUid(), textName, textYear, textDegree, textGender, textAge, textPhone);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("done", "done");
                    Toast.makeText(GroupProfileActivity.this, "update profile successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Fail", t.getMessage());
                Toast.makeText(GroupProfileActivity.this, "error in updating profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        if (parent.getId() == R.id.year) {
            yearText = parent.getItemAtPosition(position).toString();
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
                startActivity(new Intent(GroupProfileActivity.this, GroupHomeActivity.class));
                return true;
            case R.id.action_edit_profile:
                startActivity(new Intent(GroupProfileActivity.this, GroupProfileActivity.class));
                finish();
                return true;
            case R.id.action_search_group:
                startActivity(new Intent(GroupProfileActivity.this, SearchGroupActivity.class));
                finish();
                return true;
            case R.id.action_open_group:
                startActivity(new Intent(GroupProfileActivity.this, AddGroupActivity.class));
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
                            startActivity(new Intent(GroupProfileActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            case R.id.action_change_user_type:
                startActivity(new Intent(GroupProfileActivity.this, ChooseUserActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
