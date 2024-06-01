package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;

public class ItemStudentEntity {
    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private final String surname;

    public ItemStudentEntity(@NonNull String id, @NonNull String name, @NonNull String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getSurname() {
        return surname;
    }

    @NonNull
    public String getFullName(){
        return getName() + " " + getSurname();
    }

    @NonNull
    public String getStringId(){
        return "ID: " + getId();
    }
}
