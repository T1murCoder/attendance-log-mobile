package ru.technosopher.attendancelogapp.data.dto;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentDto {
    @Nullable
    @SerializedName("id")
    public String id;

    @Nullable
    @SerializedName("id")
    public String name;

    @Nullable
    @SerializedName("id")
    public String surname;

    @Nullable
    @SerializedName("points")
    public String points;

    @Nullable
    @SerializedName("attendanceDtoList")
    public List<AttendanceDto> attendanceDtoList;

}
