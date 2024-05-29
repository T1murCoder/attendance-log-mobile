package ru.technosopher.attendancelogapp.data.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.GregorianCalendar;

public class AttendanceDto {

    @Nullable
    @SerializedName("id")
    public String id;

    @Nullable
    @SerializedName("isVisited")
    public Boolean isVisited;

    @Nullable
    @SerializedName("studentId")
    public String studentId;

    @Nullable
    @SerializedName("lessonId")
    public String lessonId;

    @Nullable
    @SerializedName("lessonTimeStart")
    public GregorianCalendar lessonTimeStart;

    @Nullable
    @SerializedName("points")
    public String points;

}
