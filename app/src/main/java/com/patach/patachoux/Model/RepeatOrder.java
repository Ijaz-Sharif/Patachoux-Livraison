package com.patach.patachoux.Model;

public class RepeatOrder {


    boolean status;
    String time,day;

    public RepeatOrder(String day,boolean status, String time) {
        this.status = status;
        this.time = time;
        this.day=day;
    }

    public String getDay() {
        return day;
    }

    public boolean isStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }
}
