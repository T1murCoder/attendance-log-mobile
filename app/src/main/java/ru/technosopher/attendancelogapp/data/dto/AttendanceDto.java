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
    @SerializedName("studentId")
    public String studentId;

    @Nullable
    @SerializedName("lessonId")
    public String lessonId;
    @Nullable
    @SerializedName("isVisited")
    public Boolean isVisited;

    @Nullable
    @SerializedName("points")
    public String points;
    @Nullable
    @SerializedName("lessonTimeStart")
    public GregorianCalendar lessonTimeStart;

    public AttendanceDto(@Nullable String id, @Nullable String studentId, @Nullable String lessonId, @Nullable Boolean isVisited, @Nullable String points, @Nullable GregorianCalendar lessonTimeStart) {
        this.id = id;
        this.studentId = studentId;
        this.lessonId = lessonId;
        this.isVisited = isVisited;
        this.points = points;
        this.lessonTimeStart = lessonTimeStart;
    }
}
