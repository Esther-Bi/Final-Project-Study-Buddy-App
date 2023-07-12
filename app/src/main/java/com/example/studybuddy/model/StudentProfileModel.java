package com.example.studybuddy.model;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.objects.Student;
import com.example.studybuddy.viewModel.StudentProfileActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class StudentProfileModel {


    private Activity activity;
    private FirebaseUser user;


    public StudentProfileModel (Activity activity){
        this.activity = activity;
        this.user  = FirebaseAuth.getInstance().getCurrentUser();
    }

    public GoogleSignInClient googleSignInClient() {
        return GoogleSignIn.getClient(this.activity, GoogleSignInOptions.DEFAULT_SIGN_IN);
    }

    public void modelOnStart(EditText name, EditText age, Spinner year, TextView degree, EditText phone_number, ImageView female_icon, ImageView male_icon, StudentProfileActivity thisactivity) {

        Call<Student> call = RetrofitClient.getInstance().getAPI().getStudentDetails(user.getUid());
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
                int index = thisactivity.yearSpinnerAdapter.getPosition(yearResult);
                year.setSelection(index);
                degree.setText(degreeResult);
                phone_number.setText(phoneResult);
                if (thisactivity.value.equals("second") && curr_student.getGender().equals("Male")) {
                    female_icon.setVisibility(View.INVISIBLE);
                }
                if (thisactivity.value.equals("second") && curr_student.getGender().equals("Female")) {
                    male_icon.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Log.d("Fail", t.getMessage());
                Toast.makeText(thisactivity, "no profile yet" , Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void updateProfileModel(String textName, String textYear, String textDegree, String textGender, String textAge, String textPhone) {

        assert user != null;

        Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().updateStudentDetails(user.getUid(), textName, textYear, textDegree, textGender, textAge, textPhone);
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

    }

}
