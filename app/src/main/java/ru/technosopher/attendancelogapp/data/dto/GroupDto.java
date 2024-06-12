package ru.technosopher.attendancelogapp.data.dto;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupDto {

    @Nullable
    @SerializedName("id")
    public String id;
    @Nullable
    @SerializedName("name")
    public String name;
    @Nullable
    @SerializedName("studentList")
    public List<StudentItemDto> studentList;

    public GroupDto(@Nullable String id, @Nullable String name, @Nullable List<StudentItemDto> studentList) {
        this.name = name;
        this.id = id;
        this.studentList = studentList;
    }
}
