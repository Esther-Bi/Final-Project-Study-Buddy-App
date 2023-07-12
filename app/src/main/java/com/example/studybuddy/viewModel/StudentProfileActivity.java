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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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


public class StudentProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    StudentProfileModel model = new StudentProfileModel(this);

    EditText name, age, phone_number;
    TextView degree;
    Spinner year;
    Button save;
    RadioGroup gender_group;
    RadioButton gender;
    ImageView gender_image, female_icon, male_icon;
    public ArrayAdapter<CharSequence> yearSpinnerAdapter;
    public String value = "second";


    Dialog dialogDegree;
    String degreeText = "Select Degree", yearText;

    GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("KEY");
        }

        googleSignInClient= model.googleSignInClient();

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        degree = findViewById(R.id.degree);
        year = findViewById(R.id.year);
        phone_number = findViewById(R.id.phone);
        save = findViewById(R.id.save);
        gender_group = findViewById(R.id.gender_group);
        gender_image = findViewById(R.id.gender_image);
        female_icon = findViewById(R.id.female_icon);
        male_icon = findViewById(R.id.male_icon);

        if (value.equals("second")){
            gender_group.setVisibility(View.GONE);
            gender_image.setVisibility(View.GONE);
        } else {
            female_icon.setVisibility(View.GONE);
            male_icon.setVisibility(View.GONE);
        }

        ArrayAdapter<CharSequence> degreesSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.degrees, android.R.layout.simple_spinner_item);
        year.setOnItemSelectedListener(this);
        yearSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
        yearSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(yearSpinnerAdapter);

        degree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDegree = new Dialog(StudentProfileActivity.this);
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
            String textGender = "gender";
            if (value.equals("first")){
                int radioID = gender_group.getCheckedRadioButtonId();
                if (radioID == -1) {
                    Toast.makeText(StudentProfileActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                } else {
                    gender = findViewById(radioID);
                    textGender = gender.getText().toString();
                }
            }
            String textAge = age.getText().toString();
            String textName = name.getText().toString();
            String textDegree = degree.getText().toString();
            String textYear = yearText;
            String textPhone = phone_number.getText().toString();

            if (TextUtils.isEmpty(textName) || TextUtils.isEmpty(textDegree) || TextUtils.isEmpty(textYear) || TextUtils.isEmpty(textGender) || TextUtils.isEmpty(textAge) || TextUtils.isEmpty(textPhone)) {
                Toast.makeText(StudentProfileActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
            }else if (textPhone.length() != 9){
                Toast.makeText(StudentProfileActivity.this, "phone number is illegal", Toast.LENGTH_SHORT).show();
            } else {
                updateProfile(textName, textYear, textDegree, textGender, textAge, textPhone);
                startActivity(new Intent(this, StudentHomeActivity.class));
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        model.modelOnStart(name, age, year, degree, phone_number, female_icon, male_icon, StudentProfileActivity.this);
    }

    public void updateProfile(String textName, String textYear, String textDegree, String textGender, String textAge, String textPhone) {

        model.updateProfileModel(textName, textYear, textDegree, textGender, textAge, textPhone);
        startActivity(new Intent(StudentProfileActivity.this, MainActivity.class));
        finish();
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
