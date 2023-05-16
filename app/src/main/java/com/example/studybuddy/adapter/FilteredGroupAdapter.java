package com.example.studybuddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.studybuddy.R;
import com.example.studybuddy.objects.Group;
import com.example.studybuddy.objects.Teacher;

import java.util.List;

public class FilteredGroupAdapter extends ArrayAdapter<Group> {
    public FilteredGroupAdapter(Context context, int resource, List<Group> groupsList) {
        super(context,resource,groupsList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Group group = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_group_item, parent, false);
        }

        TextView subject = (TextView) convertView.findViewById(R.id.subject);
        subject.setText(group.getSubject());
        TextView day = (TextView) convertView.findViewById(R.id.day);
        day.setText(group.getDay());
        TextView degree = (TextView) convertView.findViewById(R.id.degree);
        degree.setText(group.getDegree());
        TextView time = (TextView) convertView.findViewById(R.id.time);
        time.setText(group.getTime());

        return convertView;
    }
}
