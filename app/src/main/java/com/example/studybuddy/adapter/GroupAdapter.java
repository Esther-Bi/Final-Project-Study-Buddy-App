package com.example.studybuddy.adapter;

        import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import java.util.List;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.studybuddy.R;
        import com.example.studybuddy.model.api.RetrofitClient;
        import com.example.studybuddy.objects.Group;
        import com.google.firebase.auth.FirebaseAuth;

        import okhttp3.ResponseBody;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import com.example.studybuddy.viewModel.GroupHomeActivity;


public class GroupAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<Group> items;
    GroupHomeActivity activity;

    public GroupAdapter(Context context, List<Group> items, GroupHomeActivity activity) {
        this.context = context;
        this.items = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.group_vieu,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, int position) {
        holder.subject.setText(items.get(position).getSubject());
        holder.day.setText(items.get(position).getDay());
        holder.time.setText(items.get(position).getTime());
        holder.delete_Button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                activity.popUpDelete(items.get(position).getId());
            }
        });
        holder.edit_Button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                activity.popUpEdit(items.get(position).getId());
            }
        });
        holder.whatsapp_Button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                activity.openWhatsapp(items.get(position).getLink());
            }
        });
        holder.participants_Button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                activity.popUpParticipants(items.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
