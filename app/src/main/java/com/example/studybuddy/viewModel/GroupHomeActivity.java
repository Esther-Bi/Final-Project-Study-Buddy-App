package com.example.studybuddy.viewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.example.studybuddy.R;
import com.example.studybuddy.adapter.GroupAdapter;
import com.example.studybuddy.model.api.RetrofitClient;
import com.example.studybuddy.objects.Group;
import com.example.studybuddy.objects.Student;
import com.example.studybuddy.objects.Teacher;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupHomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    GoogleSignInClient googleSignInClient;

    public GroupAdapter adapter;
    RecyclerView recyclerView;// = findViewById(R.id.recyclerview);

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    String day, time, location;


    Spinner daysSpinner;// = findViewById(R.id.daysSpinner);
    Spinner timesSpinner;// = findViewById(R.id.timesSpinner);
    Spinner locationsSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        googleSignInClient = GoogleSignIn.getClient(GroupHomeActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        setContentView(R.layout.activity_group_home);

        recyclerView = findViewById(R.id.recyclerview);
        this.refresh();

    }

    public void refresh(){

        Call<ArrayList<Group>> call = RetrofitClient.getInstance().getAPI().getMyGroups(FirebaseAuth.getInstance().getCurrentUser().getUid());
        call.enqueue(new Callback<ArrayList<Group>>() {
            @Override
            public void onResponse(Call<ArrayList<Group>> call, Response<ArrayList<Group>> response) {
                ArrayList<Group> myGroups = response.body();
                if (myGroups == null){
                    myGroups = new ArrayList<Group>();
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(GroupHomeActivity.this));
                adapter = new GroupAdapter(getApplicationContext(),myGroups, GroupHomeActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Group>> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });

    }

    public void popUpDelete(String groupId){

        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.delete_question_popup, null);

        TextView question = popupView.findViewById(R.id.question);
        Button yes_button = popupView.findViewById(R.id.yes_button);
        Button no_button = popupView.findViewById(R.id.no_button);
        question.setText("Are you sure you want to leave this group?'\n");

        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Log.d("want to quit", "delete group");
                Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().deleteFromGroup(FirebaseAuth.getInstance().getCurrentUser().getUid(), groupId);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Log.d("done", "done");
                            refresh();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("Fail", t.getMessage());
                    }
                });
            }
        });
        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("regret quit", "dont want to quit this group");
                dialog.dismiss();
            }
        });
    }

    public void popUpEdit(String groupId){

        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.edit_group_popup, null);

        Button edit_group_save = popupView.findViewById(R.id.edit_group_save);
        Button edit_group_cancel = popupView.findViewById(R.id.edit_group_cancel);
        daysSpinner = popupView.findViewById(R.id.daysSpinner);
        timesSpinner = popupView.findViewById(R.id.timesSpinner);
        locationsSpinner = popupView.findViewById(R.id.locationsSpinner);

        daysSpinner.setOnItemSelectedListener(this);
        timesSpinner.setOnItemSelectedListener(this);
        locationsSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> daySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.days, android.R.layout.simple_spinner_item);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysSpinner.setAdapter(daySpinnerAdapter);

        ArrayAdapter<CharSequence> timeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.times, android.R.layout.simple_spinner_item);
        timeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timesSpinner.setAdapter(timeSpinnerAdapter);

        ArrayAdapter<CharSequence> locationSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.locations, android.R.layout.simple_spinner_item);
        locationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationsSpinner.setAdapter(locationSpinnerAdapter);

        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        edit_group_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Log.d("want to quit", "delete group");
                Call<ResponseBody> call = RetrofitClient.getInstance().getAPI().editGroupTime(groupId, day, time, location);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Log.d("done", "done");
                            refresh();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("Fail", t.getMessage());
                    }
                });
            }
        });
        edit_group_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("regret quit", "dont want to quit this group");
                dialog.dismiss();
            }
        });
    }

    private boolean appInstalledOrNot(String url){
        boolean app_installed;
        try{
            this.getPackageManager().getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void openWhatsapp(String link){
        boolean installed = appInstalledOrNot("com.whatsapp");
        if (installed){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(browserIntent);
            finish();
        } else {
            Toast.makeText(this.getApplicationContext(), "whatsApp not installed on this device", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPrivateWhatsApp(String mobile_number){
        boolean installed = appInstalledOrNot("com.whatsapp");
        if (installed){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData ( Uri.parse ( "https://wa.me/" + "+972" + mobile_number + "/?text=" + "Hi! I'm from StudyBuddy app (:" ) );
            GroupHomeActivity.this.startActivity(intent);
        } else {
            Toast.makeText(GroupHomeActivity.this.getApplicationContext(), "whatsApp not installed on this device", Toast.LENGTH_SHORT).show();
        }
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
                startActivity(new Intent(GroupHomeActivity.this, GroupHomeActivity.class));
                return true;
            case R.id.action_edit_profile:
                startActivity(new Intent(GroupHomeActivity.this, GroupProfileActivity.class));
                finish();
                return true;
            case R.id.action_search_group:
                startActivity(new Intent(GroupHomeActivity.this, SearchGroupActivity.class));
                finish();
                return true;
            case R.id.action_open_group:
                startActivity(new Intent(GroupHomeActivity.this, AddGroupActivity.class));
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
                            startActivity(new Intent(GroupHomeActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            case R.id.action_change_user_type:
                startActivity(new Intent(GroupHomeActivity.this, ChooseUserActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.daysSpinner) {
            day = adapterView.getItemAtPosition(i).toString();
        }
        if (adapterView.getId() == R.id.timesSpinner) {
            time = adapterView.getItemAtPosition(i).toString();
        }
        if (adapterView.getId() == R.id.locationsSpinner) {
            location = adapterView.getItemAtPosition(i).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void popUpParticipants(String groupId) {
        Dialog dialog;
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.participants_popup);
        dialog.getWindow().setLayout(800,1500);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        EditText editText = dialog.findViewById(R.id.search_participant);
        ListView listView = dialog.findViewById(R.id.search_participant_list_view);
        ImageButton close = dialog.findViewById(R.id.close_Button);

        ArrayList<String> names = new ArrayList<>();
        HashMap<String,String> name_mobile = new HashMap<>();

        Call<ArrayList<Student>> call = RetrofitClient.getInstance().getAPI().getGroupParticipants(FirebaseAuth.getInstance().getCurrentUser().getUid(), groupId);
        call.enqueue(new Callback<ArrayList<Student>>() {
            @Override
            public void onResponse(Call<ArrayList<Student>> call, Response<ArrayList<Student>> response) {
                ArrayList<Student> participants = response.body();
                names.add("You");
                if (participants == null){
                    participants = new ArrayList<Student>();
                }
                for (int i=0 ; i<participants.size() ; i++){
                    String name = participants.get(i).getName();
                    names.add(name);
                    name_mobile.put(name,participants.get(i).getPhone());
                }
                ArrayAdapter<String> participantsAdapter = new ArrayAdapter<String>(GroupHomeActivity.this,
                        android.R.layout.simple_list_item_1,
                        names);

                listView.setAdapter(participantsAdapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        participantsAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name = participantsAdapter.getItem(position).toString();
                        //Toast.makeText(GroupHomeActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                        //participants.get(position).getPhone();
                        if (!name.equals("You")){
                            String mobile = name_mobile.get(name);
                            openPrivateWhatsApp(mobile);
                            dialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<Student>> call, Throwable t) {
                Log.d("Fail", t.getMessage());
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}