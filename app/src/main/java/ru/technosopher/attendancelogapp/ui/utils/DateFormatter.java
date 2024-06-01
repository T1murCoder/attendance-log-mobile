package ru.technosopher.attendancelogapp.ui.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateFormatter {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static String getDateStringFromDate(GregorianCalendar date, String pat){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(pat);
        return df.format(date.getTime());
    }
    public static String getFullTimeStringFromDate(GregorianCalendar start, GregorianCalendar end, String pat){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(pat);
        return df.format(start.getTime()) + " - " + df.format(end.getTime());
    }
    public static int getYearFromDate(GregorianCalendar date) {
        return date.get(Calendar.YEAR);
    }

    public static int getMonthFromDate(GregorianCalendar date) {
        return date.get(Calendar.MONTH);
    }

    public static int getDayFromDate(GregorianCalendar date) {
        return date.get(Calendar.DAY_OF_MONTH);
    }

    public static String getTimeFromDate(GregorianCalendar date) {
        return date.get(Calendar.HOUR) + ":" + date.get(Calendar.MINUTE);
    }

    public static int getActualYear() {
        return LocalDate.now().getYear();
    }

    public static int getActualMonth() {
        return  LocalDate.now().getMonthValue() - 1;
    }

    public static int getActualDay() {
        return LocalDate.now().getDayOfMonth();
    }

    public static GregorianCalendar setDate(int year, int month, int day, int hour, int minute) {
        return new GregorianCalendar(year, month, day, hour, minute);
    }
}
