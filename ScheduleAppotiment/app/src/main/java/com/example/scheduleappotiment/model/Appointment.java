package com.example.scheduleappotiment.model;

public class Appointment {

    private String title;
    private String description;
    private long dateTime;
    private String id;
    private int status;

    public Appointment() {
    }

    public Appointment(String title, String description, long dateTime, String id,int status) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.id = id;
        this.status=status;
    }

    public Appointment(String title,long dateTime,String id,int status){
        this(title,null,dateTime,id,status);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                ", id='" + id + '\'' +
                ", status=" + status +
                '}';
    }
}
