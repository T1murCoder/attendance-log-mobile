package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;

public class RequestEntity {

    @NonNull
    private final String id;

    @NonNull
    private final ItemStudentEntity student;

    public RequestEntity(@NonNull String id, @NonNull ItemStudentEntity student) {
        this.id = id;
        this.student = student;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public ItemStudentEntity getStudent() {
        return student;
    }
}
