package ru.technosopher.attendancelogapp.data.dto;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentWithAttendances {

    @Nullable
    @SerializedName("id")
    public String id;

    @Nullable
    @SerializedName("name")
    public String name;

    @Nullable
    @SerializedName("surname")
    public String surname;

    @Nullable
    @SerializedName("points")
    public String points;

    @Nullable
    @SerializedName("attendanceDtoList")
    public List<AttendanceDto> attendanceDtoList;

}
