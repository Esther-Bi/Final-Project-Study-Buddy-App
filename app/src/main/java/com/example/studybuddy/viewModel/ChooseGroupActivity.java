package com.example.studybuddy.viewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studybuddy.R;
import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.objects.Group;
import com.google.firebase.auth.FirebaseAuth;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseGroupActivity extends AppCompatActivity {
    Group currentGroup;
    TextView chosenSubject, chosenDegree, chosenYear, chosenDay, chosenTime, chosenLanguage,
        chosenMin, chosenMax, chosenCurrent, chosenAvailable, chosenLocation;
    Button back, join;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);

        Bundle bundle = getIntent().getExtras();
        currentGroup = (Group) bundle.getParcelable("chosen_group");

        chosenSubject = findViewById(R.id.chosen_subject);
        chosenDegree = findViewById(R.id.chosen_degree);
        chosenYear = findViewById(R.id.chosen_year);
        chosenDay = findViewById(R.id.chosen_day);
        chosenTime = findViewById(R.id.chosen_time);
        chosenLanguage = findViewById(R.id.chosen_language);
        chosenMin = findViewById(R.id.chosen_min_par);
        chosenMax = findViewById(R.id.chosen_max_par);
        chosenCurrent = findViewById(R.id.chosen_current_par);
        chosenAvailable = findViewById(R.id.chosen_available);
        chosenLocation = findViewById(R.id.chosen_location);
        back = findViewById(R.id.back_btn);
        join = findViewById(R.id.join_btn);

        chosenSubject.setText(currentGroup.getSubject());
        chosenDegree.setText(currentGroup.getDegree());
        chosenYear.setText(currentGroup.getYear());
        chosenDay.setText(currentGroup.getDay());
        chosenTime.setText(currentGroup.getTime());
        chosenLanguage.setText(currentGroup.getLanguage());
        chosenMin.setText(String.valueOf(currentGroup.getMin_participants()));
        chosenMax.setText(String.valueOf(currentGroup.getMax_participants()));
        chosenCurrent.setText(String.valueOf(currentGroup.getParticipants().size()));
        if (currentGroup.getMin_participants()<=currentGroup.getParticipants().size()){
            chosenAvailable.setText("true");
        } else {
            chosenAvailable.setText("false");
        }
        chosenLocation.setText(currentGroup.getLocation());

        back.setOnClickListener(v -> {
            super.finish();
        });

        join.setOnClickListener(v -> {
            Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().joinGroup(FirebaseAuth.getInstance().getCurrentUser().getUid(), currentGroup.getId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(ChooseGroupActivity.this, "You've got a new learning group! Go check it up (:", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChooseGroupActivity.this, GroupHomeActivity.class));
                        Log.d("done", "joining a group");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(ChooseGroupActivity.this, "failed to join the group", Toast.LENGTH_SHORT).show();
                    Log.d("Fail", t.getMessage());
                }
            });
        });

    }
}