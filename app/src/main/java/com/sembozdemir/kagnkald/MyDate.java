package com.sembozdemir.kagnkald;

import org.joda.time.DateTime;

/**
 * Created by Semih Bozdemir on 22.2.2015.
 */
public class MyDate {
    private int day;
    private int month;
    private int year;
    private DateTime date;

    public MyDate() {
        // to avoid null pointer exception
        day = 1;
        month  = 1;
        year = 2015;

        date = new DateTime(year,month,day,0,0,0);
    }

    public MyDate(DateTime date) {
        this.date = date;

        day = date.getDayOfMonth();
        month = date.getMonthOfYear();
        year = date.getYear();
    }

    public MyDate(int year, int month, int day) {
        this.day = day;
        this.month = month;
        this.year = year;

        date = new DateTime(year,month,day,0,0,0);
    }

    public void setDate(DateTime date) {
        this.date = date;

        day = date.getDayOfMonth();
        month = date.getMonthOfYear();
        year = date.getYear();
    }

    public void setDate(int year, int month , int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        date = new DateTime(year,month,day,0,0,0);
    }

    public DateTime getDate() {
        return date;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String formatDate() {
        StringBuilder sb = new StringBuilder();
        return sb.append(day).append(".").append(month).append(".").append(year).toString();
    }

    public String formatDateTR() {
        // Aylar: Ocak, Şubat, Mart şeklinde yazsın.
        StringBuilder sb = new StringBuilder();
        return sb.append(day).append(" ").append(MyDateConstants.MONTHS_TR[month]).append(" ").append(year).toString();
    }
}
