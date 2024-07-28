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

    @NonNull
    private final String joinCode;

    public GroupEntity(@NonNull String name, @NonNull String id, @NonNull List<ItemStudentEntity> studentList, @NonNull String joinCode) {
        this.name = name;
        this.id = id;
        this.studentList = studentList;
        this.joinCode = joinCode;
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

    @NonNull
    public String getJoinCode() {
        return joinCode;
    }
}
