package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;

public class ItemGroupEntity {

    @NonNull
    private final String id;
    @NonNull
    private final String name;

    public ItemGroupEntity(@NonNull String id, @NonNull String name) {
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
