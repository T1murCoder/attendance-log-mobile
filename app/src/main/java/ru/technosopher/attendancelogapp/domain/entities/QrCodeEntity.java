package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;

import java.util.GregorianCalendar;

public class QrCodeEntity {

    @NonNull
    private final String id;

    @NonNull
    private final String lessonId;

    @NonNull
    private final GregorianCalendar createdAt;

    @NonNull
    private final GregorianCalendar expiresAt;

    public QrCodeEntity(@NonNull String id, @NonNull String lessonId, @NonNull GregorianCalendar createdAt, @NonNull GregorianCalendar expiresAt) {
        this.id = id;
        this.lessonId = lessonId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getLessonId() {
        return lessonId;
    }
    @NonNull
    public GregorianCalendar getCreatedAt() {
        return createdAt;
    }
    @NonNull
    public GregorianCalendar getExpiresAt() {
        return expiresAt;
    }
}
