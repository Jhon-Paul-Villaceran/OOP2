package com.studytracker.model;

import java.time.LocalTime;

public class Reminder{
    public int id;
    public int userID;
    public String subject;
    public LocalTime time;
    public String message;
    
    public Reminder(int id, int userID, String subject, LocalTime time, String message){
        this.id = id;
        this.userID = userID;
        this.subject = subject;
        this.time = time;
        this.message = message;
    }
}
