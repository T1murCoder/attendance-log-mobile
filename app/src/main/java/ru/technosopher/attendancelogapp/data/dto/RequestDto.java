package ru.technosopher.attendancelogapp.data.dto;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class RequestDto {
    @Nullable
    @SerializedName("id")
    public String id;

    @Nullable
    @SerializedName("student")
    public StudentDto student;

}
