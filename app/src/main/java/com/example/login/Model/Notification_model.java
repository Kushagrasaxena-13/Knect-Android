package com.example.login.Model;

public class Notification_model {

    private String who;
    private String what;

    public Notification_model() {
    }

    public Notification_model(String who, String what) {
        this.who = who;
        this.what = what;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }
}
