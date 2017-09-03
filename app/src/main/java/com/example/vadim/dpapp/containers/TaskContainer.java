package com.example.vadim.dpapp.containers;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskContainer implements Serializable{
    private static final long serialVersionUID = -3158424480246390052L;

    private String code;
    private String taskName;
    private String contractor;
    private String date;
    private String executor;
    private String complite;
    private String isApproved;
    private String Waypoint;

    public String getNextExecutor() {
        return nextExecutor;
    }

    public void setNextExecutor(String nextExecutor) {
        this.nextExecutor = nextExecutor;
    }

    private String nextExecutor;

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getWaypoint() {
        return Waypoint;
    }

    public void setWaypoint(String waypoint) {
        Waypoint = waypoint;
    }

    public String getTypeTask() {
        return typeTask;
    }

    public void setTypeTask(String typeTask) {
        this.typeTask = typeTask;
    }

    private String typeTask;
    private ArrayList<OTaskContainer> otasks;

    public ArrayList<CompliteTaskContainer> getCompliteTask() {
        return compliteTask;
    }

    public void setCompliteTask(ArrayList<CompliteTaskContainer> compliteTask) {
        this.compliteTask = compliteTask;
    }

    private ArrayList<CompliteTaskContainer> compliteTask;

    public TaskContainer(String code, String taskName, String contractor, String date, String complite) {
        this.code = code;
        this.taskName = taskName;
        this.contractor = contractor;
        this.date = date;
        this.complite = complite;
    }
    public TaskContainer(String code, String taskName, String contractor, String date, String executor, String complite, ArrayList otasks,ArrayList compliteTask,String typeTask,String isApproved,String Waypoint,String nextExecutor) {
        this.code = code;
        this.taskName = taskName;
        this.contractor = contractor;
        this.date = date;
        this.executor = executor;
        this.complite = complite;
        this.otasks = otasks;
        this.compliteTask =compliteTask;
        this.typeTask = typeTask;
        this.isApproved = isApproved;
        this.Waypoint =Waypoint;
        this.nextExecutor = nextExecutor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getComplite() {
        return complite;
    }

    public void setComplite(String complite) {
        this.complite = complite;
    }

    public ArrayList<OTaskContainer> getOtasks() {
        return otasks;
    }

    public void setOtasks(ArrayList<OTaskContainer> otasks) {
        this.otasks = otasks;
    }
}

