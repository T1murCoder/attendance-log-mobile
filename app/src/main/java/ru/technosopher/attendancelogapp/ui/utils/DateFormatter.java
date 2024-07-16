package ru.technosopher.attendancelogapp.ui.utils;

import android.annotation.SuppressLint;
import android.text.format.Time;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateFormatter {
    public static String getDateStringFromDate(GregorianCalendar date, String pat) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(pat);
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date.getTime());
    }
    public static String getFullTimeStringFromDate(GregorianCalendar start, GregorianCalendar end, String pat) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(pat);
        df.setTimeZone(TimeZone.getDefault());
        return df.format(start.getTime()) + " - " + df.format(end.getTime());
    }
    public static int getActualYear() {
        return LocalDate.now().getYear();
    }
    public static int getActualMonth() {
        return LocalDate.now().getMonthValue() - 1;
    }
    public static int getActualDay() {
        return LocalDate.now().getDayOfMonth();
    }
}