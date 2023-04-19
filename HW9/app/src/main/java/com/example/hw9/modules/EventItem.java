package com.example.hw9.modules;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventItem implements Serializable {
    String eventName;
    String venueName;
    String dateTime;
    String imgUrl;
    String id;
    String genre;
    String date;
    String time;

    public EventItem(String eventName, String venueName, String dateTime, String imgUrl, String id, String genre) {
        this.eventName = eventName;
        this.venueName = venueName;
        this.dateTime = dateTime;
        this.imgUrl = imgUrl;
        this.id = id;
        this.genre = genre;
        String [] dateTimeArr = dateTime.split("\n");
        this.date = dateTimeArr[0];
        this.time = "";
        if (this.dateTime.contains(":")) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US);
            try {
                @SuppressLint("SimpleDateFormat") Date timeValue = new SimpleDateFormat("HH:mm:ss").parse(dateTimeArr[1]);
                this.time = timeFormat.format(timeValue);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            @SuppressLint("SimpleDateFormat") Date dateValue = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            this.date = dateFormat.format(dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        System.out.println("date: " + this.date + " time: " + this.time);

    }

    public String getEventName() {
        return eventName;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
