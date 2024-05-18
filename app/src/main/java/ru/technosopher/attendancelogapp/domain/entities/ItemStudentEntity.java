package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;

public class ItemStudentEntity {
    @NonNull
    private final String id;

    @NonNull
    private final String name;

    public ItemStudentEntity(@NonNull String id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }
}
