package com.example.studybuddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studybuddy.R;
import com.example.studybuddy.objects.Teacher;

import java.util.List;

public class TeacherAdapter extends ArrayAdapter<Teacher> {

    private String course;

    public TeacherAdapter(Context context, int resource, List<Teacher> teacherList, String course) {
        super(context,resource,teacherList);
        this.course = course;
    }

    public TeacherAdapter(Context context, int resource, List<Teacher> teacherList) {
        super(context,resource,teacherList);
        this.course = "choose";
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Teacher teacher = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_teacher, parent, false);
        }
        TextView teacherName = (TextView) convertView.findViewById(R.id.teacherName);
        teacherName.setText(teacher.getName());
        TextView degree = (TextView) convertView.findViewById(R.id.degree);
        degree.setText(teacher.getDegree());
        TextView price = (TextView) convertView.findViewById(R.id.price);
        if (this.course.equals("choose")){
            price.setText("--- ₪");
        } else{
            int index = teacher.getCourses().indexOf(this.course);
            price.setText(teacher.getPrices().get(index) + "₪");
        }
        RatingBar rating = (RatingBar) convertView.findViewById(R.id.rating);
        rating.setRating((float) teacher.getRating());

        return convertView;
    }
}