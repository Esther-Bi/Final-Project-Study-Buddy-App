package com.example.studybuddy.objects;

public class Group {

    private boolean available;
    private String day;
    private String degree;
    private String language;
    private String location;
    private int max_participants;
    private int min_participants;
    private String subject;
    private String time;
    private int year;
    private String[] participants;
    private String id;

    public Group (String day, String degree, String language, String location, int max_participants, int min_participants, String subject, String time, int year, String id){
        this.available = true;
        this.day = day;
        this.degree = degree;
        this.language = language;
        this.location = location;
        this.max_participants = max_participants;
        this.min_participants = min_participants;
        this.subject = subject;
        this.time = time;
        this.year = year;
        this.id = id;
    }

    public boolean isAvailable() {
        return available;
    }

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

    public int getYear() {
        return year;
    }

    public String getId() {
        return id;
    }

    public String[] getParticipants() {
        return participants;
    }
}
