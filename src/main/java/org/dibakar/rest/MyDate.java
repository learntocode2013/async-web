package org.dibakar.rest;

public class MyDate {
    private int day ;
    private int month ;
    private int year ;

    public int getDay() {
        return day;
    }

    public MyDate setDay(int day) {
        this.day = day;
        return this ;
    }

    public int getMonth() {
        return month;
    }

    public MyDate setMonth(int month) {
        this.month = month;
        return this ;
    }

    public int getYear() {
        return year;
    }

    public MyDate setYear(int year) {
        this.year = year;
        return this ;
    }

    @Override
    public String toString() {
        return "MyDate{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}
