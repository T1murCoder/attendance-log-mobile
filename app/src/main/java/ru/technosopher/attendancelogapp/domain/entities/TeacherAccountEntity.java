package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;

public class TeacherAccountEntity {

    @NonNull
    private final String id;
    @NonNull
    private final String password;
    @NonNull
    private final String name;
    @NonNull
    private final String surname;
    @NonNull
    private final String username;

    public TeacherAccountEntity(@NonNull String id, @NonNull String name, @NonNull String surname,
                                @NonNull String username, @NonNull String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
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
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getId() {
        return id;
    }
}
