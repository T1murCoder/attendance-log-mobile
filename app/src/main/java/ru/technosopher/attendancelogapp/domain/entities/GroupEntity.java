package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;

import java.util.List;

public class GroupEntity {
    @NonNull
    private final String name;

    @NonNull
    private final String id;

    @NonNull
    private final List<ItemStudentEntity> studentList;

    public GroupEntity(@NonNull String name, @NonNull String id, @NonNull List<ItemStudentEntity> studentList) {
        this.name = name;
        this.id = id;
        this.studentList = studentList;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public List<ItemStudentEntity> getStudentList() {
        return studentList;
    }
}
