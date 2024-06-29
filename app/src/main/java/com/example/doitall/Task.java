package com.example.doitall;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    private long id;
    private String taskText;
    private int priority;
    private String dueDate;
    private boolean completed;

    public Task() {
    }

    public Task(String taskText,int priority, boolean completed) {
        this.taskText = taskText;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }


    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
