package com.sembozdemir.kagnkald;

import android.util.Log;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Created by Semih Bozdemir on 22.2.2015.
 */
public class MyDate implements Serializable {
    private static final long serialVersionUID = 1L;

    private DateTime dateTime;

    public MyDate(String dateString) {
        // dd.mm.yyyy : 01.34.6789
        String[] tokens = dateString.split(Pattern.quote("."));
        Log.d("MyDate", "tokens: " + tokens[0] + "/" + tokens[1] + "/" + tokens[2]);
        int day = Integer.parseInt(tokens[0]);
        int month = Integer.parseInt(tokens[1]);
        int year = Integer.parseInt(tokens[2]);

        dateTime = new DateTime(year,month,day,0,0,0);
        Log.d("MyDate", "String Format control: " + formatDate());
    }

    public MyDate(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public MyDate(int year, int month, int day) {
        dateTime = new DateTime(year,month,day,0,0,0);
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDateTime(int year, int month, int day) {
        dateTime = new DateTime(year, month, day, 0, 0, 0);
    }

    public void setDateTime(String dateString) {
        String[] tokens = dateString.split(Pattern.quote("."));
        Log.d("MyDate", "tokens: " + tokens[0] + "/" + tokens[1] + "/" + tokens[2]);
        int day = Integer.parseInt(tokens[0]);
        int month = Integer.parseInt(tokens[1]);
        int year = Integer.parseInt(tokens[2]);

        dateTime = new DateTime(year,month,day,0,0,0);
        Log.d("MyDate", "String Format control: " + formatDate());
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public int getDay() {
        return dateTime.getDayOfMonth();
    }

    public int getMonth() {
        return dateTime.getMonthOfYear();
    }

    public int getYear() {
        return dateTime.getYear();
    }

    @Override
    public String toString() {
        return formatDate();
    }

    public String formatDate() {
        // Ex: "30.12.2015"
        StringBuilder sb = new StringBuilder();
        return sb.append(getDay()).append(".").append(getMonth()).append(".").append(getYear()).toString();
    }

    public String formatDateTR() {
        // Aylar: Ocak, Şubat, Mart şeklinde yazsın.
        StringBuilder sb = new StringBuilder();
        return sb.append(getDay()).append(" ").append(MyDateConstants.MONTHS_TR[getMonth()]).append(" ").append(getYear()).toString();
    }
}
