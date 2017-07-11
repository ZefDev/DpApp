package com.example.vadim.dpapp.containers;

/**
 * Created by Vadim on 28.05.2017.
 */
public class CompliteTaskContainer {
    private int id;
    private String date;
    private String compliteTask;
    private String time;
    private String codeTask;
    private String codeActiv;

    public CompliteTaskContainer(int id, String date, String compliteTask, String time, String codeTask, String codeActiv) {
        this.id = id;
        this.date = date;
        this.compliteTask = compliteTask;
        this.time = time;
        this.codeTask = codeTask;
        this.codeActiv = codeActiv;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompliteTask() {
        return compliteTask;
    }

    public void setCompliteTask(String compliteTask) {
        this.compliteTask = compliteTask;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCodeTask() {
        return codeTask;
    }

    public void setCodeTask(String codeTask) {
        this.codeTask = codeTask;
    }

    public String getCodeActiv() {
        return codeActiv;
    }

    public void setCodeActiv(String codeActiv) {
        this.codeActiv = codeActiv;
    }

    @Override
    public String toString() {
        return "CompliteTaskContainer{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", compliteTask='" + compliteTask + '\'' +
                ", time='" + time + '\'' +
                ", codeTask='" + codeTask + '\'' +
                ", codeActiv='" + codeActiv + '\'' +
                '}';
    }
}
