package ru.technosopher.attendancelogapp.data.dto;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class StudentItemDto {

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
    @SerializedName("username")
    public String username;
}
