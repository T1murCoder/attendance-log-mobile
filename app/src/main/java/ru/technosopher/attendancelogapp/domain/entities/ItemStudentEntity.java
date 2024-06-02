package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;

public class ItemStudentEntity {
    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private final String surname;

    @NonNull
    private final String username;
    public ItemStudentEntity(@NonNull String id, @NonNull String name, @NonNull String surname, @NonNull String username) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
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

    @NonNull
    public String getUsername() {
        return username;
    }
}
