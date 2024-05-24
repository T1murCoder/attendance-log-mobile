package ru.technosopher.attendancelogapp.data.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class AttendanceDto {

    @Nullable
    @SerializedName("id")
    public String id;

    @Nullable
    @SerializedName("isVisited")
    public Boolean isVisited;

    @Nullable
    @SerializedName("id")
    public String studentId;

    @Nullable
    @SerializedName("id")
    public String lessonId;

}
