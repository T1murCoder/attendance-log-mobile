package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TeacherEntity {

    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @NonNull
    private final String surname;
    @NonNull
    private final String username;
    @Nullable
    private final String telegram_url;
    @Nullable
    private final String github_url;
    @Nullable
    private final String photo_url;

    public TeacherEntity(@NonNull String id, @NonNull String name, @NonNull String surname,
                         @NonNull String username, @Nullable String telegram_url,
                         @Nullable String github_url, @Nullable String photo_url) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.telegram_url = telegram_url;
        this.github_url = github_url;
        this.photo_url = photo_url;
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
    public String getUsername() {
        return username;
    }

    @Nullable
    public String getTelegram_url() {
        return telegram_url;
    }

    @Nullable
    public String getGithub_url() {
        return github_url;
    }

    @Nullable
    public String getPhoto_url() {
        return photo_url;
    }
}
