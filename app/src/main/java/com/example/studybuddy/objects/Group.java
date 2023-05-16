package com.example.studybuddy.objects;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Group implements Parcelable {

//    private boolean available;
    private String day;
    private String degree;
    private String language;
    private String location;
    private int max_participants;
    private int min_participants;
    private String subject;
    private String time;
    private String year;
    private List<String> participants = new ArrayList<String>();
    private String id;

    public Group (String day, String degree, String language, String location, int max_participants, int min_participants, String subject, String time, String year, List<String> participants, String id){
//        this.available = true;
        this.day = day;
        this.degree = degree;
        this.language = language;
        this.location = location;
        this.max_participants = max_participants;
        this.min_participants = min_participants;
        this.subject = subject;
        this.time = time;
        this.year = year;
        this.participants = participants;
        this.id = id;
    }

    protected Group(Parcel in) {
//        available = in.readByte() != 0;
        day = in.readString();
        degree = in.readString();
        language = in.readString();
        location = in.readString();
        max_participants = in.readInt();
        min_participants = in.readInt();
        subject = in.readString();
        time = in.readString();
        year = in.readString();
        participants = in.readArrayList(null);
        id = in.readString();
    }

//    public boolean isAvailable() {
//        return available;    }

    public String getDay() {
        return day;
    }

    public String getDegree() {
        return degree;
    }

    public String getLanguage() {
        return language;
    }

    public String getLocation() {
        return location;
    }

    public int getMax_participants() {
        return max_participants;
    }

    public int getMin_participants() {
        return min_participants;
    }

    public String getSubject() {
        return subject;
    }

    public String getTime() {
        return time;
    }

    public String getYear() {
        return year;
    }

    public String getId() {
        return id;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //        dest.writeByte((byte) (available ? 1 : 0));
        dest.writeString(day);
        dest.writeString(degree);
        dest.writeString(language);
        dest.writeString(location);
        dest.writeInt(max_participants);
        dest.writeInt(min_participants);
        dest.writeString(subject);
        dest.writeString(time);
        dest.writeString(year);
        dest.writeList(participants);
        dest.writeString(id);
    }
}