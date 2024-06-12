package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;

public class ItemTeacherEntity {

    @NonNull
    private final String id;
    @NonNull
    private final String username;

    public ItemTeacherEntity(@NonNull String id, @NonNull String name) {
        this.id = id;
        this.username = name;
    }


    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return username;
    }
}
