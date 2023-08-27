package com.wasim.taskmaster.models;

import java.util.Date;

public class Task {
    String title;
    String body;
    State state;

    java.util.Date dateCreated;
    TaskCategoryEnum category;

//    @Ignore
    public Task(String title) {
        this.title = title;
    }

    public enum State {
        NEW, ASSIGNED, IN_PROGRESS, COMPLETE
    }

    //, Commented out State state
    //, TaskCategoryEnum category <-- add that back to constructor if you try to comment the enumCategoryClass
    public Task(String title, String body, java.util.Date dateCreated, State state) {
        this.title = title;
        this.body = body;
        this.dateCreated = dateCreated;
//        this.category = category;
                this.state = state;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public TaskCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(TaskCategoryEnum category) {
        this.category = category;
    }
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
